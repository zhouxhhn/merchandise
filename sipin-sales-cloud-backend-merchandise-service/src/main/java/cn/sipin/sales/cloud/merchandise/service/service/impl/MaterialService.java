package cn.sipin.sales.cloud.merchandise.service.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sipn.erp.v3.synchronization.material.client.MaterialClient;
import com.sipn.erp.v3.synchronization.material.client.MaterialClientBuilder;
import com.sipn.erp.v3.synchronization.material.client.entity.MaterialVO;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

import cn.sipin.cloud.merchandise.constants.MerchandiseConstants;
import cn.sipin.cloud.merchandise.pojo.Material;
import cn.sipin.cloud.merchandise.response.MaterialResponse;
import cn.sipin.sales.cloud.merchandise.service.config.thirdparty.MaterialProperties;
import cn.sipin.sales.cloud.merchandise.service.contract.Loggable;
import cn.sipin.sales.cloud.merchandise.service.mapper.MaterialMapper;
import cn.sipin.sales.cloud.merchandise.service.request.MaterialListRequest;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialServiceContract;
import cn.sipin.sales.cloud.merchandise.service.service.RedisClusterServiceContract;
import cn.sipin.sales.cloud.merchandise.service.util.JsonTransformer;

/**
 * <p> 服务实现类 </p>
 */
@Primary
@Service
public class MaterialService extends ServiceImpl<MaterialMapper, Material> implements MaterialServiceContract, Loggable {

  private MaterialProperties materialProperties;

  private RedisClusterServiceContract redisService;

  @Autowired
  public MaterialService(MaterialProperties materialProperties, RedisClusterServiceContract redisService) {
    this.materialProperties = materialProperties;
    this.redisService = redisService;
  }

  @Override
  public void syncMaterial() {
    MaterialClient client = getMaterialCient();

    Integer page = 1;
    Integer size = 100;
    Page<MaterialVO> materialPage = client.selectPage(page, size);

    WeakHashMap<String, Material> redisMaterialMap = new WeakHashMap<String, Material>(100);

    while (materialPage.getPages() >= page) {
      materialPage.getRecords().forEach(ma -> {
        Material targetMaterial = new Material();
        BeanUtils.copyProperties(ma, targetMaterial);
        if (this.selectById(targetMaterial.getMaterialId()) != null) {

          Boolean isSuccess = this.updateById(targetMaterial);
          if (isSuccess && !Objects.isNull(targetMaterial.getNumber())) {
            redisMaterialMap.put(targetMaterial.getNumber(), targetMaterial);
          }
        } else {
          this.insertIgnore(targetMaterial);
          if (!Objects.isNull(targetMaterial.getNumber())) {
            redisMaterialMap.put(targetMaterial.getNumber(), targetMaterial);
          }
        }
      });

      page++;
      materialPage = client.selectPage(page, size);
    }

    if (redisMaterialMap.size() > 0) {
      logger().info("物料保存Redis --- 开始");
      // 插入redis hash表中
      initSkustoRedis(redisMaterialMap);
      logger().info("物料保存Redis --- 结束");
    }
  }

  @Override
  public void insertIgnore(Material targetMaterial) {

    baseMapper.insertIgnore(targetMaterial);
  }

  @Override public Page<MaterialResponse> selectMaterialPage(Page<MaterialResponse> page, MaterialListRequest request) {

    List<Material> materialList = baseMapper.selectMaterialPage(page.getLimit(), page.getOffset(), request);
    if (materialList.size() <= 0) {
      return page;
    }

    List<MaterialResponse> materialResponses = new ArrayList<>(materialList.size());
    materialList.forEach(it -> {
      MaterialResponse materialResponse = new MaterialResponse();
      BeanUtils.copyProperties(it, materialResponse);
      materialResponse.setDiscount(request.getDiscount());
      materialResponses.add(materialResponse);
    });

    page.setTotal(baseMapper.selectMaterialPageCount(request));
    return page.setRecords(materialResponses);
  }

  @Override public Page<MaterialResponse> selectWithoutStockPage(Page<MaterialResponse> page, MaterialListRequest request) {

    List<Material> materialList = baseMapper.selectWithoutStockPage(page.getLimit(), page.getOffset(), request);

    if (materialList.size() <= 0) {
      return page;
    }

    List<MaterialResponse> materialResponses = new ArrayList<>(materialList.size());
    materialList.forEach(it -> {
      MaterialResponse materialResponse = new MaterialResponse();
      BeanUtils.copyProperties(it, materialResponse);
      materialResponse.setDiscount(request.getDiscount());
      materialResponses.add(materialResponse);
    });
    page.setTotal(baseMapper.selectMaterialPageCount(request));
    return page.setRecords(materialResponses);
  }

  @Override public List<Material> selectByNos(List<String> skuNoList) {
    return baseMapper.selectByNos(skuNoList);
  }

  @Override public List<Material> selectBySns(List<String> skuSnList) {
    List<Material> materials = baseMapper.selectBySns(skuSnList);

    return materials;
  }

  @Override public void initSkusToRedis() {
    // 缓存提供给销售订单优惠券用
    List<Material> allMaterialList = baseMapper.selectList(null);
    if (allMaterialList != null && allMaterialList.size() > 0) {
      HashMap<String, String> skuMap = new HashMap<>(allMaterialList.size());

      for (Material material : allMaterialList) {
        if (StringUtils.isBlank(material.getSku())) {
          continue;
        }
        skuMap.put(material.getSku(), material.getNumber());
      }
      redisService.hmset(MerchandiseConstants.REDIS_SKU_SN_NO_HASH, skuMap);

      for (Material material : allMaterialList) {
        if (StringUtils.isBlank(material.getNumber())) {
          continue;
        }
        skuMap.put(material.getNumber(), JsonTransformer.toJson(material));
      }

      redisService.hmset(MerchandiseConstants.REDIS_SKU_NO_MATERIAL_HASH, skuMap);
    }
  }

  @Override public List<Material> getskuBygroupNumber(String skuNumber) {
    List<Material> materials = baseMapper.getskuBygroupNumber(skuNumber);

    return materials;
  }

  private void initSkustoRedis(WeakHashMap<String, Material> materialMap) {

    WeakHashMap<String, String> skuSnAndNoMap = new WeakHashMap<>(materialMap.size());
    WeakHashMap<String, String> skuNoMaterialMap = new WeakHashMap<>(materialMap.size());
    for (Map.Entry<String, Material> entry : materialMap.entrySet()) {
      // key => skuNo, value => Material
      if (!Objects.isNull(entry.getValue().getSku())) {
        skuSnAndNoMap.put(entry.getValue().getSku(), entry.getKey());
      }

      skuNoMaterialMap.put(entry.getKey(), JsonTransformer.toJson(entry.getValue()));
    }
    redisService.hmset(MerchandiseConstants.REDIS_SKU_SN_NO_HASH, skuSnAndNoMap);

    redisService.hmset(MerchandiseConstants.REDIS_SKU_NO_MATERIAL_HASH, skuNoMaterialMap);
  }

  private MaterialClient getMaterialCient() {

    return MaterialClientBuilder.create(materialProperties.getServerUrl(), false);
  }
}
