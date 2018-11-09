/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 商品生产服务类入口
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.sipin.sales.cloud.merchandise.service.*","cn.siyue.platform.*"})
@MapperScan("cn.sipin.sales.cloud.merchandise.service.mapper*")
public class MerchandiseServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(MerchandiseServerApplication.class);

  }
}
