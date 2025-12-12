# 校园拼车系统数据库设计文档

## 1 概述

### 1.1 数据库整体划分
校园拼车系统的数据库分为以下几类表：

| 表类型 | 前缀 | 说明 | 示例表名 |
|-------|-----|------|---------|
| 业务表 | ccp_ | 系统核心业务表 | ccp_trip、ccp_mini_user、ccp_school等 |
| 系统表 | sys_ | 若依框架自带系统表 | sys_user、sys_role、sys_menu等 |
| 定时任务表 | qrtz_ | Quartz定时任务相关表 | qrtz_job_details、qrtz_cron_triggers等 |
| 代码生成表 | gen_ | 若依代码生成器相关表 | gen_table、gen_table_column等 |

### 1.2 数据库基本信息
- **数据库类型**：MySQL 8.0
- **字符集**：utf8mb4
- **排序规则**：utf8mb4_0900_ai_ci
- **主键策略**：自增主键（AUTO_INCREMENT）
- **存储引擎**：InnoDB
- **大致数据量预期**：
  - 初期：1000-5000用户，100-500日活
  - 中期：10000-50000用户，1000-5000日活
  - 长期：100000+用户，10000+日活

## 2 业务表分组说明

### 2.1 基础地理与学校信息

#### 2.1.1 ccp_city - 城市信息表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 城市ID | 是 | 自增主键 |
| city_name | varchar(100) | 城市名称 | 是 | 唯一 |
| province_name | varchar(100) | 省份名称 | 否 | - |
| city_code | varchar(50) | 城市编码 | 否 | - |
| status | tinyint | 状态 | 否 | 1正常 0停用，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_city_name（city_name）
**关系**：
- 一对多：一个城市包含多个学校

#### 2.1.2 ccp_school - 学校表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 学校ID | 是 | 自增主键 |
| city_id | bigint | 城市ID | 否 | 关联ccp_city.id |
| school_name | varchar(100) | 学校名称 | 是 | 唯一 |
| school_short_name | varchar(50) | 学校简称 | 否 | - |
| logo_url | varchar(255) | 学校Logo | 否 | - |
| address | varchar(255) | 学校总地址 | 否 | - |
| status | tinyint | 状态 | 否 | 1正常 0停用，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_school_name（school_name）、idx_city_id（city_id）
**关系**：
- 多对一：多个学校属于一个城市
- 一对多：一个学校包含多个校区

#### 2.1.3 ccp_school_campus - 学校校区表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 校区ID | 是 | 自增主键 |
| school_id | bigint | 所属学校ID | 是 | 关联ccp_school.id |
| campus_name | varchar(100) | 校区名称 | 是 | 唯一 |
| address | varchar(255) | 校区地址 | 否 | - |
| latitude | decimal(10,6) | 纬度 | 否 | - |
| longitude | decimal(10,6) | 经度 | 否 | - |
| manager_user_id | bigint | 校区管理员 | 否 | 关联sys_user.user_id |
| status | tinyint | 状态 | 否 | 1正常 0停用，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_school_id（school_id）、idx_campus_name（campus_name）、idx_manager_user_id（manager_user_id）
**关系**：
- 多对一：多个校区属于一个学校
- 一对多：一个校区包含多个校门和地点

#### 2.1.4 ccp_school_gate - 学校校门表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 校门ID | 是 | 自增主键 |
| campus_id | bigint | 校区ID | 是 | 关联ccp_school_campus.id |
| gate_name | varchar(100) | 校门名称 | 是 | - |
| latitude | decimal(10,6) | 纬度 | 是 | - |
| longitude | decimal(10,6) | 经度 | 是 | - |
| sort | int | 排序优先级 | 否 | 越小越前，默认0 |
| status | tinyint | 状态 | 否 | 1正常 0停用，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_campus_id（campus_id）、idx_gate_name（gate_name）
**关系**：
- 多对一：多个校门属于一个校区

