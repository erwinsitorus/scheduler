package com.tech.scheduler.services;

import com.tech.scheduler.dto.domain.Job;
import com.tech.scheduler.dto.request.JobRequest;

import java.util.List;

public interface SchedulerService {
	void init();

	List<Job> list();

	Job add(JobRequest request);

	void delete(String jobName);
}
