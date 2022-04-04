package com.infybuzz.main.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infybuzz.main.itemprocessor.FirstItemProcessor;
import com.infybuzz.main.itemreader.FirstItemReader;
import com.infybuzz.main.itemwriter.FirstItemWriter;
import com.infybuzz.main.listener.FirstListener;
import com.infybuzz.main.listener.FirstStepListener;
import com.infybuzz.main.services.ThirdTasklet;

@Configuration
public class SampleJob {
	
	@Autowired
	JobBuilderFactory jobbuilderfactory;
	
	
	@Autowired
	StepBuilderFactory stepbuilderfactory;
	
	
	@Autowired
	ThirdTasklet thirdtasklet;
	
	@Autowired
	FirstListener firstlistener;
	
	@Autowired
	FirstStepListener firststeplistener;
	
	@Autowired
	FirstItemReader firstitemreader;
	
	@Autowired
	FirstItemProcessor firstitemprocessor;
	
	@Autowired
	FirstItemWriter firstitemwriter;
	
	
	
	@Bean
	public Job firstJob()
	{
		return jobbuilderfactory.get("First_Job") // to create job by using get() of jobbuilderfactory
				.incrementer(new RunIdIncrementer())
				.start(firstStep())
				.next(secondStep())
				.next(thirdStep())
				.listener(firstlistener)// this is jobExecutionlistener to intercept First_Job(name of job) to perform actions like cross cutting functions .
				.build();
	}
	
	
	public Step firstStep()
	{
		return stepbuilderfactory.get("First_Step") // to create step by using get() of stepbuilderfactory
				                 .listener(firststeplistener)				 
				                 .tasklet(firstTasklet())
								 .build();
				
	}
	
	public Tasklet firstTasklet()
	{
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("This is first tasklet step ");
				System.out.println(chunkContext.getStepContext());
				System.out.println(chunkContext.getStepContext().getJobExecutionContext());
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	public Step secondStep()
	{
		return stepbuilderfactory.get("Second_Step") // to create step by using get() of stepbuilderfactory
								 .tasklet(secondTasklet())
								 .build();
				
	}
	
	public Tasklet secondTasklet()
	{
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("This is second tasklet step ");
				System.out.println(chunkContext.getStepContext());
				System.out.println(chunkContext.getStepContext().getJobExecutionContext());
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	public Step thirdStep()
	{
		return stepbuilderfactory.get("Third_Step") // to create step by using get() of stepbuilderfactory
								 .tasklet(thirdtasklet)
								 .build();
				
	}
	
	
	
	@Bean
	public Job first_chunk_Job() // for chunk based example
	{
		return jobbuilderfactory.get("First_chunk_Job") 
				.incrementer(new RunIdIncrementer())
				.start(first_chunk_Step())
				.build();
	}
	
	
	public Step first_chunk_Step()
	{
		return stepbuilderfactory.get("first_chunk__Step") 
				                 .<Integer,Long>chunk(3)
				                 .reader(firstitemreader)
				                 .processor(firstitemprocessor)
				                 .writer(firstitemwriter)
								 .build();
				
	}
	
	
}
