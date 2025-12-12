# 校园拼车系统部署说明

## 1 环境依赖

| 依赖项 | 版本要求 | 说明 |
|-------|---------|------|
| JDK | 1.8 或以上 | 后端开发和运行环境 |
| Maven | 3.6 或以上 | 项目构建工具 |
| Node.js | 14.x 或以上 | 前端开发环境 |
| npm | 6.x 或以上 | Node.js 包管理工具 |
| MySQL | 8.0 | 数据库服务器 |
| 微信开发者工具 | 稳定版 | 小程序开发和调试工具 |

## 2 数据库部署

### 2.1 创建数据库
1. 打开 MySQL 客户端（如 Navicat、MySQL Workbench 或命令行）
2. 登录 MySQL 数据库
3. 创建名为 `ccp_db` 的数据库：
   ```sql
   CREATE DATABASE ccp_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
   ```
4. 创建数据库用户并授权（可选，也可使用现有用户）：
   ```sql
   CREATE USER 'ccp_user'@'localhost' IDENTIFIED BY 'ccp_password';
   GRANT ALL PRIVILEGES ON ccp_db.* TO 'ccp_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

### 2.2 执行数据库脚本
1. 定位到项目根目录下的 `sql` 文件夹
2. 使用 MySQL 客户端执行 `ccp_db.sql` 脚本：
   ```sql
   USE ccp_db;
   SOURCE /path/to/ccp_db.sql;
   ```
3. 等待脚本执行完成，脚本将创建所有必要的表结构

### 2.3 环境配置差异

| 环境 | 配置差异 |
|-----|---------|
| 开发环境 | 使用本地 MySQL 数据库，配置简单 |
| 测试环境 | 使用测试服务器 MySQL 数据库，需要配置远程访问 |
| 生产环境 | 使用生产服务器 MySQL 数据库，需要配置高可用、备份策略 |

## 3 后端部署

### 3.1 配置文件修改
1. 定位到 `ruoyi-admin/src/main/resources` 目录
2. 编辑 `application.yml` 文件，修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/ccp_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
       username: ccp_user
       password: ccp_password
   ```
3. 编辑 `application.yml` 文件，修改微信小程序配置：
   ```yaml
   ccp:
     wx:
       appid: your_appid
       secret: your_secret
   ```
4. 编辑 `application.yml` 文件，修改小程序后端 BASE_URL：
   ```yaml
   ccp:
     mini:
       base-url: http://localhost:8080/mp
   ```

### 3.2 项目结构说明
- `ruoyi-admin` 是主应用，包含管理后台 API 和小程序 API
- `ccp-miniprogram` 是小程序后端模块，包含小程序相关的 Controller 和 Service
- `ccp-core` 是核心业务模块，包含所有业务逻辑
- `ccp-common` 是公共组件模块，包含通用的 DTO、VO、工具类等

### 3.3 Maven 打包和启动

#### 3.3.1 打包
1. 打开命令行工具，进入项目根目录
2. 执行 Maven 打包命令：
   ```bash
   mvn clean package
   ```
3. 等待打包完成，生成的 jar 文件位于 `ruoyi-admin/target` 目录下

#### 3.3.2 启动
1. 进入 `ruoyi-admin/target` 目录
2. 执行启动命令：
   ```bash
   java -jar ruoyi-admin.jar
   ```
3. 或使用 nohup 命令在后台运行：
   ```bash
   nohup java -jar ruoyi-admin.jar > log.out 2>&1 &
   ```
4. 等待应用启动完成，可通过 `http://localhost:8080` 访问管理后台

## 4 管理端前端部署（ruoyi-ui）

### 4.1 安装依赖
1. 打开命令行工具，进入 `ruoyi-ui` 目录
2. 执行 npm 安装命令：
   ```bash
   npm install
   ```
3. 等待依赖安装完成

### 4.2 启动开发模式
1. 在 `ruoyi-ui` 目录下执行启动命令：
   ```bash
   npm run dev
   ```
