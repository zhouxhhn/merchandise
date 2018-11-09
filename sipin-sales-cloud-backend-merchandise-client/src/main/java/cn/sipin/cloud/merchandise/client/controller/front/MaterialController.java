/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.controller.front;

import cn.sipin.cloud.merchandise.client.config.MaterialStockProperties;
import cn.sipin.cloud.merchandise.client.service.MaterialService;
import cn.sipin.cloud.merchandise.client.service.impl.SalesUserServiceImpl;
import cn.sipin.cloud.merchandise.client.utils.TransObject;
import cn.sipin.cloud.merchandise.request.IndexSkuRequest;
import cn.sipin.cloud.merchandise.request.MaterialRequest;
import cn.sipin.cloud.merchandise.response.AgencyCodeResponse;
import cn.sipin.cloud.merchandise.response.MaterialResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.sipn.erp.v3.synchronization.material.client.MaterialClient;
import com.sipn.erp.v3.synchronization.material.client.MaterialClientBuilder;
import com.sipn.erp.v3.synchronization.material.client.entity.MaterialStockVO;
import com.sipn.erp.v3.synchronization.material.client.request.SkuWarehouseRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

import javax.validation.Valid;

@RestController
@Api(tags = "经销商_商品管理_前台API")
@RequestMapping(path = "/front", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MaterialController {

  private MaterialService materialService;

  private MaterialStockProperties materialStockProperties;

  private SalesUserServiceImpl salesUserService;

  private int addPageNum=0;
  private Set<String> disUnableMap=new HashSet<String>();
  private List<MaterialResponse> newMaterialList=new ArrayList<MaterialResponse>();

  private final static Logger logger = LoggerFactory.getLogger(MaterialController.class);

  @Autowired
  public MaterialController(
      MaterialService materialService, MaterialStockProperties materialStockProperties, SalesUserServiceImpl salesUserService
  ) {
    this.materialService = materialService;
    this.materialStockProperties = materialStockProperties;
    this.salesUserService = salesUserService;

  }

  @GetMapping("/search-all-sku")
  @ApiOperation(nickname = "frontSearchAllSku", value = "获取所有SKU(含库存)列表")
  public ResponseData searchSku(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
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

    //请求并获取物料
    ResponseData result=getAllMaterial(page,size,indexAgencyRequest.getSearch(),indexAgencyRequest.getStatus(),response);

    /*//需要排除的物料清单
    newMaterialList=new ArrayList<MaterialResponse>();
    //需要排除的物料清单
    disUnableMap=this.getUnableMaterial();
    //重置额外加的页数
    addPageNum=0;
    unableMaterialList(result,page,size,indexAgencyRequest.getSearch(),indexAgencyRequest.getStatus(),response);*/

    return result;
  }

  @GetMapping("/search-all-sku-no-stock")
  @ApiOperation(nickname = "frontSearchAllSkuNoStock", value = "获取所有SKU(不包含库存)列表")
  public ResponseData searchSkuNoStock(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
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

      ResponseData result=getMaterialNoStock(page,size,indexAgencyRequest.getSearch(),indexAgencyRequest.getStatus(),response);

      /*//需要排除的物料清单
      disUnableMap=this.getUnableMaterial();
      //请求并获取物料
      newMaterialList=new ArrayList<MaterialResponse>();
      //重置额外加的页数
      addPageNum=0;
      //需要排除的物料清单
      unableMaterialList(result,page,size,indexAgencyRequest.getSearch(),indexAgencyRequest.getStatus(),response);*/

      return result;
  }

  /*
  排除部分在清单里的物料
   */
  private void unableMaterialList(ResponseData result,int page,int size,String search,List status,AgencyCodeResponse response){

    //如果redis中不存在排除列表，即直接返回
    if(disUnableMap.size()==0){
      return;
    }

    //判断当前获取到的物料是否存在在排队名单中，如果是，即过滤掉
    try {
      //把Object强转为mybatis plus的Page
      Page<MaterialResponse> pageObject=new TransObject<Page>().convertObject(result.getData(),Page.class);

      //当前接口返回的物料列表
      List<MaterialResponse> currentMaterialList=pageObject.getRecords();
      System.out.println("currentMaterialList size="+currentMaterialList.size());
      //排除后的剩下的物料列表

      for (int i=0;i<currentMaterialList.size();i++){
        MaterialResponse currentMaterial=new TransObject<MaterialResponse>().convertObject(currentMaterialList.get(i),MaterialResponse.class);
        //如果当前物料的编号不在排除名单中，即把当前物料加入到新的物料清单中
        if (disUnableMap.contains(currentMaterial.getNumber().toString())==false) {
          newMaterialList.add(currentMaterial);
        }
      }
      System.out.println("newMaterialList size="+newMaterialList.size());

      //如果过滤后剩下的数据量太少，即再获取一次，并合并
      //循环，直到第一页的数据量达到指定值
      while (true) {
          //如果当前数据少到单页的显示的数据，且没有搜索条件，即循环获取，直到获取到指定数量
          if(newMaterialList.size() < pageObject.getSize() && search.equals("")){

              //记录当前额外添加的页数
              addPageNum++;

              ResponseData getResult=getMaterialNoStock(page+addPageNum,size,search,status,response);

              //需要排除的物料清单
              unableMaterialList(getResult,page+addPageNum,size,search,status,response);
          }else{
              break;
          }
      }
      System.out.println("newMaterialList22222 size="+newMaterialList.size());

      //更新列表中的物料数据
      pageObject.setRecords(newMaterialList);
      pageObject.setCurrent(page+addPageNum);

      result.setData(pageObject);

    }catch(Exception ex){
      logger.error("排除部分在清单里的物料时异常，"+ex.getMessage());
    }
  }

  /*
  获取需要排除的物料清单
   */
  private Set<String>  getUnableMaterial(){
    Set<String> set=new HashSet<>();

    try{
      Collection<String> unableList= (Collection<String>)materialService.getUnableSkus().getData();

      if(unableList!=null){
          for (String item:unableList) {
              set.add(item);
          }
      }
    }catch (Exception ex){
      logger.error("获取需要排除的物料清单时异常，"+ex.getMessage());
    }

    return set;
  }

  /*
  获取所有SKU(不包含库存)列表
  */
  private ResponseData getMaterialNoStock(int page,int size,String search,List status,AgencyCodeResponse response){
    MaterialRequest request = new MaterialRequest();

    request.setPage(page);
    request.setSize(size);
    request.setSearch(search);
    request.setAgencyCodeResponse(response);
    if(status != null){
      request.setStatus(status);
    }

    try {
      return materialService.indexWithoutStock(request);
    } catch (Exception e) {

      return ResponseData.build(
              ResponseBackCode.ERROR_FAIL.getValue(),
              ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  /*
  获取所有SKU(含库存)列表
   */
  private ResponseData getAllMaterial(int page,int size,String search,List status,AgencyCodeResponse response){
    MaterialRequest request = new MaterialRequest();
    request.setPage(page);
    request.setSize(size);
    request.setSearch(search);
    request.setAgencyCodeResponse(response);
    if(status != null){
      request.setStatus(status);
    }
    try {
      return materialService.index(request);
    } catch (Exception e) {

      return ResponseData.build(
              ResponseBackCode.ERROR_FAIL.getValue(),
              ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @GetMapping("/search-sku-warehouse-stock")
  @ApiOperation(nickname = "frontSearchSkuWarehouseStock", value = "获取指定仓库商品库存列表")
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


