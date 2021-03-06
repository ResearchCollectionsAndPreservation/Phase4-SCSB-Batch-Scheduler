package org.recap.batch.flow;

import org.recap.PropertyKeyConstants;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by rajeshbabuk on 13/7/17.
 */
public class DataExportExecutionDecider implements JobExecutionDecider {

    @Value("${" + PropertyKeyConstants.INCLUDE_DATA_EXPORT_IN_SEQUENCE_JOB + "}")
    private boolean includeDataExportInSequenceJob;

    /**
     * This method decides if the data export should be included in the job sequence process.
     *
     * @param jobExecution
     * @param stepExecution
     * @return FlowExecutionStatus
     */
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (includeDataExportInSequenceJob) {
            return FlowExecutionStatus.COMPLETED;
        } else {
            return FlowExecutionStatus.FAILED;
        }
    }
}