2. 等待启动完成，自动打开浏览器访问 `http://localhost:80`
3. 登录管理后台，默认用户名：admin，密码：123456

### 4.3 打包发布
1. 在 `ruoyi-ui` 目录下执行打包命令：
   ```bash
   npm run build
   ```
2. 等待打包完成，生成的静态文件位于 `dist` 目录下
3. 将 `dist` 目录下的文件部署到 Web 服务器（如 Nginx、Apache）

## 5 小程序前端部署（ccp-mp-ui）

### 5.1 导入项目
1. 打开微信开发者工具
2. 点击「导入项目」按钮
3. 选择 `ccp-mp-ui` 目录作为项目目录
4. 输入小程序 AppID（若没有，可使用测试号）
5. 选择「不使用云服务」
6. 点击「导入」按钮

### 5.2 配置文件修改
1. 定位到 `ccp-mp-ui/utils` 目录
2. 编辑 `config.js` 文件，修改 API 基础 URL：
   ```javascript
   const BASE_URL = 'http://localhost:8080/mp';
   ```
3. 编辑 `project.config.json` 文件，确保 `appid` 与实际使用的小程序 AppID 一致：
   ```json
   {
     "appid": "your_appid",
     "projectname": "校园拼车系统",
     "description": "校园拼车系统小程序",
     "setting": {
       "urlCheck": true,
       "es6": true,
       "enhance": true,
       "postcss": true,
       "preloadBackgroundData": false,
       "minified": true,
       "newFeature": true,
       "coverView": true,
       "nodeModules": false,
       "autoAudits": false,
       "showShadowRootInWxmlPanel": true,
       "scopeDataCheck": false,
       "uglifyFileName": false,
       "checkInvalidKey": true,
       "checkSiteMap": true,
       "uploadWithSourceMap": true,
       "compileHotReLoad": false,
       "lazyloadPlaceholderEnable": false,
       "useMultiFrameRuntime": false,
       "useApiHook": true,
       "useApiHostProcess": true,
       "showES6CompileOption": false,
       "babelSetting": {
         "ignore": [],
         "disablePlugins": [],
         "outputPath": ""
       },
       "enableEngineNative": false,
       "useIsolateContext": true,
       "userConfirmedBundleSwitch": false,
       "packNpmManually": false,
       "packNpmRelationList": [],
       "minifyWXSS": true,
       "useCompilerPlugins": false
     },
     "compileType": "miniprogram",
     "libVersion": "2.15.0",
     "appid": "your_appid",
     "projectname": "校园拼车系统",
     "debugOptions": {
       "hidedInDevtools": []
     },
     "isGameTourist": false,
     "simulatorType": "wechat",
     "simulatorPluginLibVersion": {},
     "condition": {
       "search": {
         "list": []
       },
       "conversation": {
         "list": []
       },
       "game": {
         "list": []
       },
       "plugin": {
         "list": []
       },
       "gamePlugin": {
         "list": []
       },
       "miniprogram": {
         "list": []
       }
     }
   }
   ```

### 5.3 预览和真机调试
1. 在微信开发者工具中点击「预览」按钮
2. 使用微信扫描生成的二维码，在手机上预览小程序
3. 点击「真机调试」按钮，可在手机上进行实时调试
4. 注意事项：
   - 确保手机和开发机器处于同一网络环境
   - 如果使用本地后端服务，需要配置微信开发者工具的「不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书」选项
   - 生产环境需要配置合法域名

## 6 首次启动后初始化建议

### 6.1 管理员账号登录
1. 打开浏览器，访问管理后台地址：`http://localhost:80`
2. 使用默认账号登录：
   - 用户名：admin
   - 密码：123456
3. 登录后建议修改密码

### 6.2 基础数据录入

#### 6.2.1 录入学校信息
1. 登录管理后台
2. 进入「系统管理」→「学校管理」页面
3. 点击「新增」按钮，填写学校信息：
   - 学校名称
   - 学校简称
   - 城市
   - 地址
   - 状态设置为「正常」
4. 点击「保存」按钮

