package cn.sipin.cloud.merchandise.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物料模型
 * </p>
 */
@Data
@EqualsAndHashCode(exclude = { "createdAt", "updatedAt" }, callSuper = true)
@Accessors(chain = true)
public class Material extends Model<Material> {

  public static final String K3_ENABLED = "启用";

  private static final long serialVersionUID = 1L;

  /**
   * 物料ID
   */
  @TableId(value = "material_id", type = IdType.INPUT)
  private Long materialId;

  /**
   * 物料编码
   */
  private String number;

  /**
   * 物料名称
   */
  private String name;

  /**
   * 物料规格
   */
  private String specification;

  /**
   * 物料材质
   */
  private String texture;

  /**
   * 物料颜色
   */
  private String color;

  /**
   * SPU
   */
  private String spu;

  /**
   * SKU
   */
  private String sku;

  /**
   * 是否启用
   */
  @TableField("status")
  private Integer status;

  /**
   * 禁用状态：1为未禁用，0为已禁用
   */
  @TableField("forbid_status")
  private Integer forbidStatus;

  /**
   * 图片资源
   */
  @TableField("img_path")
  private String imgPath;

  /**
   * 售价
   */
  private BigDecimal amount;

  /**
   * 库存数
   */
  @TableField("stock_qty")
  private Integer stockQty;

  @TableField("created_at")
  private LocalDateTime createdAt;

  @TableField("updated_at")
  private LocalDateTime updatedAt;

  @Override
  protected Serializable pkVal() {
    return this.materialId;
  }
}
