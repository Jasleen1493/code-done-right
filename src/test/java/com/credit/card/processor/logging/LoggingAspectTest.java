package com.credit.card.processor.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class LoggingAspectTest {
	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;
	
	private LoggingAspect loggingAspect = new LoggingAspect();
	
	@Test
	public void testLoggingAspect() throws Throwable {
	}
}
