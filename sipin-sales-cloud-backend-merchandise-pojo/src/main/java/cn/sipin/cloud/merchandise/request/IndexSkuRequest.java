/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.request;

import java.util.List;

import lombok.Data;

@Data
public class IndexSkuRequest {

  private List<Long> status;


  private String search;
}
