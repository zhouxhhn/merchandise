/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Primary
@ConfigurationProperties(prefix = "thirdparty.erp.warehouse-stock")
@Data
public class MaterialStockProperties {

  /**
   * 物料同步地址
   */
  private String serverUrl;


}
