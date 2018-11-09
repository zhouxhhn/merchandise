/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.config.thirdparty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Primary
@ConfigurationProperties(prefix = "thirdparty.material-group-sync")
@Data
public class MaterialGroupProperties {

  /**
   * 物料同步地址
   */
  private String serverUrl;

  /**
   * 是否启用
   */
  private Boolean enable;

  /**
   * 同步间隔（分）
   */
  private Integer interval;

  /**
   * 延迟几分钟开始同步
   */
  private Integer delay;
}
