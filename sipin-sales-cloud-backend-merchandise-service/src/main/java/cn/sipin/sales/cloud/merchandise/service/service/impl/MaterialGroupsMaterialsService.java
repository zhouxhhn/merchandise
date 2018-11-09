package cn.sipin.sales.cloud.merchandise.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import cn.sipin.cloud.merchandise.pojo.MaterialGroupsMaterials;
import cn.sipin.sales.cloud.merchandise.service.mapper.MaterialGroupsMaterialsMapper;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialGroupsMaterialsServiceContract;

/**
 * <p>
 * 物料与组合关联表 服务实现类
 * </p>
 *
 * @author Sipin ERP Development Team
 */
@Primary
@Service
public class MaterialGroupsMaterialsService extends ServiceImpl<MaterialGroupsMaterialsMapper, MaterialGroupsMaterials> implements MaterialGroupsMaterialsServiceContract {

  @Override
  public boolean ignoreInsert(MaterialGroupsMaterials materialGroupsMaterials) {
    return  baseMapper.ignoreInsert(materialGroupsMaterials);
  }
}
