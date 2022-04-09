package com.example.main.config;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.example.main.itemprocessor.FirstItemProcessor;
import com.example.main.itemreader.FirstItemReader;
import com.example.main.itemwriter.FirstItemWriter;
import com.example.main.listener.SkipListener;
import com.example.main.modal.StudentCsv;
import com.example.main.modal.StudentJdbc;
import com.example.main.modal.StudentJson;
import com.example.main.modal.StudentResponse;
import com.example.main.service.StudentService;




@Configuration
public class SampleJob {
	
	@Autowired
	JobBuilderFactory jobbuilderfactory;
	
	
	@Autowired
	StepBuilderFactory stepbuilderfactory;
	
	
	@Autowired
	FirstItemReader firstitemreader;
		
	@Autowired
	FirstItemProcessor firstitemprocessor;
	
	@Autowired
	FirstItemWriter firstitemwriter;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	SkipListener skipListener;
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource datasource()
	{
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.universitydatasource")
	public DataSource universityDataSource()
	{
		return DataSourceBuilder.create().build();
	}
	
//	@Bean
	public Job firstJob()
	{
		return jobbuilderfactory.get("First_Job") 
				.incrementer(new RunIdIncrementer())
				.start(firstStep())
				.next(secondStep())
				.build();
	}
	
	
	public Step firstStep()
	{
		return stepbuilderfactory.get("First_Step") 		 
				                 .tasklet(firstTasklet())
								 .build();
				
	}
	
	public Tasklet firstTasklet()
	{
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is first tasklet step ");
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	public Step secondStep()
	{
		return stepbuilderfactory.get("Second_Step") 
								 .tasklet(secondTasklet())
								 .build();
				
	}
	
	public Tasklet secondTasklet()
	{
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is second tasklet step ");
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	
	@Bean
	public Job first_chunk_Job() 
	{
		return jobbuilderfactory.get("First_chunk_Job") 
				.incrementer(new RunIdIncrementer())
				.start(first_chunk_Step())
				.build();
	}
	
	
	public Step first_chunk_Step()
	{
		return stepbuilderfactory.get("first_chunk__Step") 
				                 .<StudentJdbc,StudentJdbc>chunk(3)
//				                 .reader(flatFileItemReaderAlternateWay())
//				                 .reader(jsonItemReader(null))
//				                 .reader(jdbcCursorItemReader())
				                 .reader(jdbcCursorItemReaderWithinanotherSchema())
//				                 .reader(itemReaderAdapter())
//				                 .processor(firstitemprocessor)
//				                 .writer(firstitemwriter)
//				                 .writer(flatFileItemWriter(null))
				                 .writer(jsonFileItemWriter(null))
				                 .faultTolerant()
//				                 .skip(FlatFileParseException.class)
//				                 .skip(NullPointerException.class)
				                 .skip(Throwable.class)// same as Exception.class // this is suggested by udemy
				                 .skip(Exception.class)// my own imagination
//				                 .skipLimit(1)
//				                 .skipLimit(Integer.MAX_VALUE) // if Exeption occurs it go on Skipping
				                 .skipPolicy(new AlwaysSkipItemSkipPolicy()) // if Exception occurs it always skip
				                 .listener(skipListener) // capturing badrecords 
				                 .build();
				
	}
	
	
	public FlatFileItemReader<StudentCsv> flatFileItemReader()// csv 1st normal method
	{
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<StudentCsv>();
		
		flatFileItemReader.setResource(new FileSystemResource(
				new File("C:\\Users\\ASUS\\Documents\\workspace-spring-tool-suite-4-4.13.1.RELEASE\\spring-batch-ItemReaders\\InputFiles\\students.csv")));
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("ID","First Name","Last Name","Email");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
					{
						setTargetType(StudentCsv.class);
					}
				});
			}
		});
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	@StepScope
	@Bean // Csv HardCoding of Source file is prohibhited
	public FlatFileItemReader<StudentCsv> flatFileItemReaderInsteadOfHardcodingSource(@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource)
	{
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<StudentCsv>();
		
		flatFileItemReader.setResource(fileSystemResource);
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("ID","First Name","Last Name","Email");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
					{
						setTargetType(StudentCsv.class);
					}
				});
			}
		});
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	public FlatFileItemReader<StudentCsv> flatFileItemReaderAlternateWay()// csv 1st normal method alternate implementaation
	{
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<StudentCsv>();
		
		DefaultLineMapper<StudentCsv> defaultLineMapper = new DefaultLineMapper<StudentCsv>();
		
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID","First Name","Last Name","Email");
		

		BeanWrapperFieldSetMapper<StudentCsv> fieldSetMapper=new BeanWrapperFieldSetMapper<StudentCsv>();
		fieldSetMapper.setTargetType(StudentCsv.class);
		
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);
		
		flatFileItemReader.setResource(new FileSystemResource(
				new File("C:\\Users\\ASUS\\Documents\\workspace-spring-tool-suite-4-4.13.1.RELEASE\\spring-batch-ItemReaders\\InputFiles\\students.csv")));
		flatFileItemReader.setLineMapper(defaultLineMapper);
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
	}
	
	@StepScope
	@Bean // for json
	public JsonItemReader<StudentJson> jsonItemReader(@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource)
	{
		JsonItemReader<StudentJson> jsonItemReader = 
				new JsonItemReader<StudentJson>();
		jsonItemReader.setResource(fileSystemResource);
		jsonItemReader.setJsonObjectReader(
				new JacksonJsonObjectReader<>(StudentJson.class));
		jsonItemReader.setMaxItemCount(8);// this will read only 8 rows from input file
		jsonItemReader.setCurrentItemCount(2); // we are setting cursor to 2nd index(that means we are skipping first two rows) 
		
		return jsonItemReader;
	}
	
	public JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader()// Data source with in metadata spring batch schema
	{
		JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<StudentJdbc>();
		
		jdbcCursorItemReader.setDataSource(datasource());
		jdbcCursorItemReader.setSql("select id,first_name as firstName,last_name as lastName,email from student");
		
        BeanPropertyRowMapper<StudentJdbc> beanPropertyRowMapper = new BeanPropertyRowMapper<StudentJdbc>();
        beanPropertyRowMapper.setMappedClass(StudentJdbc.class);
	
		
		jdbcCursorItemReader.setRowMapper(beanPropertyRowMapper);
		System.out.println("JDBC item REader");
		
		jdbcCursorItemReader.setCurrentItemCount(2);// starts to read from 2nd index or skips first two rows
		jdbcCursorItemReader.setMaxItemCount(8);// it takes 8 rows and as above , 2 rows are skipped
		return jdbcCursorItemReader;
	}
	
	public JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReaderWithinanotherSchema()// Data source with in metadata spring batch schema
	{
		JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<StudentJdbc>();
		
		jdbcCursorItemReader.setDataSource(universityDataSource());
		jdbcCursorItemReader.setSql("select id,first_name as firstName,last_name as lastName,email from student");
		
        BeanPropertyRowMapper<StudentJdbc> beanPropertyRowMapper = new BeanPropertyRowMapper<StudentJdbc>();
        beanPropertyRowMapper.setMappedClass(StudentJdbc.class);
	
		
		jdbcCursorItemReader.setRowMapper(beanPropertyRowMapper);
		System.out.println("university JDBC item REader");
		
		jdbcCursorItemReader.setCurrentItemCount(2);// starts to read from 2nd index or skips first two rows
		jdbcCursorItemReader.setMaxItemCount(8);// it takes 8 rows and as above , 2 rows are skipped
		return jdbcCursorItemReader;
	}
	
	
	public ItemReaderAdapter<StudentResponse> itemReaderAdapter()// ItemReaderAdapter is for reading from Rest Api Data Source
	{
		ItemReaderAdapter<StudentResponse> itemReaderAdapter = new ItemReaderAdapter<StudentResponse>();
		
		itemReaderAdapter.setTargetObject(studentService);
		itemReaderAdapter.setTargetMethod("getStudent");// we need to get single object not all objects , so we created a methond which gives single object
		
		return itemReaderAdapter;
		
	}
	
	@StepScope
	@Bean
	public FlatFileItemWriter<StudentJdbc> flatFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource)
	{
		System.out.println("flatFileItemWriter");
		 FlatFileItemWriter<StudentJdbc> flatFileItemWriter = new FlatFileItemWriter<StudentJdbc>();
		 flatFileItemWriter.setResource(fileSystemResource);
		 flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {// these are Headers of csv file
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				
				writer.write("id,first Name,last Name,email");
				
			}
		});
		 flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<StudentJdbc>() {
				{
					setFieldExtractor(new BeanWrapperFieldExtractor<StudentJdbc>() {
						{
							setNames(new String[] {"id", "firstName", "lastName", "email"});
						}
					});
				}
			});
		 
		 flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
				@Override
				public void writeFooter(Writer writer) throws IOException {
					writer.write("Created @ " + new Date());
				}
			});
		 return flatFileItemWriter;
	}
	
	@StepScope
	@Bean
	public JsonFileItemWriter<StudentJdbc> jsonFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource)
	{// StudentJdbc we can use , anyPojo we can use
		System.out.println("flatFileItemWriter");
		JsonFileItemWriter<StudentJdbc> jsonFileItemWriter = new JsonFileItemWriter<StudentJdbc>(fileSystemResource,
				new JacksonJsonObjectMarshaller<>());
		
		 return jsonFileItemWriter;
	}
	
	
}
