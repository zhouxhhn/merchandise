package cn.sipin.sales.cloud.merchandise.service.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

import cn.sipin.cloud.merchandise.pojo.Material;
import cn.sipin.cloud.merchandise.response.MaterialResponse;
import cn.sipin.sales.cloud.merchandise.service.request.MaterialListRequest;


/**
 * <p>
 * 服务类
 * </p>
 */
public interface MaterialServiceContract extends IService<Material> {

  public void syncMaterial();

  public void insertIgnore(Material targetMaterial);

  Page<MaterialResponse> selectMaterialPage(Page<MaterialResponse> materialPage, MaterialListRequest request);

  Page<MaterialResponse> selectWithoutStockPage(Page<MaterialResponse> materialPage, MaterialListRequest request);

  List<Material> selectByNos(List<String> skuNoList);

  List<Material> selectBySns(List<String> skuSnList);

  void initSkusToRedis();

  List<Material> getskuBygroupNumber(String skuNumber);
}
