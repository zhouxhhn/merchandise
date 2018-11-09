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

import cn.sipin.cloud.merchandise.pojo.MaterialGroup;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialGroupServiceContract;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialServiceContract;

public class MaterialGroupSyncJob implements Job {

  private static final Logger logger = LoggerFactory.getLogger(MaterialGroupSyncJob.class);

  @Autowired
  private MaterialGroupServiceContract materialGroupService;

  @Override public void execute(JobExecutionContext context) throws JobExecutionException {

    logger.info("同步商品组开始");
    materialGroupService.syncMaterialGroup();
    logger.info("同步商品组结束");
  }



}