### 2.2 地点与路线

#### 2.2.1 ccp_location - 地点体系表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 地点ID | 是 | 自增主键 |
| campus_id | bigint | 所属校区ID | 是 | 关联ccp_school_campus.id |
| location_name | varchar(100) | 地点名称 | 是 | - |
| location_type | tinyint | 地点类型 | 是 | 1地铁站 2商超 3公交站 4公寓 5其他 |
| latitude | decimal(10,6) | 纬度 | 是 | - |
| longitude | decimal(10,6) | 经度 | 是 | - |
| cover_image_url | varchar(255) | 封面图片URL | 否 | - |
| description_text | text | 富文本描述 | 否 | - |
| status | tinyint | 状态 | 否 | 1正常 0停用，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_campus_id（campus_id）、idx_location_type（location_type）、idx_location_name（location_name）
**关系**：
- 多对一：多个地点属于一个校区

#### 2.2.2 ccp_location_stats - 地点热度统计表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | ID | 是 | 自增主键 |
| location_id | bigint | 地点ID | 是 | 关联ccp_location.id，唯一 |
| campus_id | bigint | 所属校区ID | 是 | 关联ccp_school_campus.id |
| used_as_start_count | int | 作为起点使用次数 | 是 | 默认0 |
| used_as_end_count | int | 作为终点使用次数 | 是 | 默认0 |
| last_used_time | datetime | 最近使用时间 | 否 | - |

**主键**：id
**索引**：uk_location（location_id，唯一）、idx_campus（campus_id）
**关系**：
- 一对一：一个地点对应一条热度统计记录

#### 2.2.3 ccp_route_preset - 校区公共常见线路模板

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 常见路线ID | 是 | 自增主键 |
| campus_id | bigint | 所属校区ID | 是 | 关联ccp_school_campus.id |
| name | varchar(100) | 线路名称 | 是 | - |
| start_gate_id | bigint | 起点校门ID | 否 | 关联ccp_school_gate.id |
| start_location_id | bigint | 起点地点ID | 否 | 关联ccp_location.id |
| start_address | varchar(255) | 起点文字描述 | 否 | - |
| end_gate_id | bigint | 终点校门ID | 否 | 关联ccp_school_gate.id |
| end_location_id | bigint | 终点地点ID | 否 | 关联ccp_location.id |
| end_address | varchar(255) | 终点文字描述 | 否 | - |
| require_text | varchar(500) | 对拼车人的要求模板 | 否 | - |
| default_people | int | 默认人数 | 否 | - |
| status | tinyint | 状态 | 否 | 1启用 0停用，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_campus_id（campus_id）、idx_route_name（name）
**关系**：
- 多对一：多个路线模板属于一个校区

#### 2.2.4 ccp_route_stats - 路线热度统计表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | ID | 是 | 自增主键 |
| campus_id | bigint | 校区ID | 是 | 关联ccp_school_campus.id |
| start_location_id | bigint | 起点地点ID | 否 | 关联ccp_location.id |
| end_location_id | bigint | 终点地点ID | 否 | 关联ccp_location.id |
| start_gate_id | bigint | 起点校门ID | 否 | 关联ccp_school_gate.id |
| end_gate_id | bigint | 终点校门ID | 否 | 关联ccp_school_gate.id |
| use_count | int | 使用次数 | 是 | 默认0 |
| last_used_time | datetime | 最近使用时间 | 否 | - |

**主键**：id
**索引**：idx_campus（campus_id）
**关系**：
- 多对一：多个路线统计属于一个校区

