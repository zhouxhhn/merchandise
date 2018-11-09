/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.sipin.sales.cloud.merchandise.service.contract.Loggable;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialServiceContract;

@Component
public class MaterialToRedisCommandLineRunner implements CommandLineRunner, Loggable {

  private MaterialServiceContract materialService;

  @Autowired
  public MaterialToRedisCommandLineRunner(MaterialServiceContract materialService) {
    this.materialService = materialService;
  }

  @Override public void run(String... args) throws Exception {
    if (args == null || args.length == 0) {
      return;
    }
    for (String arg : args) {
      if ("initSkusToRedis".equals(arg)) {
        logger().info("初始化物料数据 插入redis --- 开始");
        materialService.initSkusToRedis();
        logger().info("初始化物料数据 插入redis --- 结束");
      }
    }
  }
}
