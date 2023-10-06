package com.tech.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Helper {
	public static final ObjectMapper objectMapper = new ObjectMapper();

	public static String toString(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
}