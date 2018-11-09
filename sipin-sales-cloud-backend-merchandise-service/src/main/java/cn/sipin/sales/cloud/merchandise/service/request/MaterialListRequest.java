/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.request;

import java.util.List;

import lombok.Data;

@Data
public class MaterialListRequest {

  private String skuSn;

  private String skuNo;

  private String skuName;

  private String spu;

  private String discount;

  private Byte forbidStatusId;

  private List<Long> status;

  public MaterialListRequest() {
  }
}
