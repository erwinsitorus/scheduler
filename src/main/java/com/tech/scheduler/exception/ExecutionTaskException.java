package com.tech.scheduler.exception;

public class ExecutionTaskException extends RuntimeException{
	private final String msg;

	public ExecutionTaskException(String msg) {
		super(msg);
		this.msg = msg;
	}
}