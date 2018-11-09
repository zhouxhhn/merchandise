package cn.sipin.sales.cloud.merchandise.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import cn.sipin.cloud.merchandise.pojo.MaterialGroup;

/**
 * <p>
 * 物料组合 Mapper 接口
 * </p>
 *
 * @author Sipin ERP Development Team
 */
public interface MaterialGroupMapper extends BaseMapper<MaterialGroup> {

  boolean insertGroup(@Param("materialGroup") MaterialGroup materialGroup);

}
