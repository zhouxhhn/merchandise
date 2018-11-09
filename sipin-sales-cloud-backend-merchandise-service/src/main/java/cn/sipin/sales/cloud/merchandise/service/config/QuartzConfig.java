/*
 * (C) Copyright 2018 Siyue Holding Group.
 */

package cn.sipin.sales.cloud.merchandise.service.config;


import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

import cn.sipin.sales.cloud.merchandise.service.config.thirdparty.MaterialGroupProperties;
import cn.sipin.sales.cloud.merchandise.service.config.thirdparty.MaterialProperties;
import cn.sipin.sales.cloud.merchandise.service.scheduler.MaterialGroupSyncJob;
import cn.sipin.sales.cloud.merchandise.service.scheduler.MaterialSyncJob;

/**
 * 定时任务配置
 */
@Configuration
public class QuartzConfig {

  private MaterialProperties materialProperties;

  private MaterialGroupProperties materialGroupProperties;

  @Autowired
  public QuartzConfig(MaterialProperties materialProperties,MaterialGroupProperties materialGroupProperties) {
    this.materialProperties = materialProperties;
    this.materialGroupProperties = materialGroupProperties;
  }

  @Bean
  public JobDetail materialSyncJobDetail() {
    return JobBuilder.newJob(MaterialSyncJob.class)
        .withIdentity(MaterialSyncJob.class.getName())
        .storeDurably()
        .build();
  }

  @Bean
  public JobDetail materialGroupSyncJob() {
    return JobBuilder.newJob(MaterialGroupSyncJob.class)
        .withIdentity(MaterialGroupSyncJob.class.getName())
        .storeDurably()
        .build();
  }



  @Bean
  @ConditionalOnProperty("thirdparty.material-sync.enable")
  public Trigger materialSyncJobTrigger() {
    SimpleScheduleBuilder scheduleBuilder =
        SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMinutes(materialProperties.getInterval())
            .repeatForever();

    return TriggerBuilder.newTrigger()
        .withIdentity(MaterialSyncJob.class.getName())
        .startAt(DateUtils.addMinutes(new Date(), materialProperties.getDelay()))
        .forJob(materialSyncJobDetail())
        .withSchedule(scheduleBuilder)
        .build();
  }

  @Bean
  @ConditionalOnProperty("thirdparty.material-group-sync.enable")
  public Trigger materialGroupSyncJobTrigger() {
    SimpleScheduleBuilder scheduleBuilder =
        SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMinutes(materialGroupProperties.getInterval())
            .repeatForever();

    return TriggerBuilder.newTrigger()
        .withIdentity(MaterialGroupSyncJob.class.getName())
        .startAt(DateUtils.addMinutes(new Date(), materialGroupProperties.getDelay()))
        .forJob(materialGroupSyncJob())
        .withSchedule(scheduleBuilder)
        .build();
  }
}
