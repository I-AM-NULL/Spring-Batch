package com.infybuzz.main.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
	
	@Autowired
	@Qualifier("firstJob")
	Job firstjob;
	
	@Autowired
	@Qualifier("first_chunk_Job")
	Job secondjob;
	
	@Autowired
	JobLauncher joblauncher;
	
	@Async
	public void startjob(String name) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException
	{
		Map<String,JobParameter> param = new HashMap<>();
		param.put("currentTime", new JobParameter(System.currentTimeMillis()));
		
		JobParameters jobparameters = new JobParameters(param);
		JobExecution jobexecution = null ;
		
		if(name.equals("First_job"))
		{
			jobexecution = joblauncher.run(firstjob,jobparameters);
			System.out.println("first job");
			
		}
		else if(name.equals("First_chunk_Job"))
		{
			jobexecution = joblauncher.run(secondjob,jobparameters );
			System.out.println("First_chunk_job");
		}
	
	}

}
