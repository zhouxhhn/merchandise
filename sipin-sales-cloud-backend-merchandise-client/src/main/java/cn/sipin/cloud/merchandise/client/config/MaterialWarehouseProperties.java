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
@ConfigurationProperties(prefix = "thirdparty.erp.warehouse")
@Data
public class MaterialWarehouseProperties {

  /**
   * 物料仓库地址
   */
  private String serverUrl;
}
