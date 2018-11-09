drop table if EXISTS material_group;
CREATE TABLE `material_group` (
  `material_id` bigint(20) NOT NULL COMMENT '物料组合ID',
  `number` varchar(50) NOT NULL DEFAULT '' COMMENT '物料组合编码',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '物料组合名称',
  `specification` varchar(150) NOT NULL DEFAULT '' COMMENT '物料组合规格',
  `texture` varchar(150) NOT NULL DEFAULT '' COMMENT '物料组合材质',
  `color` varchar(50) NOT NULL DEFAULT '' COMMENT '物料组合颜色',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `sku` varchar(50) DEFAULT NULL COMMENT 'SKU',
  `amount` decimal(12,2) NOT NULL COMMENT '售价',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`material_id`),
  KEY `material_group_idx_id_number_sku` (`material_id`,`number`,`sku`)
) COMMENT='物料组合';

CREATE TABLE `material_groups_materials` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `material_id` bigint(20) NOT NULL,
  `qty` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `material_groups_materials_idx_ids` (`group_id`,`material_id`)
) COMMENT ='物料与组合关联表';