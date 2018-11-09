package cn.sipin.sales.cloud.merchandise.service.service;

import com.baomidou.mybatisplus.service.IService;
import cn.sipin.cloud.merchandise.pojo.MaterialGroup;

/**
 * <p>
 * 物料组合 服务类
 * </p>
 *
 * @author Sipin ERP Development Team
 */
public interface MaterialGroupServiceContract extends IService<MaterialGroup> {

   void syncMaterialGroup();

}
