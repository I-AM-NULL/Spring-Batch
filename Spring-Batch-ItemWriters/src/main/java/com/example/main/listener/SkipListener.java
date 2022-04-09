package com.example.main.listener;

import java.io.File;
import java.io.FileWriter;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.example.main.modal.StudentCsv;

@Component
public class SkipListener {
	
	@OnSkipInRead  // this annotation is for ItemReader
	public void skipInReading(Throwable th)
	{
		if(th instanceof FlatFileParseException)
		{
			createFile("D:\\Education\\Udemy\\Java\\Spring\\Spring batch\\Spring-Batch-ItemWriters\\job\\stepname\\itemreader\\errorRecords.txt", 
					((FlatFileParseException) th).getInput());
		}
	}
	
	public void createFile(String path , String data)
	{
		
		try(FileWriter fileWriter = new FileWriter(new File(path), true))
		{
			fileWriter.write(data + "\n");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	@OnSkipInProcess
	public void skipInProcessing(StudentCsv  studentCsv,Throwable th)
	{
		
	}
}
