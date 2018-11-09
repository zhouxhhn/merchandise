/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.controller;

import cn.sipin.sales.cloud.merchandise.service.service.RedisClusterServiceContract;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import com.baomidou.mybatisplus.plugins.Page;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import cn.sipin.cloud.merchandise.pojo.Material;
import cn.sipin.cloud.merchandise.request.MaterialRequest;
import cn.sipin.cloud.merchandise.response.MaterialResponse;
import cn.sipin.sales.cloud.merchandise.service.request.MaterialListRequest;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialServiceContract;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "商品管理-服务端")
@RequestMapping(path = "/sku", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController {

  private final static Logger logger = LoggerFactory.getLogger(MaterialController.class);

  @Autowired
  private MaterialServiceContract materialService;

  @Autowired
  private RedisClusterServiceContract lettuceClusterService;

  @PostMapping("/with-stock")
  @ApiOperation(nickname = "getAllSkuWithStock", value = "获取商品(包含正品仓库存)列表分页")
  public ResponseData index(
      @RequestBody MaterialRequest request
  ) {
    Page<MaterialResponse> materialPage = new Page<MaterialResponse>(request.getPage(), request.getSize());
    materialPage.setAsc(false);
    MaterialListRequest listRequest = buildRequest(request);
    listRequest.setDiscount(request.getAgencyCodeResponse().getAgencyDiscount());
    materialPage = materialService.selectMaterialPage(materialPage, listRequest);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        materialPage
    );
  }

  @PostMapping("/without-stock")
  @ApiOperation(nickname = "getAllSkuWithoutStock", value = "获取商品(不包含库存)列表分页")
  public ResponseData indexWithoutStock(
      @RequestBody MaterialRequest request
  ) {
    Page<MaterialResponse> materialPage = new Page<MaterialResponse>(request.getPage(), request.getSize());
    materialPage.setAsc(false);
    MaterialListRequest listRequest = buildRequest(request);
    listRequest.setDiscount(request.getAgencyCodeResponse().getAgencyDiscount());

    Gson gson=new Gson();
    System.out.print("materialPage="+gson.toJson(materialPage));
    System.out.print("listRequest="+gson.toJson(listRequest));

    materialPage = materialService.selectWithoutStockPage(materialPage, listRequest);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        materialPage
    );
  }

  @GetMapping("/sku-list")
  @ApiOperation(nickname = "getSkusBySkuNoList", value = "通过商品编码列表获取商品列表")
  public ResponseData getSkusBySkuNos(
      @RequestParam(value = "skuNos") String skuNos
  ) {
    List<String> skuNoList = Lists.newArrayList(Splitter.on(",").split(skuNos));

    List<Material> materials = materialService.selectByNos(skuNoList);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        materials
    );
  }

  @GetMapping("/sku-list-by-sn")
  @ApiOperation(nickname = "getSkusBySkuSnList", value = "通过商品SN列表获取商品列表")
  public ResponseData getSkusBySkuSns(
      @RequestParam(value = "skuSns") String skuSns
  ) {
    List<String> skuSnList = Lists.newArrayList(Splitter.on(",").split(skuSns));

    List<Material> materials = materialService.selectBySns(skuSnList);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        materials
    );
  }

  @GetMapping("/get-sku-by-group-number")
  @ApiOperation(nickname = "getskuBygroupNumber", value = "通过组合商品SN获取商品列表")
  public ResponseData getskuBygroupNumber( @RequestParam(value = "skuNumber") String skuNumber) {
    List<Material> materials = materialService.getskuBygroupNumber(skuNumber);
    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        materials
    );
  }

  @GetMapping("/unablesku-list")
  @ApiOperation(nickname = "getUnableSkus", value = "获取需要排除的物料列表")
  public ResponseData getUnableSkus() {

    String[] set = new String[]{};

    try {
      //是否开启排除物料
      String openUnableMaterial = lettuceClusterService.get("openUnableMaterial");
      //如果开关不存在，默认是开启
      if (openUnableMaterial==null||openUnableMaterial.toLowerCase().equals("true")) {
        //获取需要排除的物料字符串
        String unableMaterialContent = lettuceClusterService.get("unableMaterial");

        if (unableMaterialContent != null) {
          //物料使用逗号分隔
          set = unableMaterialContent.split(",");
        }
      }
    }catch (Exception ex){
      logger.error("获取排除物料列表失败，ex:"+ex.getMessage());
    }

    return ResponseData.build(
            ResponseBackCode.SUCCESS.getValue(),
            ResponseBackCode.SUCCESS.getMessage(),
            set
    );
  }

  private MaterialListRequest buildRequest(MaterialRequest materialRequest) {
    // 如果是非数字 设置为SKU名称
    // 如果是数字，设置为SKU-NO
    MaterialListRequest request = new MaterialListRequest();

    if (StringUtils.isBlank(materialRequest.getSearch())) {
      // 提供给后端API
      request.setSkuNo(materialRequest.getSkuNo());
      request.setSkuSn(materialRequest.getSkuSn());
      request.setSkuName(materialRequest.getName());
      request.setForbidStatusId(materialRequest.getForbidStatusId());
      request.setStatus(materialRequest.getStatus());

      return request;
    }

    // 前端API
    String search = StringUtils.trim(materialRequest.getSearch());
    if (StringUtils.isNumeric(search)) {
      request.setSkuNo(search);
    }else if (materialRequest.getSearch().startsWith("TZ")){
      request.setSkuNo(search);
    } else {
      request.setSkuName(search);
    }
    return request;
  }
}

