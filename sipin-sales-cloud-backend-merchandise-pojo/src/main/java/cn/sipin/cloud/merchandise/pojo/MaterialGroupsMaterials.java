package cn.sipin.cloud.merchandise.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物料与组合关联表
 * </p>
 *
 * @author Sipin ERP Development Team
 */
@Data
@Accessors(chain = true)
@TableName("material_groups_materials")
public class MaterialGroupsMaterials extends Model<MaterialGroupsMaterials> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("group_id")
    private Long groupId;
    @TableField("material_id")
    private Long materialId;
    private Integer qty;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
