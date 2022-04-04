package com.infybuzz.main.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		System.out.println("before step  stepExecutionListener");
		stepExecution.getExecutionContext().put("stepContext1", "one");
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		System.out.println("after step  stepExecutionListener");
		System.out.println("StepExecutionContext afterFirstStep is "+stepExecution.getExecutionContext().get("stepContext1"));// StepContext
		System.out.println("JobExecutionContext is "+stepExecution.getExecutionContext());
		return null;
	}

}
