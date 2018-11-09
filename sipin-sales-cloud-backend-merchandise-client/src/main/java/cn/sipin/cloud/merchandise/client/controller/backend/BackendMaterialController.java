/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;
import com.sipn.erp.v3.synchronization.material.client.MaterialClient;
import com.sipn.erp.v3.synchronization.material.client.MaterialClientBuilder;
import com.sipn.erp.v3.synchronization.material.client.entity.MaterialStockVO;
import com.sipn.erp.v3.synchronization.material.client.request.SkuWarehouseRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import cn.sipin.cloud.merchandise.client.config.MaterialStockProperties;
import cn.sipin.cloud.merchandise.client.service.MaterialService;
import cn.sipin.cloud.merchandise.client.service.impl.SalesUserServiceImpl;
import cn.sipin.cloud.merchandise.request.IndexSkuRequest;
import cn.sipin.cloud.merchandise.request.MaterialRequest;
import cn.sipin.cloud.merchandise.response.AgencyCodeResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_商品管理_后台API")
@RequestMapping(path = "/backend", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BackendMaterialController {

  private MaterialService materialService;

  private MaterialStockProperties materialStockProperties;

  private SalesUserServiceImpl salesUserService;

  @Autowired
  public BackendMaterialController(
      MaterialService materialService, MaterialStockProperties materialStockProperties, SalesUserServiceImpl salesUserService
  ) {
    this.materialService = materialService;
    this.materialStockProperties = materialStockProperties;
    this.salesUserService = salesUserService;
  }

  @GetMapping("/search-all-sku")
  @ApiOperation(nickname = "backendSearchAllSku", value = "获取所有SKU(含库存)列表")
  public ResponseData searchSku(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestParam(value = "skuNo", required = false) String skuNo,
      @RequestParam(value = "skuSn", required = false) String skuSn,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "forbidStatusId", required = false) Byte forbidStatusId,
      @Valid IndexSkuRequest indexAgencyRequest
  ) {

    AgencyCodeResponse response = salesUserService.getUserByToken();

    if(response.getAgencyDiscount() == null) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "经销商折扣信息为空"
      );
    }

    if((new BigDecimal(response.getAgencyDiscount())).compareTo(BigDecimal.ZERO) <= 0) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "经销商折扣信息有误"
      );
    }

    MaterialRequest request = new MaterialRequest();
    request.setPage(page);
    request.setSize(size);
    request.setSearch(null);
    request.setName(name);
    request.setSkuNo(skuNo);
    request.setSkuSn(skuSn);
    request.setForbidStatusId(forbidStatusId);
    request.setAgencyCodeResponse(response);
    request.setStatus(indexAgencyRequest.getStatus());
    try {
      return materialService.index(request);
    } catch (Exception e) {

      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @GetMapping("/search-all-sku-no-stock")
  @ApiOperation(nickname = "backendSearchAllSkuNoStock", value = "获取所有SKU(不包含库存)列表")
  public ResponseData searchSkuNoStock(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestParam(value = "skuNo", required = false) String skuNo,
      @RequestParam(value = "skuSn", required = false) String skuSn,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "forbidStatusId", required = false) Byte forbidStatusId,
      @Valid IndexSkuRequest indexAgencyRequest
  ) {

    AgencyCodeResponse response = salesUserService.getUserByToken();


    if(response.getAgencyDiscount() == null) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "经销商折扣信息为空"
      );
    }

    if((new BigDecimal(response.getAgencyDiscount())).compareTo(BigDecimal.ZERO) <= 0) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "经销商折扣信息有误"
      );
    }

    MaterialRequest request = new MaterialRequest();
    request.setPage(page);
    request.setSize(size);
    request.setSearch(null);
    request.setSkuNo(skuNo);
    request.setSkuSn(skuSn);
    request.setName(name);
    request.setForbidStatusId(forbidStatusId);
    request.setStatus(indexAgencyRequest.getStatus());
    request.setAgencyCodeResponse(response);
    try {
      return materialService.indexWithoutStock(request);
    } catch (Exception e) {

      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @GetMapping("/search-sku-warehouse-stock")
  @ApiOperation(nickname = "backendSearchSkuWarehouseStock", value = "获取指定仓库商品库存列表")
  public ResponseData searchWarehouseStock(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestParam(value = "warehouseId", required = true, defaultValue = "1") String warehouseId,
      @RequestParam(value = "search", required = false) String search
  ) {

    MaterialClient client = getMaterialCient();
    SkuWarehouseRequest request = new SkuWarehouseRequest();
    request.setWarehouseId(warehouseId);
    request.setSearch(search);
    Page<MaterialStockVO> materialStockVOPage = client.selectWarehouseStockPage(page, size, request);


    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        materialStockVOPage
    );


  }



  private MaterialClient getMaterialCient() {

    return MaterialClientBuilder.create(materialStockProperties.getServerUrl(), false);
  }

}


