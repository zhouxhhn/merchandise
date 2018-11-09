package cn.sipin.sales.cloud.merchandise.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import cn.sipin.cloud.merchandise.pojo.Material;
import cn.sipin.sales.cloud.merchandise.service.request.MaterialListRequest;

/**
 * <p>
 * Mapper 接口
 * </p>
 */
public interface MaterialMapper extends BaseMapper<Material> {

  List<Material> selectMaterialPage(@Param("limit") int limit, @Param("offset") int offset, @Param("request") MaterialListRequest request);

  int selectMaterialPageCount(@Param("request") MaterialListRequest request);

  List<Material> selectWithoutStockPage(@Param("limit") int limit, @Param("offset") int offset, @Param("request") MaterialListRequest request);

  void insertIgnore(@Param("material") Material targetMaterial);

  List<Material> selectByNos(@Param("skuNoList") List<String> skuNoList);

  /**
   * 通过skuSn获取对应物料列表
   */
  List<Material> selectBySns(@Param("skuSnList") List<String> skuSnList);

  List<Material> selectOnlySnAndNoList();

  /**
   * 组合商品的number查询sku
   * @param skuNumber
   * @return
   */
  List<Material> getskuBygroupNumber(@Param("skuNumber") String skuNumber);
}
