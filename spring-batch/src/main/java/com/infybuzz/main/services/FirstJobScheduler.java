package com.infybuzz.main.services;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FirstJobScheduler {
	
	
	@Autowired
	@Qualifier("firstJob")
	Job firstjob;
	
	@Autowired
	JobLauncher joblauncher;
	
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void schedulingJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException
	{
		Map<String,JobParameter> param = new HashMap<>();
		param.put("currentTime", new JobParameter(System.currentTimeMillis()));
		
		JobParameters jobparameters = new JobParameters(param);
		
		
			joblauncher.run(firstjob,jobparameters);
			System.out.println("first job");
	}

}
