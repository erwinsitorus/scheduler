package com.tech.scheduler.services.impl;

import com.tech.scheduler.dto.domain.Job;
import com.tech.scheduler.dto.request.JobRequest;
import com.tech.scheduler.exception.ExecutionTaskException;
import com.tech.scheduler.repositories.JobRepository;
import com.tech.scheduler.services.SchedulerService;
import com.tech.scheduler.services.SchedulerTask;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SchedulerServiceImpl implements SchedulerService {
	private final TaskScheduler taskScheduler;
	private final WebClient webClient;
	private final JobRepository jobRepository;
	private final Map<String, ScheduledFuture<?>> schedulerMap = new HashMap<>();

	public SchedulerServiceImpl(TaskScheduler taskScheduler, WebClient.Builder webClientBuilder, JobRepository jobRepository) {
		this.taskScheduler = taskScheduler;
		this.webClient = webClientBuilder.build();
		this.jobRepository = jobRepository;
	}

	@Override
	public void init() {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);

		jobRepository.findAll().stream().filter(job -> !job.isDeleted()).forEach(job -> {
			SchedulerTask schedulerTask = new SchedulerTask(taskScheduler, webClient, jobRepository, job);
			Future<ScheduledFuture<?>> future = executor.submit(schedulerTask);
			try {
				schedulerMap.put(String.valueOf(job.getName()), future.get());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				throw new ExecutionTaskException(e.getMessage());
			}
		});
	}

	@Override
	public List<Job> list() {
		return jobRepository.findAll();
	}

	@SuppressWarnings("all")
	@Override
	public Job add(JobRequest request) {
		Job job = addJob(request);
		schedulerMap.put(String.valueOf(job.getName()), new SchedulerTask(taskScheduler, webClient, jobRepository, job).call());
		return job;
	}

	@Override
	public void delete(String jobName) {
		Job job = jobRepository.findByName(jobName);
		ScheduledFuture<?> future = schedulerMap.get(job.getName());

		job.setDeleted(true);
		jobRepository.save(job);

		future.cancel(true);
	}

	@Transactional
	public Job addJob(JobRequest request) {
		Job job = Job.builder()
				.url(request.getUrl())
				.name(request.getName())
				.headers(request.getHeaders())
				.cron(request.getCron())
				.body(request.getBody())
				.method(request.getMethod())
				.timezone(request.getTimezone())
				.build();

		return jobRepository.save(job);
	}
}