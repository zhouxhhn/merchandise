/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.response;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MaterialResponse")
public class MaterialResponse {

  /**
   * 物料编码
   */
  @ApiModelProperty(value = "物料编码")
  private String number;

  /**
   * 物料名称
   */
  @ApiModelProperty(value = "物料名称")
  private String name;

  /**
   * 物料规格
   */
  @ApiModelProperty(value = "物料规格")
  private String specification;

  /**
   * 物料材质
   */
  @ApiModelProperty(value = "物料材质")
  private String texture;

  /**
   * 物料颜色
   */
  @ApiModelProperty(value = "物料颜色")
  private String color;

  /**
   * SPU
   */
  @ApiModelProperty(value = "SPU")
  private String spu;

  /**
   * SKU
   */
  @ApiModelProperty(value = "SKU")
  private String sku;

  /**
   * 是否启用，K3审核状态
   */
  @ApiModelProperty(value = "K3审核状态，是否启用")
  private Integer status;

  /**
   * 禁用状态：1为未禁用，0为已禁用 目前表示上下架状态
   */
  @ApiModelProperty(value = "禁用状态：1为未禁用，0为已禁用；目前表示上下架状态")
  private Integer forbidStatus;

  /**
   * 图片资源
   */
  @ApiModelProperty(value = "图片资源")
  private String imgPath;

  /**
   * 售价
   */
  @ApiModelProperty(value = "售价")
  private BigDecimal amount;

  /**
   * 库存数
   */
  @ApiModelProperty(value = "库存数")
  private Integer stockQty;

  /**
   * 经销商折扣
   */
  @ApiModelProperty(value = "经销商折扣")
  private String discount;

}
