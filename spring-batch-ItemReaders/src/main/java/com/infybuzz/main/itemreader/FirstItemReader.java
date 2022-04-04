package com.infybuzz.main.itemreader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class FirstItemReader implements ItemReader<Integer> {
	
	List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
	int i =0 ;
	Integer item ;

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		System.out.println("Inside Item Reader");
		if(i<list.size())
		{
			item = list.get(i);
			System.out.println(item);
			i++;
			return item;
		}
		return null;
	}

}
