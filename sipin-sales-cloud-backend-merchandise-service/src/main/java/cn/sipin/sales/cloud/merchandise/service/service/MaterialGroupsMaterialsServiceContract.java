package cn.sipin.sales.cloud.merchandise.service.service;

import com.baomidou.mybatisplus.service.IService;
import cn.sipin.cloud.merchandise.pojo.MaterialGroupsMaterials;

/**
 * <p>
 * 物料与组合关联表 服务类
 * </p>
 *
 * @author Sipin ERP Development Team
 */
public interface MaterialGroupsMaterialsServiceContract extends IService<MaterialGroupsMaterials> {

  boolean ignoreInsert(MaterialGroupsMaterials materialGroupsMaterials);
}
