package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.EmailService;
import org.main.recap.jpa.JobDetailsRepository;
import org.main.recap.model.EmailPayLoad;
import org.main.recap.model.jpa.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by rajeshbabuk on 10/4/17.
 */
public class EmailProcessingTasklet implements Tasklet, StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailProcessingTasklet.class);

    @Value("${server.protocol}")
    private String serverProtocol;

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    private Date jobCreatedDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Sending Email");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();

        JobEntity jobEntity = jobDetailsRepository.findByJobName(jobName);

        EmailPayLoad emailPayLoad = new EmailPayLoad();
        emailPayLoad.setJobName(jobName);
        emailPayLoad.setJobDescription(jobEntity.getJobDescription());
        emailPayLoad.setStartDate(createdDate);
        emailPayLoad.setStatus("Successfully");
        String result = emailService.sendEmail(serverProtocol, solrClientUrl, emailPayLoad);
        logger.info("Email sending - {}", result);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        jobCreatedDate = (Date) executionContext.get(RecapConstants.JOB_CREATED_DATE);
        logger.info("Date Before Execution: {}", jobCreatedDate);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        if(jobCreatedDate != null) {
            logger.info("Date After Execution : {}",jobCreatedDate);
            executionContext.put(RecapConstants.JOB_CREATED_DATE, jobCreatedDate);
        }
        return null;
    }
}
