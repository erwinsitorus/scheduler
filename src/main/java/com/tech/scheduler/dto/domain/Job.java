package com.tech.scheduler.dto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class Job {

	public enum JobMethod {
		GET, POST, PATCH, PUT, DELETE
	}

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "url")
	private String url;

	@Column(name = "cron")
	private String cron;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "headers")
	@Builder.Default
	private Map<String, String> headers = new HashMap<>();

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "body")
	private Object body;

	@Enumerated(EnumType.STRING)
	@Column(name = "method")
	private JobMethod method;

	@Column(name = "last_run")
	private Timestamp lastRun;

	@Column(name = "response", columnDefinition="TEXT")
	private String response;

	@Column(name = "timezone")
	private String timezone;

	@Builder.Default
	private boolean deleted = false;
}