#### 2.2.5 ccp_user_route - 用户个人常用路线模板

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 路线模板ID | 是 | 自增主键 |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id |
| campus_id | bigint | 所属校区ID | 是 | 关联ccp_school_campus.id |
| name | varchar(100) | 自定义名称 | 是 | - |
| start_gate_id | bigint | 起点校门ID | 否 | 关联ccp_school_gate.id |
| start_location_id | bigint | 起点地点ID | 否 | 关联ccp_location.id |
| start_address | varchar(255) | 起点文字描述 | 否 | - |
| end_gate_id | bigint | 终点校门ID | 否 | 关联ccp_school_gate.id |
| end_location_id | bigint | 终点地点ID | 否 | 关联ccp_location.id |
| end_address | varchar(255) | 终点文字描述 | 否 | - |
| require_text | varchar(500) | 对拼车人的要求模板 | 否 | - |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_user（user_id）、idx_campus（campus_id）
**关系**：
- 多对一：多个路线模板属于一个用户和一个校区

### 2.3 用户与认证

#### 2.3.1 ccp_mini_user - 小程序用户表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| open_id | varchar(64) | 微信openId | 是 | 唯一 |
| union_id | varchar(64) | 微信unionId | 否 | - |
| nick_name | varchar(100) | 昵称 | 否 | - |
| avatar_url | varchar(255) | 头像 | 否 | - |
| phone | varchar(20) | 手机号 | 否 | - |
| real_name | varchar(50) | 真实姓名 | 否 | - |
| id_card_name | varchar(50) | 身份证姓名 | 否 | - |
| id_card_number | varchar(32) | 身份证号 | 否 | - |
| face_image_url | varchar(255) | 人脸图片地址 | 否 | - |
| face_verify_result | varchar(255) | 人脸比对结果 | 否 | - |
| gender | tinyint | 性别 | 否 | - |
| status | tinyint | 状态 | 否 | 1正常，默认1 |
| real_auth_status | tinyint | 实名认证状态 | 否 | 0未认证 1待审核 2已认证 3不通过，默认0 |
| real_auth_fail_reason | varchar(255) | 实名认证失败原因 | 否 | - |
| real_auth_review_by | bigint | 实名认证审核人 | 否 | 关联sys_user.user_id |
| real_auth_review_time | datetime | 实名认证审核时间 | 否 | - |
| admin_remark | varchar(255) | 管理员备注 | 否 | - |
| last_active_time | datetime | 最近活跃时间 | 否 | - |
| online_status | tinyint | 在线状态 | 否 | 0离线 1在线，默认0 |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_open_id（open_id，唯一）、idx_phone（phone）、idx_online_status（online_status）
**关系**：
- 一对多：一个用户可以有多个学校认证、多个拼车订单、多个聊天消息等

#### 2.3.2 ccp_user_school_auth - 用户-学校学生认证表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| user_id | bigint | 小程序用户ID | 是 | 关联ccp_mini_user.id |
| school_id | bigint | 学校ID | 是 | 关联ccp_school.id |
| campus_id | bigint | 主关联校区ID | 否 | 关联ccp_school_campus.id |
| student_no | varchar(50) | 学号 | 是 | - |
| student_name | varchar(50) | 学生证姓名 | 否 | - |
| student_card_image_url | varchar(255) | 学生证照片URL | 否 | - |
| extra_image_url | varchar(255) | 补充照片URL | 否 | - |
| status | tinyint | 认证状态 | 否 | 0未创建 1待审核 2已通过 3不通过 4已过期/失效，默认1 |
| fail_reason | varchar(255) | 审核不通过原因 | 否 | - |
| submit_time | datetime | 提交时间 | 否 | 默认CURRENT_TIMESTAMP |
| review_time | datetime | 审核时间 | 否 | - |
| review_by | bigint | 审核人ID | 否 | 关联sys_user.user_id |
| expire_time | datetime | 认证过期时间 | 否 | - |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_user_school（user_id, school_id，唯一）、idx_user_id（user_id）、idx_school_id（school_id）、idx_status（status）、idx_campus_id（campus_id）
**关系**：
- 多对一：多个学校认证属于一个用户和一个学校

