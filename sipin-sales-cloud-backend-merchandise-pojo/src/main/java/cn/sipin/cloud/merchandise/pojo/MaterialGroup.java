package cn.sipin.cloud.merchandise.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物料组合
 * </p>
 *
 * @author Sipin ERP Development Team
 */
@Data
@EqualsAndHashCode(exclude = { "createdAt", "updatedAt" }, callSuper = true)
@TableName("material_group")
public class MaterialGroup extends Model<MaterialGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 物料组合ID
     */
    @TableId("material_id")
    private Long materialId;
    /**
     * 物料组合编码
     */
    private String number;
    /**
     * 物料组合名称
     */
    private String name;
    /**
     * 物料组合规格
     */
    private String specification;
    /**
     * 物料组合材质
     */
    private String texture;
    /**
     * 物料组合颜色
     */
    private String color;
    /**
     * 是否启用
     */
    private Integer status;
    /**
     * SKU
     */
    private String sku;
    /**
     * 售价
     */
    private BigDecimal amount;
    @TableField("created_at")
    private Date createdAt;
    @TableField("updated_at")
    private Date updatedAt;


    @Override
    protected Serializable pkVal() {
        return this.materialId;
    }

}
