/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.controller.front;

import com.baomidou.mybatisplus.plugins.Page;
import com.sipn.erp.v3.synchronization.material.client.MaterialClient;
import com.sipn.erp.v3.synchronization.material.client.MaterialClientBuilder;
import com.sipn.erp.v3.synchronization.material.client.entity.WarehouseVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.merchandise.client.config.MaterialWarehouseProperties;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_仓库管理_前台API")
@RequestMapping(path = "/front", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WarehouseController {

  private MaterialWarehouseProperties materialWarehouseProperties;

  @Autowired
  public WarehouseController(MaterialWarehouseProperties materialWarehouseProperties) {
    this.materialWarehouseProperties = materialWarehouseProperties;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/warehouse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(nickname = "frontWarehouseList", value = "获取仓库列表")
  public ResponseData<Page<WarehouseVO>> index() {
    Page<WarehouseVO> warehousePage = this.getMaterialCient().selectWarehousePage();

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        warehousePage
    );

  }

  private MaterialClient getMaterialCient() {
    return MaterialClientBuilder.create(materialWarehouseProperties.getServerUrl(), false);
  }
}
