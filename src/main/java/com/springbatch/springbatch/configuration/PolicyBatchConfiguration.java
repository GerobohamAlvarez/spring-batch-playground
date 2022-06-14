package com.springbatch.springbatch.configuration;

import java.util.function.Function;

import javax.batch.runtime.StepExecution;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.mapping.PassThroughRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.springbatch.springbatch.dto.*;
import com.springbatch.springbatch.mappers.PolicyExcelRowMapper;

@Configuration
@EnableBatchProcessing
public class PolicyBatchConfiguration {

    @Autowired
    PolicyExcelRowMapper policyExcelRowMapper;

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public PolicyBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<PolicyDTO, PolicyDTO>chunk(3)
                .reader(itemReader())
                .processor((Function<? super PolicyDTO, ? extends PolicyDTO>) (item) -> {
                    item.setPolicy(item.getPolicy() + 9000000);
                    return item;
                })
                .listener(new ItemProcessListener<PolicyDTO, PolicyDTO>() {

                    @Override
                    public void beforeProcess(PolicyDTO item) {
                        System.out.println("Before process ");
                    }

                    @Override
                    public void afterProcess(PolicyDTO item, PolicyDTO result) {
                        System.out.println("afterProcess process ");
                    }

                    @Override
                    public void onProcessError(PolicyDTO item, Exception e) {
                        System.out.println("Error procesando el item ");
                        System.out.println(e.getMessage());
                    }

                })
                .writer(itemWriter())
                .build();
    }

    @Bean
    ItemReader<PolicyDTO> itemReader() {
        PoiItemReader<PolicyDTO> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("xlsx/PolicyData.xlsx"));
        reader.setRowMapper(policyExcelRowMapper);
        return reader;
    }

    @Bean
    public ItemWriter<PolicyDTO> itemWriter() {
        return items -> {
            for (PolicyDTO item : items) {
                System.out.println("itemWriter");
                System.out.println(item);
            }
        };
    }

}