#### 2.3.3 ccp_user_emergency_contact - 用户紧急联系人

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id |
| contact_name | varchar(50) | 联系人姓名 | 是 | - |
| contact_phone | varchar(20) | 联系人电话 | 是 | - |
| relation | varchar(50) | 关系 | 否 | - |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_user_id（user_id）
**关系**：
- 多对一：多个紧急联系人属于一个用户

#### 2.3.4 ccp_user_blacklist - 用户黑名单表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| user_id | bigint | 被封禁用户ID | 是 | 关联ccp_mini_user.id |
| reason | varchar(255) | 封禁原因 | 否 | - |
| start_time | datetime | 封禁开始时间 | 否 | 默认CURRENT_TIMESTAMP |
| end_time | datetime | 封禁结束时间 | 否 | NULL=永久 |
| status | tinyint | 状态 | 否 | 1生效 0失效，默认1 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_user_id（user_id）、idx_status（status）
**关系**：
- 多对一：多个黑名单记录属于一个用户

#### 2.3.5 ccp_user_reputation - 用户信誉统计表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id，唯一 |
| total_reviews | int | 累计评价次数 | 否 | 默认0 |
| avg_rating | decimal(3,2) | 平均评分 | 否 | 1-5，默认0.00 |
| total_no_show | int | 爽约次数 | 否 | 默认0 |
| good_rate | decimal(5,2) | 好评率 % | 否 | 默认0.00 |
| last_review_time | datetime | 最后评价时间 | 否 | - |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_user（user_id，唯一）、idx_no_show（total_no_show）
**关系**：
- 一对一：一个用户对应一条信誉统计记录

#### 2.3.6 ccp_user_tag_stats - 用户标签统计表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id |
| tag | varchar(50) | 标签内容 | 是 | - |
| tag_count | int | 被贴标签次数 | 否 | 默认0 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_user_tag（user_id, tag，唯一）、idx_user（user_id）、idx_tag（tag）
**关系**：
- 多对一：多个标签统计属于一个用户

### 2.4 拼车行程

#### 2.4.1 ccp_trip - 拼车主表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 拼车ID | 是 | 自增主键 |
| school_id | bigint | 学校ID | 是 | 关联ccp_school.id |
| campus_id | bigint | 校区ID | 是 | 关联ccp_school_campus.id |
| owner_user_id | bigint | 发起人用户ID | 是 | 关联ccp_mini_user.id |
| route_preset_id | bigint | 引用的公共常见路线ID | 否 | 关联ccp_route_preset.id |
| user_route_id | bigint | 引用的用户常用路线ID | 否 | 关联ccp_user_route.id |
| start_gate_id | bigint | 起点校门 | 否 | 关联ccp_school_gate.id |
| start_location_id | bigint | 起点地点ID | 否 | 关联ccp_location.id |
| start_address | varchar(255) | 起点文字描述 | 是 | - |
| end_gate_id | bigint | 终点校门 | 否 | 关联ccp_school_gate.id |
| end_location_id | bigint | 终点地点ID | 否 | 关联ccp_location.id |
| end_address | varchar(255) | 终点文字描述 | 是 | - |
| total_people | int | 总拼车人数 | 是 | - |
| owner_people_count | int | 发起方人数 | 是 | - |
| current_people | int | 当前加入人数 | 否 | 默认0 |
| require_text | varchar(500) | 对拼车人的要求文本 | 否 | - |
| departure_time | datetime | 计划出发时间 | 是 | - |
| actual_departure_time | datetime | 实际出发时间 | 否 | - |
| expire_time | datetime | 自动过期时间 | 否 | - |
| status | tinyint | 状态 | 否 | 0招募中 1待确认 2已确认 3已完成 4已取消 5已过期，默认0 |
| confirm_mode | tinyint | 确认模式 | 否 | 0不需确认 1满人后确认 2手动发起确认，默认1 |
| confirm_start_time | datetime | 开始确认时间 | 否 | - |
| confirmed_time | datetime | 全员确认成团时间 | 否 | - |
| cancel_type | tinyint | 取消类型 | 否 | 0未取消 1发起人取消 2成员取消 3系统超时，默认0 |
| cancel_reason | varchar(255) | 取消原因 | 否 | - |
| cancel_by_user_id | bigint | 取消操作人 | 否 | 关联ccp_mini_user.id |
| share_code | varchar(64) | 分享码 | 否 | - |
| share_count | int | 分享次数 | 否 | 默认0 |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_trip_campus（campus_id）、idx_trip_owner（owner_user_id）、idx_trip_status（status）、idx_trip_departure（departure_time）、idx_trip_expire（expire_time）
**关系**：
- 多对一：多个拼车订单属于一个学校、一个校区和一个发起人
- 一对多：一个拼车订单包含多个成员、多条聊天消息和多条操作日志

