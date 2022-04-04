package com.infybuzz.main.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstListener implements JobExecutionListener {

	

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		System.out.println("beforejob execution listener");
		System.out.println("ExecutionContext "+jobExecution.getExecutionContext());
		System.out.println("Job Parameter "+jobExecution.getJobParameters());
		System.out.println("job_Name is "+jobExecution.getJobInstance().getJobName());
		jobExecution.getExecutionContext().put("context1", "one");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		System.out.println("afterjob execution listener");
		System.out.println("ExecutionContext "+jobExecution.getExecutionContext());
		System.out.println("Job Parameter "+jobExecution.getJobParameters());
		System.out.println("job_Name is "+jobExecution.getJobInstance().getJobName());
		
	}

}
