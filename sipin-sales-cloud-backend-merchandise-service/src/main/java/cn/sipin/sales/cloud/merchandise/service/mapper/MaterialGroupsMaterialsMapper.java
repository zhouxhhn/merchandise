package cn.sipin.sales.cloud.merchandise.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import cn.sipin.cloud.merchandise.pojo.MaterialGroup;
import cn.sipin.cloud.merchandise.pojo.MaterialGroupsMaterials;

/**
 * <p>
 * 物料与组合关联表 Mapper 接口
 * </p>
 *
 * @author Sipin ERP Development Team
 */
public interface MaterialGroupsMaterialsMapper extends BaseMapper<MaterialGroupsMaterials> {
  boolean ignoreInsert(@Param("materialGroupsMaterials") MaterialGroupsMaterials materialGroupsMaterials);
}
