/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;
import com.sipn.erp.v3.synchronization.material.client.MaterialClient;
import com.sipn.erp.v3.synchronization.material.client.MaterialClientBuilder;
import com.sipn.erp.v3.synchronization.material.client.entity.WarehouseVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.merchandise.client.config.MaterialWarehouseProperties;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_仓库管理_后台API")
@RequestMapping(path = "/backend", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BackendWarehouseController {

  private MaterialWarehouseProperties materialWarehouseProperties;

  @Autowired
  public BackendWarehouseController(MaterialWarehouseProperties materialWarehouseProperties) {
    this.materialWarehouseProperties = materialWarehouseProperties;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/warehouse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(nickname = "backendWarehouseList", value = "获取仓库列表")
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
