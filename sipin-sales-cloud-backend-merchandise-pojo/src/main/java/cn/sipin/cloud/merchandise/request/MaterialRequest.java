/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.request;

import java.util.List;

import cn.sipin.cloud.merchandise.response.AgencyCodeResponse;
import lombok.Data;

@Data
public class MaterialRequest {


  private Integer page;

  private Integer size;

  private String search;

  private AgencyCodeResponse agencyCodeResponse;

  private String name;

  private String skuNo;

  private String skuSn;

  private Byte forbidStatusId;

  private List<Long> status;
}
