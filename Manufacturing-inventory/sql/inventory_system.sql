/*
 Navicat Premium Data Transfer

 Source Server         : D_project
 Source Server Type    : MySQL
 Source Server Version : 80406
 Source Host           : localhost:3306
 Source Schema         : inventory_system

 Target Server Type    : MySQL
 Target Server Version : 80406
 File Encoding         : 65001

 Date: 14/04/2026 12:27:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `inventory`;

-- ----------------------------
-- Table structure for bas_customer
-- ----------------------------
DROP TABLE IF EXISTS `bas_customer`;
CREATE TABLE `bas_customer`  (
  `id` bigint NOT NULL COMMENT '客户ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户名称',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `bank` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户银行',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行账号',
  `level_type` tinyint NULL DEFAULT 1 COMMENT '客户等级',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_code`(`tenant_id`, `code`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bas_customer
-- ----------------------------
INSERT INTO `bas_customer` VALUES (1, 1, 'CUS001', '广州机电公司', '王总', '13911111111', NULL, NULL, NULL, 1, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_customer` VALUES (2, 1, 'CUS002', '深圳贸易商行', '刘总', '13922222222', NULL, NULL, NULL, 2, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);

-- ----------------------------
-- Table structure for bas_dept
-- ----------------------------
DROP TABLE IF EXISTS `bas_dept`;
CREATE TABLE `bas_dept`  (
  `id` bigint NOT NULL COMMENT '部门ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '上级部门ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bas_dept
-- ----------------------------
INSERT INTO `bas_dept` VALUES (1, 1, 0, '总经理室', 1, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_dept` VALUES (2, 1, 1, '生产部', 2, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_dept` VALUES (3, 1, 1, '采购部', 3, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_dept` VALUES (4, 1, 1, '销售部', 4, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_dept` VALUES (5, 1, 1, '仓库部', 5, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);

-- ----------------------------
-- Table structure for bas_product
-- ----------------------------
DROP TABLE IF EXISTS `bas_product`;
CREATE TABLE `bas_product`  (
  `id` bigint NOT NULL COMMENT '商品ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '件' COMMENT '单位',
  `category` tinyint NULL DEFAULT 1 COMMENT '类别: 1原材料 2半成品 3成品',
  `cost_price` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '成本价',
  `sale_price` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '销售价',
  `safety_stock` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '安全库存',
  `bar_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_code`(`tenant_id`, `code`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bas_product
-- ----------------------------
INSERT INTO `bas_product` VALUES (1, 1, 'RM001', '钢板', '10mm', '张', 1, 80.00, 100.00, 100.00, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_product` VALUES (2, 1, 'RM002', '螺丝', 'M8*30', '盒', 1, 15.00, 20.00, 50.00, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_product` VALUES (3, 1, 'RM003', '电机', '500W', '台', 1, 500.00, 650.00, 20.00, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_product` VALUES (4, 1, 'FG001', '电机组件', '标准型', '套', 3, 800.00, 1200.00, 30.00, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_product` VALUES (5, 1, 'FG002', '螺丝组件', '标准型', '套', 3, 30.00, 50.00, 50.00, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_product` VALUES (2042470511364280321, 1, 'FG003', '电控组件', '标准型', '件', 1, 1000.00, 2800.00, 100.00, NULL, NULL, 1, '20260410新增', '2026-04-10 13:11:32', '2026-04-10 13:11:32', 0, NULL, NULL);
INSERT INTO `bas_product` VALUES (2043347647545532417, 1, ' FG004', '电控组件FG004', '标准型', '件', 1, 1260.00, 2100.00, 30.00, NULL, NULL, 1, '电控组件FG004', '2026-04-12 23:16:57', '2026-04-12 23:16:57', 0, NULL, NULL);

-- ----------------------------
-- Table structure for bas_supplier
-- ----------------------------
DROP TABLE IF EXISTS `bas_supplier`;
CREATE TABLE `bas_supplier`  (
  `id` bigint NOT NULL COMMENT '供应商ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '供应商编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '供应商名称',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `bank` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户银行',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行账号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_code`(`tenant_id`, `code`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bas_supplier
-- ----------------------------
INSERT INTO `bas_supplier` VALUES (1, 1, 'SUP001', '东莞钢材公司', '张经理', '13811111111', NULL, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_supplier` VALUES (2, 1, 'SUP002', '深圳五金批发', '李经理', '13822222222', NULL, NULL, NULL, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);

-- ----------------------------
-- Table structure for bas_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `bas_warehouse`;
CREATE TABLE `bas_warehouse`  (
  `id` bigint NOT NULL COMMENT '仓库ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库名称',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '负责人ID',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库地址',
  `type` tinyint NULL DEFAULT 1 COMMENT '类型: 1原材料仓 2成品仓 3半成品仓',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认仓库',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_code`(`tenant_id`, `code`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bas_warehouse
-- ----------------------------
INSERT INTO `bas_warehouse` VALUES (1, 1, 'WH01', '原材料仓', NULL, NULL, NULL, 1, 1, 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_warehouse` VALUES (2, 1, 'WH02', '成品仓', NULL, NULL, NULL, 3, 1, 0, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);
INSERT INTO `bas_warehouse` VALUES (3, 1, 'WH03', '半成品仓', NULL, NULL, NULL, 2, 1, 0, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);

-- ----------------------------
-- Table structure for pro_material_req
-- ----------------------------
DROP TABLE IF EXISTS `pro_material_req`;
CREATE TABLE `pro_material_req`  (
  `id` bigint NOT NULL COMMENT '领料单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `req_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '领料单号',
  `plan_id` bigint NULL DEFAULT NULL COMMENT '关联生产计划ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `applicant_id` bigint NULL DEFAULT NULL COMMENT '申请人',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_req_no`(`req_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生产领料单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pro_material_req
-- ----------------------------

-- ----------------------------
-- Table structure for pro_material_req_detail
-- ----------------------------
DROP TABLE IF EXISTS `pro_material_req_detail`;
CREATE TABLE `pro_material_req_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `req_id` bigint NOT NULL COMMENT '领料单ID',
  `product_id` bigint NOT NULL COMMENT '物料ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10, 2) NOT NULL COMMENT '领料数量',
  `actual_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实际领料数量',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_req_id`(`req_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生产领料单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pro_material_req_detail
-- ----------------------------

-- ----------------------------
-- Table structure for pro_plan
-- ----------------------------
DROP TABLE IF EXISTS `pro_plan`;
CREATE TABLE `pro_plan`  (
  `id` bigint NOT NULL COMMENT '计划ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `plan_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划编号',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_id` bigint NULL DEFAULT NULL COMMENT '产品ID',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '生产部门',
  `plan_quantity` int NOT NULL COMMENT '计划数量',
  `completed_quantity` int NULL DEFAULT 0 COMMENT '已完成数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1待生产 2生产中 3已完成',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `expect_finish_date` date NULL DEFAULT NULL COMMENT '预计完成日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_plan_no`(`plan_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生产计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pro_plan
-- ----------------------------

-- ----------------------------
-- Table structure for pro_product
-- ----------------------------
DROP TABLE IF EXISTS `pro_product`;
CREATE TABLE `pro_product`  (
  `id` bigint NOT NULL COMMENT '报工单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `report_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报工单号',
  `plan_id` bigint NULL DEFAULT NULL COMMENT '关联生产计划ID',
  `material_req_id` bigint NULL DEFAULT NULL COMMENT '关联领料单ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '生产部门',
  `qualified_quantity` int NOT NULL COMMENT '合格数量',
  `defective_quantity` int NULL DEFAULT 0 COMMENT '不合格数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `report_time` datetime NULL DEFAULT NULL COMMENT '报工时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_report_no`(`report_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生产报工单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pro_product
-- ----------------------------

-- ----------------------------
-- Table structure for pro_quality
-- ----------------------------
DROP TABLE IF EXISTS `pro_quality`;
CREATE TABLE `pro_quality`  (
  `id` bigint NOT NULL COMMENT '质检单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `quality_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '质检单号',
  `product_id` bigint NOT NULL COMMENT '报工单ID',
  `inspect_quantity` int NOT NULL COMMENT '检验数量',
  `qualified_quantity` int NULL DEFAULT 0 COMMENT '合格数量',
  `defective_quantity` int NULL DEFAULT 0 COMMENT '不合格数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `inspector_id` bigint NULL DEFAULT NULL COMMENT '质检员',
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '质检结果',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_quality_no`(`quality_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '生产质检单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pro_quality
-- ----------------------------

-- ----------------------------
-- Table structure for pur_inbound
-- ----------------------------
DROP TABLE IF EXISTS `pur_inbound`;
CREATE TABLE `pur_inbound`  (
  `id` bigint NOT NULL COMMENT '入库单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `inbound_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '入库单号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联采购订单ID',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `warehouse_id` bigint NOT NULL COMMENT '入库仓库ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `total_amount` decimal(14, 2) NULL DEFAULT 0.00 COMMENT '入库总金额',
  `inbound_date` date NULL DEFAULT NULL COMMENT '入库日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_inbound_no`(`inbound_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购入库单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pur_inbound
-- ----------------------------

-- ----------------------------
-- Table structure for pur_inbound_detail
-- ----------------------------
DROP TABLE IF EXISTS `pur_inbound_detail`;
CREATE TABLE `pur_inbound_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `inbound_id` bigint NOT NULL COMMENT '入库单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10, 2) NOT NULL COMMENT '入库数量',
  `price` decimal(12, 2) NOT NULL COMMENT '单价',
  `amount` decimal(14, 2) NOT NULL COMMENT '金额',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_inbound_id`(`inbound_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购入库单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pur_inbound_detail
-- ----------------------------

-- ----------------------------
-- Table structure for pur_order
-- ----------------------------
DROP TABLE IF EXISTS `pur_order`;
CREATE TABLE `pur_order`  (
  `id` bigint NOT NULL COMMENT '订单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '入库仓库ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1待审核 2已审核 3已完成 4已取消',
  `total_amount` decimal(14, 2) NULL DEFAULT 0.00 COMMENT '订单总金额',
  `paid_amount` decimal(14, 2) NULL DEFAULT 0.00 COMMENT '已付款金额',
  `order_date` date NULL DEFAULT NULL COMMENT '下单日期',
  `expect_date` date NULL DEFAULT NULL COMMENT '预计到货日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE,
  INDEX `idx_supplier_id`(`supplier_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pur_order
-- ----------------------------
INSERT INTO `pur_order` VALUES (2043353153785880577, 1, 'PO802657792', 1, 1, 1, 0.00, 0.00, NULL, NULL, '', NULL, '2026-04-12 23:38:50', NULL, '2026-04-12 23:38:50', 0);
INSERT INTO `pur_order` VALUES (2043360218411507713, 1, 'PO394730496', 1, 1, 1, 0.00, 0.00, '2026-04-13', '2026-04-16', '测试新增采购订单', NULL, '2026-04-13 00:06:55', NULL, '2026-04-13 00:06:55', 0);

-- ----------------------------
-- Table structure for pur_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `pur_order_detail`;
CREATE TABLE `pur_order_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10, 2) NOT NULL COMMENT '采购数量',
  `price` decimal(12, 2) NOT NULL COMMENT '单价',
  `amount` decimal(14, 2) NOT NULL COMMENT '金额',
  `actual_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实际入库数量',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pur_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for sal_order
-- ----------------------------
DROP TABLE IF EXISTS `sal_order`;
CREATE TABLE `sal_order`  (
  `id` bigint NOT NULL COMMENT '订单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '出库仓库ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `total_amount` decimal(14, 2) NULL DEFAULT 0.00 COMMENT '订单总金额',
  `received_amount` decimal(14, 2) NULL DEFAULT 0.00 COMMENT '已收款金额',
  `order_date` date NULL DEFAULT NULL COMMENT '下单日期',
  `expect_date` date NULL DEFAULT NULL COMMENT '预计发货日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_order_no`(`order_no`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sal_order
-- ----------------------------

-- ----------------------------
-- Table structure for sal_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `sal_order_detail`;
CREATE TABLE `sal_order_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10, 2) NOT NULL COMMENT '销售数量',
  `price` decimal(12, 2) NOT NULL COMMENT '单价',
  `amount` decimal(14, 2) NOT NULL COMMENT '金额',
  `actual_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实际出库数量',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sal_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for sal_outbound
-- ----------------------------
DROP TABLE IF EXISTS `sal_outbound`;
CREATE TABLE `sal_outbound`  (
  `id` bigint NOT NULL COMMENT '出库单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `outbound_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '出库单号',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联销售订单ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `warehouse_id` bigint NOT NULL COMMENT '出库仓库ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `total_amount` decimal(14, 2) NULL DEFAULT 0.00 COMMENT '出库总金额',
  `outbound_date` date NULL DEFAULT NULL COMMENT '出库日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_outbound_no`(`outbound_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售出库单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sal_outbound
-- ----------------------------

-- ----------------------------
-- Table structure for sal_outbound_detail
-- ----------------------------
DROP TABLE IF EXISTS `sal_outbound_detail`;
CREATE TABLE `sal_outbound_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `outbound_id` bigint NOT NULL COMMENT '出库单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10, 2) NOT NULL COMMENT '出库数量',
  `price` decimal(12, 2) NOT NULL COMMENT '单价',
  `amount` decimal(14, 2) NOT NULL COMMENT '金额',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_outbound_id`(`outbound_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售出库单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sal_outbound_detail
-- ----------------------------

-- ----------------------------
-- Table structure for stc_check
-- ----------------------------
DROP TABLE IF EXISTS `stc_check`;
CREATE TABLE `stc_check`  (
  `id` bigint NOT NULL COMMENT '盘点单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `check_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盘点单号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `checker_id` bigint NULL DEFAULT NULL COMMENT '盘点人',
  `check_date` date NULL DEFAULT NULL COMMENT '盘点日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_check_no`(`check_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存盘点单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stc_check
-- ----------------------------

-- ----------------------------
-- Table structure for stc_check_detail
-- ----------------------------
DROP TABLE IF EXISTS `stc_check_detail`;
CREATE TABLE `stc_check_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `check_id` bigint NOT NULL COMMENT '盘点单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `book_quantity` decimal(10, 2) NULL DEFAULT NULL COMMENT '账面数量',
  `actual_quantity` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际数量',
  `profit_loss_quantity` decimal(10, 2) NULL DEFAULT NULL COMMENT '盈亏数量',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_check_id`(`check_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存盘点单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stc_check_detail
-- ----------------------------

-- ----------------------------
-- Table structure for stc_inventory
-- ----------------------------
DROP TABLE IF EXISTS `stc_inventory`;
CREATE TABLE `stc_inventory`  (
  `id` bigint NOT NULL COMMENT '库存ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '库存数量',
  `frozen_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '冻结数量',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_product_warehouse`(`tenant_id`, `product_id`, `warehouse_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_warehouse_id`(`warehouse_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stc_inventory
-- ----------------------------

-- ----------------------------
-- Table structure for stc_record
-- ----------------------------
DROP TABLE IF EXISTS `stc_record`;
CREATE TABLE `stc_record`  (
  `id` bigint NOT NULL COMMENT '记录ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `warehouse_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `type` tinyint NOT NULL COMMENT '类型: 1采购入库 2销售出库 3生产领料 4成品入库 5调拨出库 6调拨入库 7盘点调整',
  `in_out` tinyint NOT NULL COMMENT '出入库: 1入库 2出库',
  `quantity` decimal(10, 2) NOT NULL COMMENT '数量',
  `before_quantity` decimal(10, 2) NULL DEFAULT NULL COMMENT '操作前库存',
  `after_quantity` decimal(10, 2) NULL DEFAULT NULL COMMENT '操作后库存',
  `business_id` bigint NULL DEFAULT NULL COMMENT '业务单据ID',
  `business_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单据号',
  `operate_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `idx_operate_time`(`operate_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stc_record
-- ----------------------------

-- ----------------------------
-- Table structure for stc_transfer
-- ----------------------------
DROP TABLE IF EXISTS `stc_transfer`;
CREATE TABLE `stc_transfer`  (
  `id` bigint NOT NULL COMMENT '调拨单ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `transfer_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调拨单号',
  `from_warehouse_id` bigint NOT NULL COMMENT '调出仓库',
  `to_warehouse_id` bigint NOT NULL COMMENT '调入仓库',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `transfer_date` date NULL DEFAULT NULL COMMENT '调拨日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `idx_transfer_no`(`transfer_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存调拨单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stc_transfer
-- ----------------------------

-- ----------------------------
-- Table structure for stc_transfer_detail
-- ----------------------------
DROP TABLE IF EXISTS `stc_transfer_detail`;
CREATE TABLE `stc_transfer_detail`  (
  `id` bigint NOT NULL COMMENT '明细ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `transfer_id` bigint NOT NULL COMMENT '调拨单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quantity` decimal(10, 2) NOT NULL COMMENT '调拨数量',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_transfer_id`(`transfer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存调拨单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stc_transfer_detail
-- ----------------------------

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` bigint NOT NULL COMMENT '租户ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编码',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1, '演示租户', 'DEMO', '管理员', '13800138000', 1, NULL, '2026-04-10 10:12:22', '2026-04-10 10:12:22', 0, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT '用户ID',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` tinyint NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_username`(`tenant_id`, `username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (2042461277780262913, 1, 'admin', '$2a$10$XBYEnO7WBZlEqW98wNvKO.dxcvW0XwINmPBnYj1NJV1bLrGtMu.Pu', '系统管理员', NULL, NULL, NULL, 1, '2026-04-14 11:15:16', '2026-04-10 12:34:50', '2026-04-14 11:15:15', 0, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;