package com.tech.scheduler.dto.request;

import com.tech.scheduler.dto.domain.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
	private String name;
	private String url;
	private String cron;
	@Builder.Default
	private Map<String, String> headers = new HashMap<>();
	private Job.JobMethod method;
	private Object body;
	private String timezone;
}