#### 2.4.2 ccp_trip_member - 拼车成员表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键ID | 是 | 自增主键 |
| trip_id | bigint | 拼车ID | 是 | 关联ccp_trip.id |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id |
| role | tinyint | 角色 | 否 | 1发起者 2成员，默认2 |
| join_people_count | int | 加入人数 | 否 | 默认1 |
| status | tinyint | 状态 | 否 | 1已加入 2已退出 3被踢 4已完成 5爽约，默认1 |
| confirm_flag | tinyint | 是否确认拼车 | 否 | 0未确认 1已确认，默认0 |
| ready_flag | tinyint | 是否已准备就绪 | 否 | 0否 1是，默认0 |
| join_time | datetime | 加入时间 | 否 | 默认CURRENT_TIMESTAMP |
| quit_time | datetime | 退出/完成时间 | 否 | - |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_trip_user（trip_id, user_id，唯一）、idx_member_user（user_id）、idx_member_trip（trip_id）、idx_member_status（status）
**关系**：
- 多对一：多个成员属于一个拼车订单和一个用户

#### 2.4.3 ccp_trip_log - 拼车操作日志表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| trip_id | bigint | 拼车ID | 是 | 关联ccp_trip.id |
| operator_user_id | bigint | 操作人用户ID | 否 | 关联ccp_mini_user.id |
| action_type | varchar(50) | 操作类型 | 是 | create/join/quit/kick/confirm/update_people/cancel/expire等 |
| action_desc | varchar(255) | 操作描述 | 否 | - |
| create_time | datetime | 操作时间 | 否 | 默认CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_trip（trip_id, create_time）
**关系**：
- 多对一：多条操作日志属于一个拼车订单

#### 2.4.4 ccp_trip_merge_request - 拼车合并请求表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| from_trip_id | bigint | 发起合并的拼车ID | 是 | 关联ccp_trip.id |
| to_trip_id | bigint | 目标拼车ID | 是 | 关联ccp_trip.id |
| requester_user_id | bigint | 发起人用户ID | 是 | 关联ccp_mini_user.id |
| status | tinyint | 状态 | 否 | 0待处理 1已同意 2已拒绝 3已过期，默认0 |
| reason | varchar(255) | 备注/原因 | 否 | - |
| create_time | datetime | 请求时间 | 否 | 默认CURRENT_TIMESTAMP |
| decision_time | datetime | 处理时间 | 否 | - |

**主键**：id
**索引**：idx_from_trip（from_trip_id）、idx_to_trip（to_trip_id）、idx_status（status）
**关系**：
- 多对一：多个合并请求属于一个发起者

### 2.5 聊天与消息

