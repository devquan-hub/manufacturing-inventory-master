# 制造业进销存系统

一个完整的制造业进销存管理系统，包含采购、销售、库存、生产等核心模块，支持多租户架构。

## 技术栈

### 后端
- Spring Boot 3.2
- MyBatis-Plus 3.5
- MySQL 8.0
- Redis
- Spring Security + JWT

### 前端
- Vue 3 + Vite
- Element Plus
- Pinia
- Axios

## 项目结构

```
manufacturing-inventory/
├── backend/           # 后端 Spring Boot 项目
│   ├── src/main/java/
│   │   └── com/manufacturing/inventory/
│   │       ├── common/     # 通用组件
│   │       ├── config/      # 配置类
│   │       ├── controller/  # 控制器
│   │       ├── entity/      # 实体类
│   │       ├── mapper/      # 数据访问层
│   │       ├── security/    # 安全模块
│   │       └── service/     # 业务逻辑
│   └── src/main/resources/
│       └── application.yml
├── frontend/         # 前端 Vue 项目
│   └── src/
│       ├── api/       # API 接口
│       ├── router/    # 路由
│       ├── store/     # 状态管理
│       ├── utils/     # 工具类
│       └── views/     # 页面组件
└── sql/
    └── init.sql       # 数据库初始化脚本
```

## 快速开始

### 1. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
```

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 4. 访问系统

打开浏览器访问 http://localhost:3000

- 用户名: admin
- 密码: 123456

## 功能模块

1. **首页看板** - 统计概览
2. **商品管理** - 支持原材料/半成品/成品分类
3. **仓库管理** - 多仓库支持
4. **采购管理** - 采购订单、入库
5. **销售管理** - 销售订单、出库
6. **生产管理** - 生产计划、领料、报工、质检
7. **库存管理** - 库存查询、库存记录
8. **基础数据** - 供应商、客户、部门

## 多租户说明

系统通过 `tenant_id` 字段实现数据隔离，所有业务表都包含此字段。租户ID在用户登录时通过JWT Token传递给后端，MyBatis-Plus 拦截器自动处理租户过滤。

## 演示数据

初始化脚本包含演示租户、用户、商品、仓库等数据，可直接使用。
