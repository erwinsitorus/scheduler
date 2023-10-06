package com.tech.scheduler.controller;

import com.tech.scheduler.configuration.LogInterceptor;
import com.tech.scheduler.dto.domain.Job;
import com.tech.scheduler.dto.request.JobRequest;
import com.tech.scheduler.services.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@LogInterceptor("/scheduler/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchedulerController {
	private final SchedulerService schedulerService;

	@GetMapping("/list")
	public List<Job> list() {
		return schedulerService.list();
	}

	@PostMapping("/add")
	public ResponseEntity<Job> add(@RequestBody JobRequest request) {
		return ResponseEntity.ok(schedulerService.add(request));
	}

	@DeleteMapping("/{name}/delete")
	public void delete(@PathVariable(name = "name") String name) {
		schedulerService.delete(name);
	}
}