#### 6.2.2 录入校区信息
1. 进入「系统管理」→「校区管理」页面
2. 点击「新增」按钮，填写校区信息：
   - 所属学校
   - 校区名称
   - 地址
   - 经纬度（可选）
   - 状态设置为「正常」
3. 点击「保存」按钮

#### 6.2.3 录入校门信息
1. 进入「系统管理」→「校门管理」页面
2. 点击「新增」按钮，填写校门信息：
   - 所属校区
   - 校门名称
   - 经纬度
   - 排序
   - 状态设置为「正常」
3. 点击「保存」按钮

#### 6.2.4 录入地点信息
1. 进入「系统管理」→「地点管理」页面
2. 点击「新增」按钮，填写地点信息：
   - 所属校区
   - 地点名称
   - 地点类型
   - 经纬度
   - 状态设置为「正常」
3. 点击「保存」按钮

### 6.3 开通小程序用户的认证流程

#### 6.3.1 配置认证审核流程
1. 进入「系统管理」→「系统配置」页面
2. 配置实名认证和学校认证的审核流程
3. 设置审核人员权限

#### 6.3.2 测试认证流程
1. 使用微信开发者工具打开小程序
2. 点击「我的」→「实名认证」，提交认证申请
3. 登录管理后台，进入「用户管理」→「实名认证审核」页面
4. 审核刚才提交的认证申请
5. 返回小程序，检查认证状态是否更新
6. 同样测试学校认证流程

## 7 常见问题及解决方案

### 7.1 数据库连接失败
- **问题**：启动后端服务时，出现数据库连接失败错误
- **解决方案**：
  1. 检查 MySQL 服务是否正常运行
  2. 检查 `application.yml` 中的数据库连接信息是否正确
  3. 检查数据库用户是否有足够的权限
  4. 检查防火墙是否允许连接 MySQL 端口

### 7.2 小程序无法访问后端 API
- **问题**：小程序调用后端 API 时出现网络错误
- **解决方案**：
  1. 检查后端服务是否正常运行
  2. 检查小程序 `config.js` 中的 BASE_URL 是否正确
  3. 检查微信开发者工具是否勾选了「不校验合法域名」选项
  4. 检查后端服务是否配置了 CORS 允许跨域访问

### 7.3 管理后台登录失败
- **问题**：使用默认账号登录管理后台时失败
- **解决方案**：
  1. 检查后端服务是否正常运行
  2. 检查数据库中 `sys_user` 表是否存在 admin 用户
  3. 尝试重置 admin 用户密码：
     ```sql
     UPDATE sys_user SET password = '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2' WHERE user_name = 'admin';
     ```
  4. 密码将重置为 `admin123`

### 7.4 Maven 打包失败
- **问题**：执行 `mvn clean package` 时失败
- **解决方案**：
  1. 检查 JDK 版本是否符合要求
  2. 检查 Maven 版本是否符合要求
  3. 检查网络连接是否正常，是否能下载依赖
  4. 尝试删除 `~/.m2/repository` 目录，重新下载依赖

## 8 监控与维护

### 8.1 日志监控
1. 后端日志位于 `ruoyi-admin/logs` 目录
2. 可以使用日志分析工具（如 ELK Stack）收集和分析日志
3. 定期检查日志，发现并解决异常问题

### 8.2 数据库维护
1. 定期备份数据库
2. 定期优化数据库表结构和索引
3. 定期清理过期数据
4. 监控数据库性能，及时调整配置

### 8.3 系统更新
1. 定期更新依赖包，修复安全漏洞
2. 定期更新框架版本
3. 定期发布新功能和 bug 修复

## 9 总结

本部署说明文档详细介绍了校园拼车系统的部署步骤，包括数据库部署、后端部署、管理端前端部署和小程序前端部署。通过按照文档步骤操作，可以顺利搭建起完整的系统环境。

首次启动后，建议按照文档中的初始化建议，录入必要的基础数据，并测试核心功能流程，确保系统正常运行。

在系统运行过程中，需要定期进行监控和维护，确保系统的稳定性和安全性。