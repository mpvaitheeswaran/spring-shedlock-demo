package com.vaitheeswaran.shedlockdemo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaitheeswaran.shedlockdemo.model.MyUser;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

    private StepBuilderFactory stepBuilderFactory;

    private JobBuilderFactory jobBuilderFactory;

    private DataSource dataSource;
    
    public BatchConfig(StepBuilderFactory stepBuilderFactory, JobBuilderFactory jobBuilderFactory, DataSource dataSource) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.dataSource = dataSource;
    }


    @Bean
    FlatFileItemReader<MyUser> itemReader() {

        return new FlatFileItemReaderBuilder<MyUser>()
                .name("userItemReader")
                .resource(new ClassPathResource("test-data.csv"))
                .delimited()
                .delimiter(";")
                .names("name", "surname", "address")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MyUser>(){{
                    setTargetType(MyUser.class);

                }})
                .build();
    }

    @Bean
    JdbcBatchItemWriter<MyUser> itemWriter() {
        return new JdbcBatchItemWriterBuilder<MyUser>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO MY_USER (name, surname, address) VALUES (:name, :surname, :address)")
                .build();
    }

    @Bean
    Step step(){
        return stepBuilderFactory.get("step")
                .<MyUser, MyUser>chunk(10)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    Job job() {
        return jobBuilderFactory.get("job")
                .start(step())
                .build();
    }
}

