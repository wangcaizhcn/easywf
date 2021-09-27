package com.easywf.wf.process.event;

import org.springframework.context.ApplicationEvent;

public class ProcessStartEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	public ProcessStartEvent(Object source) {
		super(source);
	}
}
