package com.infybuzz.main.itemwriter;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.infybuzz.main.modal.StudentCsv;
import com.infybuzz.main.modal.StudentJdbc;
import com.infybuzz.main.modal.StudentJson;
import com.infybuzz.main.modal.StudentResponse;

@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {

	@Override
	public void write(List<? extends StudentJdbc> items) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Inside ItemWriter");
		items.stream().forEach(System.out::println);
		
	}
	
	

}