#### 2.5.1 ccp_trip_chat - 拼车聊天消息表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 聊天消息ID | 是 | 自增主键 |
| trip_id | bigint | 拼车ID | 是 | 关联ccp_trip.id |
| sender_user_id | bigint | 发送用户ID | 是 | 关联ccp_mini_user.id |
| content_type | tinyint | 内容类型 | 否 | 1文本 2系统消息，默认1 |
| message_type | tinyint | 系统消息子类型 | 否 | 0普通 1加入 2退出 3确认 4修改人数，默认0 |
| content | text | 内容 | 是 | - |
| is_deleted | tinyint | 是否删除 | 否 | 0否 1是，默认0 |
| send_time | datetime | 发送时间 | 否 | 默认CURRENT_TIMESTAMP |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：idx_chat_trip（trip_id, send_time）、idx_chat_sender（sender_user_id）
**关系**：
- 多对一：多条聊天消息属于一个拼车订单和一个发送者
- 一对多：一条聊天消息可以被多个用户标记为已读

#### 2.5.2 ccp_trip_chat_read - 拼车聊天消息已读表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| chat_id | bigint | 消息ID | 是 | 关联ccp_trip_chat.id |
| trip_id | bigint | 拼车ID | 是 | 关联ccp_trip.id |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id |
| read_time | datetime | 阅读时间 | 否 | 默认CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_chat_user（chat_id, user_id，唯一）、idx_trip_user（trip_id, user_id）
**关系**：
- 多对一：多条已读记录属于一个拼车订单、一条聊天消息和一个用户

### 2.6 评价与互评

#### 2.6.1 ccp_trip_review - 拼车互评表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 评价ID | 是 | 自增主键 |
| trip_id | bigint | 拼车ID | 是 | 关联ccp_trip.id |
| reviewer_user_id | bigint | 评价人 | 是 | 关联ccp_mini_user.id |
| target_user_id | bigint | 被评价人 | 是 | 关联ccp_mini_user.id |
| rating | tinyint | 评分 | 是 | 1-5 |
| is_no_show | tinyint | 是否爽约 | 否 | 0否 1是，默认0 |
| tags | varchar(100) | 标签 | 否 | 逗号分隔 |
| comment | varchar(500) | 文字评价 | 否 | - |
| remark | varchar(255) | 备注 | 否 | - |
| create_by | varchar(64) | 创建者 | 否 | - |
| create_time | datetime | 创建时间 | 否 | 默认CURRENT_TIMESTAMP |
| update_by | varchar(64) | 更新者 | 否 | - |
| update_time | datetime | 更新时间 | 否 | 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

**主键**：id
**索引**：uk_review_once（trip_id, reviewer_user_id, target_user_id，唯一）、idx_review_trip（trip_id）、idx_review_target（target_user_id）、idx_review_reviewer（reviewer_user_id）
**关系**：
- 多对一：多条评价属于一个拼车订单、一个评价人和一个被评价人

### 2.7 行为记录与风控

#### 2.7.1 ccp_user_action_log - 用户动作记录表

| 字段名 | 数据类型 | 含义 | 必填 | 取值范围/约束 |
|-------|---------|-----|-----|-------------|
| id | bigint | 主键 | 是 | 自增主键 |
| user_id | bigint | 用户ID | 是 | 关联ccp_mini_user.id |
| action_type | varchar(50) | 动作类型 | 是 | publish_trip/join_trip/chat等 |
| action_time | datetime | 动作时间 | 否 | 默认CURRENT_TIMESTAMP |
| detail | varchar(255) | 详情 | 否 | - |

**主键**：id
**索引**：idx_user_action（user_id, action_type, action_time）
**关系**：
- 多对一：多条动作记录属于一个用户

## 3 典型业务场景下的表协同说明

### 3.1 发布订单涉及的表

1. **ccp_trip**：保存拼车订单的基本信息
2. **ccp_trip_member**：保存发起者作为成员的记录
3. **ccp_trip_log**：记录订单创建的操作日志
4. **ccp_route_stats**：更新路线使用次数统计
5. **ccp_location_stats**：更新起点和终点的使用次数统计（如果使用了地点）
6. **ccp_user_action_log**：记录用户发布订单的动作

### 3.2 加入订单、确认、踢人涉及的表

