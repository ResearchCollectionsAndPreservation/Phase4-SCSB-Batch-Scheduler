package org.recap.batch.service;

import org.recap.ScsbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * Created by akulak on 10/5/17.
 */

@Service
public class DailyReconcilationService {

    @Autowired
    protected CommonService commonService;

    /**
     * This method makes a rest call to scsb circ microservice to initiate the daily reconciliation process.
     *
     * @param scsbCoreUrl    the scsb core url
     * @return status of the daily reconciliation process
     */
    public String dailyReconcilation(String scsbCoreUrl) {
        return commonService.executeService(scsbCoreUrl,  ScsbConstants.DAILY_RECONCILIATION_URL, HttpMethod.POST);
    }
}
