package com.example.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.main.modal.StudentResponse;



@Service
public class StudentService {
	
	List<StudentResponse> list ;

	public List<StudentResponse> restCallToGetStudents()
	{
		RestTemplate restTemplate = new RestTemplate();
		
		 StudentResponse[] studentResponses= restTemplate.getForObject("http://localhost:8081/api/v1/students",
				StudentResponse[].class);
		
		 list = new ArrayList<StudentResponse>();
		 for(StudentResponse sr : studentResponses)
		 {
			 list.add(sr);
		 }
		
		return list;
		
	}
	
	
	public StudentResponse getStudent()
	{
		if(list==null)
		{
			restCallToGetStudents();
		}
		if(list!=null && !list.isEmpty())
		{
			return list.remove(0);
		}
		return null ;
	}
}