#### 3.2.1 加入订单
1. **ccp_trip_member**：添加新成员记录
2. **ccp_trip**：更新current_people字段
3. **ccp_trip_chat**：生成系统消息（用户加入）
4. **ccp_trip_log**：记录加入操作日志
5. **ccp_user_action_log**：记录用户加入订单的动作

#### 3.2.2 确认订单
1. **ccp_trip_member**：更新confirm_flag字段
2. **ccp_trip**：如果所有成员都已确认，更新status为已确认
3. **ccp_trip_chat**：生成系统消息（用户确认）
4. **ccp_trip_log**：记录确认操作日志

#### 3.2.3 踢人
1. **ccp_trip_member**：更新被踢用户的status为被踢
2. **ccp_trip**：更新current_people字段
3. **ccp_trip_chat**：生成系统消息（用户被踢）
4. **ccp_trip_log**：记录踢人操作日志

### 3.3 实名和学校认证涉及的表

#### 3.3.1 实名认证
1. **ccp_mini_user**：更新real_name、id_card_number、real_auth_status等字段
2. **ccp_user_action_log**：记录用户提交实名认证的动作

#### 3.3.2 学校认证
1. **ccp_user_school_auth**：保存学校认证记录
2. **ccp_mini_user**：更新学校认证相关状态（如果需要）
3. **ccp_user_action_log**：记录用户提交学校认证的动作

### 3.4 聊天消息与已读统计涉及的表

#### 3.4.1 发送消息
1. **ccp_trip_chat**：保存聊天消息
2. **ccp_user_action_log**：记录用户发送消息的动作

#### 3.4.2 查看消息
1. **ccp_trip_chat_read**：标记消息为已读
2. **ccp_trip_chat**：查询聊天消息列表

#### 3.4.3 未读消息统计
1. **ccp_trip_chat**：查询所有消息
2. **ccp_trip_chat_read**：查询已读消息
3. 计算未读消息数

## 4 数据库优化建议

### 4.1 索引优化
1. 为频繁查询的字段创建索引，如ccp_trip表的departure_time、status、campus_id字段
2. 为关联查询的外键字段创建索引，如ccp_trip_member表的trip_id、user_id字段
3. 避免在索引字段上使用函数操作，影响索引效率
4. 定期分析索引使用情况，删除无效索引

### 4.2 查询优化
1. 使用分页查询，避免一次性查询大量数据
2. 合理使用连接查询，避免过多表连接
3. 避免在查询条件中使用OR，考虑使用UNION替代
4. 使用EXPLAIN分析查询计划，优化慢查询

### 4.3 分表分库策略
1. 对于大表如ccp_trip_chat，考虑按trip_id进行分表
2. 对于ccp_mini_user，考虑按school_id进行分库
3. 对于历史数据，考虑归档处理

### 4.4 缓存策略
1. 使用Redis缓存热点数据，如学校、校区、校门信息
2. 缓存拼车订单列表，提高查询效率
3. 缓存用户认证状态，减少数据库查询
4. 合理设置缓存过期时间，避免数据不一致

### 4.5 事务优化
1. 减少事务范围，避免长事务
2. 合理使用索引，提高事务处理效率
3. 避免在事务中执行耗时操作
4. 考虑使用异步处理，减少事务持有的时间

## 5 总结

本数据库设计文档详细描述了校园拼车系统的数据库结构，包括表设计、字段含义、索引设计和表关系。数据库采用了模块化设计，将业务表分为基础地理与学校信息、地点与路线、用户与认证、拼车行程、聊天与消息、评价与互评以及行为记录与风控等模块。

系统设计支持多学校、多校区、多地点的管理，实现了完整的拼车流程，包括发布、加入、确认、聊天、评价等功能。通过合理的索引设计和表关系，确保了系统的查询效率和扩展性。

未来可以根据业务需求和数据量增长情况，考虑进一步优化数据库设计，如分表分库、引入缓存等，以提高系统的性能和可靠性。