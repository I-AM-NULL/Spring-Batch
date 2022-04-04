package com.infybuzz.main.services;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

@Service
public class ThirdTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Third step is implemented in seperate service class ");
		System.out.println(chunkContext.getStepContext());
		System.out.println(chunkContext.getStepContext().getJobExecutionContext());// to use jobcontext which  we implemented in First Listener
		return RepeatStatus.FINISHED;
	}
	


}
