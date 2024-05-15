package com.springbatch.configuration;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.springbatch.model.Product;

@Configuration
public class JobConfiguration {
	
	

@Bean
	public Step step(JobRepository jobRepogistry,
			DataSourceTransactionManager transactionManager,
			ItemReader<Product> reader,
			ItemWriter<Product> writer ,ItemProcessor<Product, Product> processor)
	{
		   return new StepBuilder("jobStep",jobRepogistry)
				.<Product,Product>chunk(5, transactionManager)
				.reader(reader)
				.writer(writer)
                .allowStartIfComplete(true) 
                 .processor(processor)
				.build();
	}
	
	 @Bean
	public FlatFileItemReader<Product>reader() {
		return new FlatFileItemReaderBuilder<Product>()
				.name("reader")
				.resource(new ClassPathResource("data.csv"))
				 .delimited()
				 .names("productId", "title", "description", "price", "discount")
				 .targetType(Product.class)
				 .build();
	}
	 
	
	 @Bean
public ItemWriter<Product> itemWriter(DataSource dataSource) {
	return new JdbcBatchItemWriterBuilder<Product>()
			 .sql("insert into products(product_id,title,description,price,discount,discounted_price)values(:productId, :title, :description, :price, :discount, :discountedPrice)")
             .dataSource(dataSource)
             .beanMapped()
             .build();
}	 
	 
	 public ItemProcessor<Product, Product> itemProcessor() {
	 return new CustomItemProcessor();
		
	}
	
	 
	 @Bean
      public Job jobBean(JobRepository jobRepository,JobCompleteNotify listener,Step step) {
      return new JobBuilder("job", jobRepository)
    			  .start(step)
    			  .listener(listener)
    			  .build();
		
	}
	
	

}
