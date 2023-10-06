package com.tech.scheduler.services;

import com.tech.scheduler.Helper;
import com.tech.scheduler.dto.domain.Job;
import com.tech.scheduler.repositories.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

@Slf4j
public class SchedulerTask implements Callable<ScheduledFuture<?>> {
	private final TaskScheduler taskScheduler;
	private final WebClient webClient;
	private final JobRepository repository;
	private final Job job;

	public SchedulerTask(TaskScheduler taskScheduler, WebClient webClient, JobRepository repository, Job job) {
		this.taskScheduler = taskScheduler;
		this.webClient = webClient;
		this.repository = repository;
		this.job = job;
	}

	@Override
	public ScheduledFuture<?> call() {
		Job.JobMethod method = this.job.getMethod();
		WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = getRequestHeadersUriSpec(method);
		String zone = StringUtils.isNotBlank(job.getTimezone()) ? job.getTimezone() : TimeZone.getDefault().toZoneId().toString();

		TimeZone timeZone = TimeZone.getTimeZone(zone);

		try {
			return taskScheduler.schedule(
					() -> {
						job.setLastRun(new Timestamp(System.currentTimeMillis()));
						requestHeadersUriSpec
								.uri(job.getUrl())
								.headers(httpHeaders -> job.getHeaders().forEach((key, value) -> {
									httpHeaders.add(key, value);
									httpHeaders.add(key, value);
								}))
								.retrieve()
								.bodyToMono(String.class)
								.subscribe(
										data -> {
											// Handle the successful response data
											job.setResponse(data);
											repository.save(job);
											log.info("success to run scheduler: {}, response: {}", Helper.toString(job), data);
										}
										,
										error -> {
											// Handle errors
											job.setResponse(error.getMessage());
											repository.save(job);
											log.error("error to run scheduler: {}, error-message: {}", Helper.toString(job), error.getMessage());
										}
										,
										() ->
												// Handle completion (optional)
												log.info("request completed for scheduler: {}", Helper.toString(job))

								);
					},
					new CronTrigger(job.getCron(), timeZone)
			);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	private WebClient.RequestHeadersUriSpec<?> getRequestHeadersUriSpec(Job.JobMethod method) {
		switch (method) {
			case POST:
				return webClient.post();
			case PUT:
				return webClient.put();
			case PATCH:
				return webClient.patch();
			case DELETE:
				return webClient.delete();
			default:
				return webClient.get();
		}
	}
}