CREATE TABLE `material` (
  `material_id` bigint(20) NOT NULL COMMENT '物料ID',
  `number` varchar(50) NOT NULL DEFAULT '' COMMENT '物料编码',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '物料名称',
  `specification` varchar(150) NOT NULL DEFAULT '' COMMENT '物料规格',
  `texture` varchar(150) NOT NULL DEFAULT '' COMMENT '物料材质',
  `color` varchar(50) NOT NULL DEFAULT '' COMMENT '物料颜色',
  `spu` varchar(50) NOT NULL DEFAULT '' COMMENT 'SPU',
  `sku` varchar(50) NOT NULL DEFAULT '' COMMENT 'SKU',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'k3产品状态',
  `forbid_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '禁用状态',
  `img_path` varchar(150) DEFAULT NULL COMMENT '图片地址',
  `amount` decimal(12,2) NOT NULL COMMENT '售价',
  `stock_qty` int(11) NOT NULL DEFAULT '0' COMMENT '库存数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`material_id`),
  KEY `material_idx_id_number_sku` (`material_id`,`number`,`sku`)
) COMMENT='物料';