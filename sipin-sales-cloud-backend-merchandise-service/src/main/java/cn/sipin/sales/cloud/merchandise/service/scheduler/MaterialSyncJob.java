/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.scheduler;



import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.sipin.sales.cloud.merchandise.service.service.MaterialServiceContract;

public class MaterialSyncJob implements Job {

  private static final Logger logger = LoggerFactory.getLogger(MaterialSyncJob.class);

  @Autowired
  private MaterialServiceContract materialService;

  @Override public void execute(JobExecutionContext context) throws JobExecutionException {

    logger.info("同步商品和库存开始");
     materialService.syncMaterial();
    logger.info("同步商品和库存结束");
  }



}
