package com.example.main.itemwriter;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.main.modal.StudentJdbc;



@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {

	@Override
	public void write(List<? extends StudentJdbc> items) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Inside ItemWriter");
		items.stream().forEach(System.out::println);
		
	}
	
	

}
