package com.tech.scheduler.configuration;

import com.tech.scheduler.services.SchedulerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostLoadInit {

	private final SchedulerService schedulerService;

	@PostConstruct
	public void init() {
		schedulerService.init();
	}
}
