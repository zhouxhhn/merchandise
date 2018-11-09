package cn.sipin.sales.cloud.merchandise.service.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sipn.erp.v3.synchronization.material.client.MaterialGroupClient;
import com.sipn.erp.v3.synchronization.material.client.MaterialGroupClientBuilder;
import com.sipn.erp.v3.synchronization.material.client.entity.MaterialGroupVo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import cn.sipin.cloud.merchandise.pojo.MaterialGroup;
import cn.sipin.cloud.merchandise.pojo.MaterialGroupsMaterials;
import cn.sipin.sales.cloud.merchandise.service.config.thirdparty.MaterialGroupProperties;
import cn.sipin.sales.cloud.merchandise.service.mapper.MaterialGroupMapper;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialGroupServiceContract;
import cn.sipin.sales.cloud.merchandise.service.service.RedisClusterServiceContract;

/**
 * <p>
 * 物料组合 服务实现类
 * </p>
 *
 * @author Sipin ERP Development Team
 */
@Primary
@Service
public class MaterialGroupService extends ServiceImpl<MaterialGroupMapper, MaterialGroup> implements MaterialGroupServiceContract {

  private MaterialGroupProperties materialGroupProperties;

  private MaterialGroupsMaterialsService materialGroupsMaterialsService;

  @Autowired
  public MaterialGroupService(MaterialGroupProperties materialGroupProperties,MaterialGroupsMaterialsService materialGroupsMaterialsService) {
    this.materialGroupProperties = materialGroupProperties;
    this.materialGroupsMaterialsService = materialGroupsMaterialsService;
  }

  @Override
  public void syncMaterialGroup() {

    MaterialGroupClient client = getMaterialGroupCient();

    Integer page = 1;
    Integer size = 100;
    Page<MaterialGroupVo> materialPage = client.selectGroupByPage(page, size);

    WeakHashMap<String, MaterialGroup> redisMaterialMap = new WeakHashMap<>(100);

    while (materialPage.getPages() >= page) {
      List<MaterialGroupVo> materialGroupVoList = materialPage.getRecords();
      List<MaterialGroup> materialGroupList = new ArrayList<>();
      List<MaterialGroupsMaterials> materialGroupsMaterials = new ArrayList<>();
      Map<Long,String> groupMap = new HashMap<>();
      for (int i = 0; i<materialGroupVoList.size(); i++){
        MaterialGroup materialGroup = new MaterialGroup();
        MaterialGroupVo ma = materialGroupVoList.get(i);
        BeanUtils.copyProperties(ma, materialGroup);
        if (selectById(materialGroup.getMaterialId()) != null) {
          updateById(materialGroup);
        }else{
          baseMapper.insertGroup(materialGroup);
        }

        for (int j = 0; j<ma.getMaterialGroupsMaterialsList().size(); j++){
          com.sipn.erp.v3.synchronization.material.client.entity.MaterialGroupsMaterials materials = ma.getMaterialGroupsMaterialsList().get(j);
          MaterialGroupsMaterials groupsMaterials = new MaterialGroupsMaterials();
          BeanUtils.copyProperties(materials, groupsMaterials);
          if (materialGroupsMaterialsService.selectById(groupsMaterials.getId()) != null) {
            materialGroupsMaterialsService.updateById(groupsMaterials);
          }else{
            materialGroupsMaterialsService.ignoreInsert(groupsMaterials);
          }
        }
      }

      page++;
      materialPage = client.selectGroupByPage(page, size);
    }


  }

  private MaterialGroupClient getMaterialGroupCient() {

    return MaterialGroupClientBuilder.create(materialGroupProperties.getServerUrl(), false);
  }


}
