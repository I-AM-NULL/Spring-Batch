package com.infybuzz.main.controller;

import java.util.HashMap;
import java.util.Map;

import javax.batch.operations.JobOperator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infybuzz.main.services.AsyncService;

@RestController
@RequestMapping("/startingjobwithrestapi")
public class startingjobwithrestapi {

	
	@Autowired
	@Qualifier("firstJob")
	Job firstjob;
	
	@Autowired
	@Qualifier("first_chunk_Job")
	Job secondjob;
	
	@Autowired
	JobLauncher joblauncher;
	
	@Autowired
	AsyncService asyncservice;
	
	@Autowired
	JobOperator jobOperator;
	
	
	
	
	@GetMapping("/start/{job_name}")
	public String jobstarted(@PathVariable("job_name") String name) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException
	{
		Map<String,JobParameter> param = new HashMap<>();
		param.put("currentTime", new JobParameter(System.currentTimeMillis()));
		
		JobParameters jobparameters = new JobParameters(param);
		
		if(name.equals("First_job"))
		{
			joblauncher.run(firstjob,jobparameters);
			System.out.println("first job");
		
			
		}
		else if(name.equals("First_chunk_Job"))
		{
			joblauncher.run(secondjob,jobparameters );
			System.out.println("First_chunk_job");
		}
	
		
		return "Job started ....";
	}
	
	@GetMapping("/stop/{executionId}")
	public String stopJob(@PathVariable Long jobExecutionId )
	{
		jobOperator.stop(jobExecutionId);
		return "job stopped";
	}
	
	@GetMapping("/start/async/{job_name}") // asynchronous behaviour for above method
	public String Asyncjobstarted(@PathVariable("job_name") String name) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException
	{
		asyncservice.startjob(name);
		
		return "Job started ....";
	}
	
	
	
	
}
