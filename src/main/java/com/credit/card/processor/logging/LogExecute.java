package com.credit.card.processor.logging;

import com.credit.card.processor.constants.GlobalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LogExecute implements CommandLineRunner {
	private final Logger log = LoggerFactory.getLogger(LogExecute.class);
	
	@Override
	public void run(String... args) {
		MDC.put("RECORD_ID", GlobalConstants.UNIQUE_LOG_ID);
		log.info("Test Log4j with MDC");
	}
}
