package com.tech.scheduler.repositories;

import com.tech.scheduler.dto.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
	Job findByName(String name);
}
