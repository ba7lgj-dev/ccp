# 校园拼车系统详细设计文档

## 1 模块划分与接口设计

### 1.1 小程序后端接口设计

#### 1.1.1 MpAuthController - 登录认证接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 请求示例 | 返回结构 | 业务校验要点 |
|---------|-----|---------|---------|---------|---------|-------------|
| 微信登录 | /mp/auth/login | POST | code: string（微信登录code） | `{"code": "123456"}` | `{"code": 200, "msg": "成功", "data": {"token": "xxx", "user": {...}}}` | 1. 校验code是否为空<br>2. 调用微信接口获取openid<br>3. 生成JWT令牌 |
| 获取用户信息 | /mp/user/info | GET | token: string（请求头） | N/A | `{"code": 200, "msg": "成功", "data": {"id": 1, "nickName": "xxx", "avatarUrl": "xxx", "realAuthStatus": 2, "schoolAuthStatus": 2}}` | 1. 校验token有效性<br>2. 查询用户信息 |
| 更新用户信息 | /mp/user/update | POST | token: string（请求头）<br>nickName: string<br>avatarUrl: string<br>gender: number | `{"nickName": "新昵称", "gender": 1}` | `{"code": 200, "msg": "成功", "data": null}` | 1. 校验token有效性<br>2. 更新用户信息 |

#### 1.1.2 MpRealAuthController - 实名认证接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 请求示例 | 返回结构 | 业务校验要点 |
|---------|-----|---------|---------|---------|---------|-------------|
| 提交实名认证 | /mp/real-auth/submit | POST | token: string（请求头）<br>realName: string<br>idCardNumber: string<br>faceImageUrl: string | `{"realName": "张三", "idCardNumber": "110101199001011234", "faceImageUrl": "xxx"}` | `{"code": 200, "msg": "提交成功，等待审核", "data": null}` | 1. 校验token有效性<br>2. 校验姓名和身份证号格式<br>3. 校验是否已提交认证<br>4. 保存认证信息，状态设为待审核 |
| 获取实名认证信息 | /mp/real-auth/info | GET | token: string（请求头） | N/A | `{"code": 200, "msg": "成功", "data": {"realName": "张三", "idCardNumber": "110101********1234", "status": 2, "failReason": null}}` | 1. 校验token有效性<br>2. 查询用户实名认证信息<br>3. 身份证号脱敏处理 |

#### 1.1.3 MpSchoolAuthController - 学校认证接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 请求示例 | 返回结构 | 业务校验要点 |
|---------|-----|---------|---------|---------|---------|-------------|
| 提交学校认证 | /mp/school-auth/submit | POST | token: string（请求头）<br>schoolId: number<br>campusId: number<br>studentNo: string<br>studentName: string<br>studentCardImageUrl: string | `{"schoolId": 1, "campusId": 1, "studentNo": "20200001", "studentName": "张三", "studentCardImageUrl": "xxx"}` | `{"code": 200, "msg": "提交成功，等待审核", "data": null}` | 1. 校验token有效性<br>2. 校验学号和姓名格式<br>3. 校验学校和校区是否存在<br>4. 保存认证信息，状态设为待审核 |
| 获取我的学校认证 | /mp/school-auth/my | GET | token: string（请求头） | N/A | `{"code": 200, "msg": "成功", "data": [{"schoolId": 1, "schoolName": "XX大学", "status": 2, "studentNo": "20200001"}]}` | 1. 校验token有效性<br>2. 查询用户所有学校认证信息 |
| 获取已通过学校认证列表 | /mp/school-auth/passed | GET | token: string（请求头） | N/A | `{"code": 200, "msg": "成功", "data": [{"schoolId": 1, "schoolName": "XX大学", "campusId": 1, "campusName": "主校区"}]}` | 1. 校验token有效性<br>2. 查询用户已通过的学校认证 |

#### 1.1.4 MpTripController - 拼车行程接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 请求示例 | 返回结构 | 业务校验要点 |
|---------|-----|---------|---------|---------|---------|-------------|
| 发布行程 | /mp/trip/publish | POST | token: string（请求头）<br>schoolId: number<br>campusId: number<br>startGateId: number<br>startLocationId: number<br>startAddress: string<br>endGateId: number<br>endLocationId: number<br>endAddress: string<br>totalPeople: number<br>ownerPeopleCount: number<br>departureTime: string<br>requireText: string | `{"schoolId": 1, "campusId": 1, "startAddress": "南门", "endAddress": "地铁站", "totalPeople": 4, "ownerPeopleCount": 1, "departureTime": "2023-12-01 14:00", "requireText": "准时到达"}` | `{"code": 200, "msg": "发布成功", "data": {"tripId": 1}}` | 1. 校验token有效性<br>2. 校验实名认证和学校认证是否通过<br>3. 校验出发时间是否合理<br>4. 校验人数限制<br>5. 生成拼车订单，状态设为招募中 |
| 获取行程列表 | /mp/trip/list | GET | token: string（请求头）<br>campusId: number<br>startAddress: string<br>endAddress: string<br>departureTime: string<br>status: number<br>page: number<br>limit: number | N/A | `{"code": 200, "msg": "成功", "data": {"list": [...], "total": 10}}` | 1. 校验token有效性<br>2. 分页查询行程列表<br>3. 根据筛选条件过滤 |
| 获取行程详情 | /mp/trip/detail | GET | token: string（请求头）<br>tripId: number | N/A | `{"code": 200, "msg": "成功", "data": {"trip": {...}, "members": [...], "unreadCount": 0}}` | 1. 校验token有效性<br>2. 查询行程详情和成员列表<br>3. 统计未读消息数 |
| 加入行程 | /mp/trip/join | POST | token: string（请求头）<br>tripId: number<br>joinPeopleCount: number | `{"tripId": 1, "joinPeopleCount": 1}` | `{"code": 200, "msg": "加入成功", "data": null}` | 1. 校验token有效性<br>2. 校验实名认证和学校认证是否通过<br>3. 校验行程是否存在且状态为招募中<br>4. 校验人数是否已满<br>5. 添加用户到成员列表 |
| 退出行程 | /mp/trip/quit | POST | token: string（请求头）<br>tripId: number | `{"tripId": 1}` | `{"code": 200, "msg": "退出成功", "data": null}` | 1. 校验token有效性<br>2. 校验用户是否为行程成员<br>3. 校验行程状态是否允许退出<br>4. 更新成员状态为已退出 |
| 确认拼车 | /mp/trip/confirm | POST | token: string（请求头）<br>tripId: number | `{"tripId": 1}` | `{"code": 200, "msg": "确认成功", "data": null}` | 1. 校验token有效性<br>2. 校验用户是否为行程成员<br>3. 校验行程状态是否为待确认<br>4. 更新成员确认状态<br>5. 检查是否所有成员都已确认，如已确认则更新行程状态 |
| 踢人 | /mp/trip/kick | POST | token: string（请求头）<br>tripId: number<br>userId: number | `{"tripId": 1, "userId": 2}` | `{"code": 200, "msg": "踢人成功", "data": null}` | 1. 校验token有效性<br>2. 校验用户是否为行程发起者<br>3. 校验被踢用户是否为行程成员<br>4. 更新成员状态为被踢 |

#### 1.1.5 MpChatController - 聊天消息接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 请求示例 | 返回结构 | 业务校验要点 |
|---------|-----|---------|---------|---------|---------|-------------|
| 获取消息列表 | /mp/chat/list | GET | token: string（请求头）<br>tripId: number<br>lastChatId: number<br>limit: number | N/A | `{"code": 200, "msg": "成功", "data": {"list": [...], "hasMore": true}}` | 1. 校验token有效性<br>2. 校验用户是否为行程成员<br>3. 分页查询聊天消息<br>4. 标记消息为已读 |
| 发送消息 | /mp/chat/send | POST | token: string（请求头）<br>tripId: number<br>content: string<br>contentType: number | `{"tripId": 1, "content": "大家好", "contentType": 1}` | `{"code": 200, "msg": "发送成功", "data": {"chatId": 1}}` | 1. 校验token有效性<br>2. 校验用户是否为行程成员<br>3. 校验消息内容是否为空<br>4. 保存聊天消息 |
| 标记消息已读 | /mp/chat/read | POST | token: string（请求头）<br>tripId: number<br>chatId: number | `{"tripId": 1, "chatId": 1}` | `{"code": 200, "msg": "标记成功", "data": null}` | 1. 校验token有效性<br>2. 保存已读状态 |

### 1.2 管理端关键接口设计

#### 1.2.1 SchoolController - 学校管理接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 业务校验要点 |
|---------|-----|---------|---------|-------------|
| 学校列表 | /ccp/school/list | GET | 分页参数、搜索条件 | 1. 分页查询学校列表<br>2. 根据搜索条件过滤 |
| 学校详情 | /ccp/school/detail | GET | schoolId: number | 1. 查询学校详情 |
| 添加学校 | /ccp/school/add | POST | 学校信息 | 1. 校验学校名称是否已存在<br>2. 保存学校信息 |
| 更新学校 | /ccp/school/update | PUT | 学校信息 | 1. 校验学校是否存在<br>2. 更新学校信息 |
| 删除学校 | /ccp/school/delete | DELETE | schoolIds: array | 1. 校验学校是否存在<br>2. 检查是否有关联数据<br>3. 删除学校 |

#### 1.2.2 CampusController - 校区管理接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 业务校验要点 |
|---------|-----|---------|---------|-------------|
| 校区列表 | /ccp/campus/list | GET | 分页参数、schoolId、搜索条件 | 1. 分页查询校区列表<br>2. 根据学校ID和搜索条件过滤 |
| 添加校区 | /ccp/campus/add | POST | 校区信息 | 1. 校验校区名称是否已存在<br>2. 保存校区信息 |
| 更新校区 | /ccp/campus/update | PUT | 校区信息 | 1. 校验校区是否存在<br>2. 更新校区信息 |
| 删除校区 | /ccp/campus/delete | DELETE | campusIds: array | 1. 校验校区是否存在<br>2. 检查是否有关联数据<br>3. 删除校区 |

#### 1.2.3 GateController - 校门管理接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 业务校验要点 |
|---------|-----|---------|---------|-------------|
| 校门列表 | /ccp/gate/list | GET | 分页参数、campusId、搜索条件 | 1. 分页查询校门列表<br>2. 根据校区ID和搜索条件过滤 |
| 添加校门 | /ccp/gate/add | POST | 校门信息 | 1. 校验校门名称是否已存在<br>2. 保存校门信息 |
| 更新校门 | /ccp/gate/update | PUT | 校门信息 | 1. 校验校门是否存在<br>2. 更新校门信息 |
| 删除校门 | /ccp/gate/delete | DELETE | gateIds: array | 1. 校验校门是否存在<br>2. 检查是否有关联数据<br>3. 删除校门 |

#### 1.2.4 MiniUserController - 小程序用户管理接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 业务校验要点 |
|---------|-----|---------|---------|-------------|
| 用户列表 | /ccp/mini-user/list | GET | 分页参数、搜索条件、realAuthStatus、schoolAuthStatus | 1. 分页查询用户列表<br>2. 根据搜索条件和认证状态过滤 |
| 用户详情 | /ccp/mini-user/detail | GET | userId: number | 1. 查询用户详情<br>2. 查询关联的认证信息 |
| 封禁用户 | /ccp/mini-user/ban | PUT | userId: number, reason: string | 1. 校验用户是否存在<br>2. 将用户加入黑名单 |
| 解封用户 | /ccp/mini-user/unban | PUT | userId: number | 1. 校验用户是否存在<br>2. 将用户从黑名单移除 |

#### 1.2.5 UserRealAuthController - 实名认证审核接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 业务校验要点 |
|---------|-----|---------|---------|-------------|
| 认证列表 | /ccp/user/real-auth/list | GET | 分页参数、status | 1. 分页查询实名认证列表<br>2. 根据状态过滤 |
| 认证详情 | /ccp/user/real-auth/detail | GET | authId: number | 1. 查询实名认证详情 |
| 审核认证 | /ccp/user/real-auth/audit | PUT | authId: number, status: number, failReason: string | 1. 校验认证是否存在<br>2. 更新认证状态<br>3. 更新用户表中的认证状态 |

#### 1.2.6 UserSchoolAuthController - 学校认证审核接口

| 接口名称 | URL | HTTP方法 | 请求参数 | 业务校验要点 |
|---------|-----|---------|---------|-------------|
| 认证列表 | /ccp/user/school-auth/list | GET | 分页参数、status | 1. 分页查询学校认证列表<br>2. 根据状态过滤 |
| 认证详情 | /ccp/user/school-auth/detail | GET | authId: number | 1. 查询学校认证详情 |
| 审核认证 | /ccp/user/school-auth/audit | PUT | authId: number, status: number, failReason: string | 1. 校验认证是否存在<br>2. 更新认证状态<br>3. 更新用户表中的认证状态 |

## 2 关键Service和领域对象设计

### 2.1 Trip相关Service设计

#### 2.1.1 TripService - 拼车行程核心服务

**主要方法及其作用**：

| 方法名称 | 作用 | 参数 | 返回值 |
|---------|-----|------|-------|
| publishTrip | 发布拼车行程 | TripDTO | Long（tripId） |
| getTripList | 获取行程列表 | TripQueryDTO | PageResult<TripVO> |
| getTripDetail | 获取行程详情 | Long tripId, Long userId | TripDetailVO |
| joinTrip | 加入行程 | Long tripId, Long userId, Integer joinPeopleCount | void |
| quitTrip | 退出行程 | Long tripId, Long userId | void |
| confirmTrip | 确认行程 | Long tripId, Long userId | void |
| kickMember | 踢人 | Long tripId, Long ownerUserId, Long kickedUserId | void |
| updateTripStatus | 更新行程状态 | Long tripId, Integer status | void |

**内部调用顺序**：

1. **发布行程（publishTrip）**：
   - 校验用户认证状态
   - 校验出发时间和人数限制
   - 保存Trip对象到ccp_trip表
   - 保存TripMember对象到ccp_trip_member表（发起者）
   - 记录操作日志到ccp_trip_log表
   - 返回tripId

2. **加入行程（joinTrip）**：
   - 校验行程状态和人数限制
   - 保存TripMember对象到ccp_trip_member表
   - 更新ccp_trip表的current_people字段
   - 生成系统消息到ccp_trip_chat表
   - 记录操作日志到ccp_trip_log表

3. **确认行程（confirmTrip）**：
   - 更新ccp_trip_member表的confirm_flag字段
   - 检查所有成员是否都已确认
   - 如果都已确认，更新ccp_trip表的status为已确认
   - 生成系统消息到ccp_trip_chat表
   - 记录操作日志到ccp_trip_log表

**状态机规则**：

| 状态值 | 状态名称 | 说明 | 允许的状态流转 |
|-------|---------|-----|--------------|
| 0 | 招募中 | 订单刚发布，等待用户加入 | 0→1（人数已满或手动确认）<br>0→4（取消）<br>0→5（超时） |
| 1 | 待确认 | 人数已满或发起者手动确认，等待所有成员确认 | 1→2（所有成员确认）<br>1→4（取消） |
| 2 | 已确认 | 所有成员已确认，即将出发 | 2→3（行程完成）<br>2→4（取消） |
| 3 | 已完成 | 行程已结束 | 无 |
| 4 | 已取消 | 订单被取消 | 无 |
| 5 | 已过期 | 订单超时未成团 | 无 |

**confirm_mode含义**：
- 0：不需确认，加入即确认
- 1：满人后确认，人数满后所有成员需确认
- 2：手动发起确认，发起者手动触发确认流程

### 2.2 User相关Service设计

#### 2.2.1 MiniUserService - 小程序用户服务

**主要方法及其作用**：

| 方法名称 | 作用 | 参数 | 返回值 |
|---------|-----|------|-------|
| login | 用户登录 | String code | LoginResult |
| getUserInfo | 获取用户信息 | Long userId | MiniUserVO |
| updateUserInfo | 更新用户信息 | MiniUserDTO | void |
| getUserByOpenId | 根据openid获取用户 | String openId | MiniUser |
| createUser | 创建用户 | MiniUser | Long |

#### 2.2.2 UserRealAuthService - 实名认证服务

**主要方法及其作用**：

| 方法名称 | 作用 | 参数 | 返回值 |
|---------|-----|------|-------|
| submitAuth | 提交实名认证 | UserRealAuthDTO | void |
| getAuthByUserId | 根据用户ID获取认证信息 | Long userId | UserRealAuth |
| auditAuth | 审核实名认证 | Long authId, Integer status, String failReason | void |

#### 2.2.3 UserSchoolAuthService - 学校认证服务

**主要方法及其作用**：

| 方法名称 | 作用 | 参数 | 返回值 |
|---------|-----|------|-------|
| submitAuth | 提交学校认证 | UserSchoolAuthDTO | void |
| getAuthListByUserId | 根据用户ID获取学校认证列表 | Long userId | List<UserSchoolAuth> |
| getPassedAuthByUserId | 根据用户ID获取已通过的学校认证 | Long userId | List<UserSchoolAuth> |
| auditAuth | 审核学校认证 | Long authId, Integer status, String failReason | void |

### 2.3 Chat相关Service设计

#### 2.3.1 ChatService - 聊天消息服务

**主要方法及其作用**：

| 方法名称 | 作用 | 参数 | 返回值 |
|---------|-----|------|-------|
| getChatList | 获取聊天消息列表 | Long tripId, Long lastChatId, Integer limit | List<ChatVO> |
| sendChat | 发送聊天消息 | ChatDTO | Long |
| markAsRead | 标记消息为已读 | Long tripId, Long chatId, Long userId | void |
| getUnreadCount | 获取未读消息数 | Long tripId, Long userId | Integer |
| createSystemMessage | 创建系统消息 | Long tripId, Integer messageType, String content | void |

## 3 定时任务与订单状态流转设计

### 3.1 定时任务设计

系统使用若依框架的定时任务组件（基于Quartz）实现订单状态的自动流转。

**主要定时任务**：

| 任务名称 | 执行频率 | 主要功能 | 实现类 |
|---------|---------|---------|-------|
| 即将出发订单提醒 | 每5分钟 | 扫描即将出发的订单，发送提醒 | TripReminderJob |
| 超时未成团订单处理 | 每10分钟 | 扫描超时未成团的订单，设置为已过期 | TripExpireJob |
| 订单状态自动更新 | 每15分钟 | 根据出发时间和确认情况，自动更新订单状态 | TripStatusUpdateJob |

### 3.2 TripExpireJob - 超时未成团订单处理

**功能**：扫描所有状态为"招募中"且出发时间已过或超过有效期的订单，将其状态设置为"已过期"。

**执行流程**：
1. 查询所有状态为0（招募中）的订单
2. 遍历订单，检查是否超时：
   - 如果出发时间已过
   - 或者expire_time已过
3. 对于超时订单：
   - 更新ccp_trip表的status为5（已过期）
   - 更新ccp_trip表的cancel_type为3（系统超时）
   - 更新ccp_trip表的cancel_reason为"超时未成团"
   - 生成系统消息到ccp_trip_chat表
   - 记录操作日志到ccp_trip_log表

### 3.3 TripStatusUpdateJob - 订单状态自动更新

**功能**：根据订单的出发时间和成员确认情况，自动更新订单状态。

**执行流程**：
1. 查询所有状态为0（招募中）或1（待确认）的订单
2. 对于每个订单：
   - 如果状态为0（招募中）且current_people >= total_people，更新状态为1（待确认）
   - 如果状态为1（待确认），检查所有成员是否都已确认：
     - 如果都已确认，更新状态为2（已确认）
   - 如果状态为2（已确认）且出发时间已过，更新状态为3（已完成）
3. 更新ccp_trip表的status字段
4. 生成系统消息到ccp_trip_chat表
5. 记录操作日志到ccp_trip_log表

### 3.4 行程日志记录

所有订单状态变更都会记录到ccp_trip_log表，包括：
- create：创建订单
- join：加入订单
- quit：退出订单
- kick：踢人
- confirm：确认订单
- update_people：修改人数
- cancel：取消订单
- expire：订单过期
- complete：订单完成

**日志记录内容**：
- trip_id：订单ID
- operator_user_id：操作人ID
- action_type：操作类型
- action_desc：操作描述
- create_time：操作时间

## 4 数据库表结构与关系

### 4.1 核心业务表关系

```
ccp_school ──┬─── ccp_school_campus ──┬─── ccp_school_gate
             │                        └─── ccp_location
             │                        └─── ccp_route_preset
             └─── ccp_mini_user ──┬─── ccp_user_school_auth
                                   └─── ccp_trip ──┬─── ccp_trip_member
                                                   ├─── ccp_trip_chat ──┬─── ccp_trip_chat_read
                                                   ├─── ccp_trip_log
                                                   ├─── ccp_trip_merge_request
                                                   └─── ccp_trip_review
                                   └─── ccp_user_reputation
                                   └─── ccp_user_blacklist
                                   └─── ccp_user_route
```

### 4.2 关键表关联说明

| 表名 | 主要关联字段 | 关联表 | 关联说明 |
|-----|------------|-------|---------|
| ccp_school_campus | school_id | ccp_school | 校区属于学校 |
| ccp_school_gate | campus_id | ccp_school_campus | 校门属于校区 |
| ccp_location | campus_id | ccp_school_campus | 地点属于校区 |
| ccp_trip | owner_user_id | ccp_mini_user | 行程属于发起者 |
| ccp_trip | campus_id | ccp_school_campus | 行程属于校区 |
| ccp_trip_member | trip_id | ccp_trip | 成员属于行程 |
| ccp_trip_member | user_id | ccp_mini_user | 成员是用户 |
| ccp_trip_chat | trip_id | ccp_trip | 聊天消息属于行程 |
| ccp_trip_chat | sender_user_id | ccp_mini_user | 消息发送者是用户 |
| ccp_trip_chat_read | chat_id | ccp_trip_chat | 已读记录属于消息 |
| ccp_trip_chat_read | user_id | ccp_mini_user | 已读记录属于用户 |
| ccp_trip_log | trip_id | ccp_trip | 日志属于行程 |
| ccp_trip_review | trip_id | ccp_trip | 评价属于行程 |
| ccp_trip_review | reviewer_user_id | ccp_mini_user | 评价人是用户 |
| ccp_trip_review | target_user_id | ccp_mini_user | 被评价人是用户 |
| ccp_user_school_auth | user_id | ccp_mini_user | 学校认证属于用户 |
| ccp_user_school_auth | school_id | ccp_school | 学校认证属于学校 |
| ccp_user_reputation | user_id | ccp_mini_user | 信誉统计属于用户 |
| ccp_user_blacklist | user_id | ccp_mini_user | 黑名单属于用户 |
| ccp_user_route | user_id | ccp_mini_user | 常用路线属于用户 |

## 5 前端与后端交互设计

### 5.1 小程序前端请求封装

小程序前端使用utils/http.js封装所有请求，主要功能包括：
- 统一的请求前缀（BASE_URL）
- 自动添加token到请求头
- 统一的错误处理
- 加载状态管理
- 重试机制

**核心代码**：
```javascript
// http.js
const request = (url, method, data) => {
  const token = wx.getStorageSync('token');
  return new Promise((resolve, reject) => {
    wx.request({
      url: BASE_URL + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data);
        } else {
          reject(res.data);
          wx.showToast({
            title: res.data.msg,
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        reject(err);
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
    });
  });
};
```

### 5.2 管理端前端请求封装

管理端前端使用axios封装请求，主要功能包括：
- 统一的请求前缀（VUE_APP_BASE_API）
- 自动添加token到请求头
- 统一的错误处理
- 响应拦截器
- 请求取消机制

**核心代码**：
```javascript
// request.js
import axios from 'axios';

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 5000
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = getToken();
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== 200) {
      ElMessage.error(res.msg || 'Error');
      return Promise.reject(new Error(res.msg || 'Error'));
    } else {
      return res;
    }
  },
  error => {
    ElMessage.error(error.msg || '网络请求失败');
    return Promise.reject(error);
  }
);
```

## 6 安全性设计

### 6.1 认证与授权

1. **小程序端**：
   - 使用微信登录获取openid，作为用户唯一标识
   - 生成JWT令牌，用于后续API请求认证
   - 基于认证状态控制功能访问：
     - 未实名认证：无法使用拼车功能
     - 已实名认证但未学校认证：无法使用拼车功能
     - 已实名认证且已学校认证：可以使用所有拼车功能

2. **管理端**：
   - 使用若依框架的RBAC权限控制
   - 基于角色分配菜单和操作权限
   - 细粒度的接口权限控制

### 6.2 数据安全

1. **数据脱敏**：
   - 身份证号：显示前6位和后4位，中间用*替换
   - 手机号：显示前3位和后4位，中间用*替换
   - 学生证号：根据实际情况进行脱敏

2. **数据加密**：
   - 敏感数据（如密码）采用BCrypt加密存储
   - 传输过程采用HTTPS加密
   - JWT令牌使用安全的密钥签名

3. **SQL注入防护**：
   - 使用MyBatis的参数绑定，防止SQL注入
   - 输入参数校验，过滤危险字符

4. **XSS防护**：
   - 前端输入进行过滤和转义
   - 后端返回数据进行HTML转义
   - 使用若依框架的XSS过滤器

### 6.3 接口安全

1. **请求频率限制**：
   - 对敏感接口（如登录、发送消息）进行频率限制
   - 使用Redis实现接口限流

2. **接口幂等性**：
   - 对重要操作（如发布订单、加入订单）实现幂等性设计
   - 使用token或唯一标识防止重复提交

3. **跨域防护**：
   - 配置正确的CORS策略
   - 限制允许的域名和请求方法

## 7 性能优化设计

### 7.1 数据库优化

1. **索引设计**：
   - 对常用查询字段创建索引，如ccp_trip表的campus_id、status、departure_time字段
   - 对关联字段创建索引，如ccp_trip_member表的trip_id、user_id字段
   - 对唯一约束字段创建唯一索引，如ccp_mini_user表的open_id字段

2. **分表分库**：
   - 目前系统采用单库设计，未来可根据业务增长情况，考虑对大表进行分表
   - 如ccp_trip_chat表可按trip_id进行分表
   - 如ccp_mini_user表可按school_id进行分库

3. **查询优化**：
   - 使用分页查询，避免一次性查询大量数据
   - 合理使用连接查询和子查询
   - 避免在查询条件中使用函数操作

### 7.2 缓存设计

1. **Redis缓存**：
   - 缓存热点数据，如学校、校区、校门信息
   - 缓存用户认证状态，减少数据库查询
   - 缓存拼车订单列表，提高查询效率

2. **本地缓存**：
   - 使用Guava Cache或Caffeine缓存常用数据
   - 缓存配置信息和常量数据

### 7.3 代码优化

1. **异步处理**：
   - 对耗时操作（如发送消息、生成日志）采用异步处理
   - 使用Spring的@Async注解实现异步方法

2. **批量操作**：
   - 对批量数据操作（如批量更新状态）使用批量SQL
   - 减少数据库连接次数

3. **懒加载**：
   - 对关联数据采用懒加载，减少初始查询数据量
   - 使用MyBatis的association和collection实现懒加载

## 8 监控与日志设计

### 8.1 操作日志

1. **管理员操作日志**：
   - 使用若依框架的日志记录功能
   - 记录管理员的登录、退出、增删改查等操作
   - 日志存储在sys_oper_log表

2. **用户操作日志**：
   - 记录用户的关键操作，如发布订单、加入订单、发送消息等
   - 日志存储在ccp_user_action_log表
   - 用于分析用户行为和故障排查

### 8.2 系统日志

1. **应用日志**：
   - 使用SLF4J + Logback记录应用日志
   - 日志级别分为DEBUG、INFO、WARN、ERROR
   - 日志按日期和大小滚动存储

2. **错误日志**：
   - 记录系统错误和异常信息
   - 包含错误堆栈信息，便于排查问题
   - 定期分析错误日志，优化系统

### 8.3 监控指标

1. **JVM监控**：
   - 监控堆内存、非堆内存使用情况
   - 监控GC频率和耗时
   - 监控线程数量和状态

2. **数据库监控**：
   - 监控数据库连接池状态
   - 监控SQL执行效率
   - 监控慢查询

3. **接口监控**：
   - 监控接口响应时间
   - 监控接口调用次数和成功率
   - 监控接口错误率

4. **业务指标监控**：
   - 监控日活跃用户数
   - 监控发布订单数和完成订单数
   - 监控用户认证通过率

## 9 扩展性设计

### 9.1 模块化设计

系统采用模块化设计，各模块之间低耦合，便于扩展和维护：

- ccp-core：核心业务逻辑，独立于具体的接口实现
- ccp-miniprogram：小程序接口，可独立部署
- ruoyi-admin：管理端接口，基于若依框架
- ccp-mp-ui：小程序前端，可独立开发和发布
- ruoyi-ui：管理端前端，基于若依框架

### 9.2 API设计

系统提供统一的RESTful API，便于第三方集成和扩展：

- 小程序端API：/mp/**
- 管理端API：/ccp/**
- 采用标准的HTTP方法和状态码
- 响应格式统一为{code, msg, data}

### 9.3 插件化设计

系统设计支持插件化扩展，可通过插件方式添加新功能：

- 支付插件：支持不同支付方式的接入
- 地图插件：支持不同地图服务的接入
- 消息插件：支持不同消息推送方式的接入
- 认证插件：支持不同认证方式的接入

### 9.4 多学校支持

系统设计支持多学校接入，各学校数据隔离：

- 学校信息存储在ccp_school表
- 校区信息存储在ccp_school_campus表
- 用户学校认证存储在ccp_user_school_auth表
- 行程信息关联到具体学校和校区

## 10 代码结构与命名规范

### 10.1 代码结构

```
ccp/
├── ccp-common/          # 公共组件
│   ├── src/main/java/org/ba7lgj/ccp/common/
│   │   ├── domain/       # 公共领域对象
│   │   ├── dto/          # 数据传输对象
│   │   ├── enums/        # 枚举类
│   │   ├── util/         # 工具类
│   │   └── vo/           # 视图对象
├── ccp-core/            # 核心业务逻辑
│   ├── src/main/java/org/ba7lgj/ccp/core/
│   │   ├── domain/       # 领域对象
│   │   ├── mapper/       # 数据访问层
│   │   └── service/      # 业务逻辑层
├── ccp-miniprogram/      # 小程序后端API
│   ├── src/main/java/org/ba7lgj/ccp/miniprogram/
│   │   ├── controller/   # 控制器
│   │   ├── service/      # 服务层
│   │   └── vo/           # 视图对象
├── ccp-mp-ui/            # 小程序前端
│   ├── pages/            # 页面
│   ├── services/         # 服务调用
│   ├── utils/            # 工具类
│   └── app.js            # 入口文件
├── ruoyi-admin/          # 管理端后端API
│   ├── src/main/java/com/ruoyi/
│   │   ├── web/          # 控制器
│   │   └── RuoYiApplication.java # 启动类
├── ruoyi-ui/             # 管理端前端
│   ├── src/
│   │   ├── api/          # API调用
│   │   ├── components/   # 组件
│   │   ├── views/        # 页面
│   │   └── App.vue       # 入口组件
```

### 10.2 命名规范

1. **包命名**：
   - 采用域名倒置的方式，如org.ba7lgj.ccp
   - 模块名使用小写字母，如ccp-core
   - 子包名使用单数形式，如domain、service

2. **类命名**：
   - 采用大驼峰命名法，如TripService
   - 接口名使用大写字母I开头，如ITripService
   - 实现类名使用Impl结尾，如TripServiceImpl
   - 控制器名使用Controller结尾，如MpTripController

3. **方法命名**：
   - 采用小驼峰命名法，如publishTrip
   - 方法名要能清晰表达其功能
   - 查询方法使用get或list开头，如getTripDetail
   - 更新方法使用update开头，如updateTripStatus
   - 保存方法使用save或add开头，如saveTrip

4. **变量命名**：
   - 采用小驼峰命名法，如tripId
   - 避免使用缩写，如userId而不是uid
   - 常量使用全大写，下划线分隔，如MAX_PEOPLE_COUNT

5. **数据库命名**：
   - 表名使用小写字母，下划线分隔，如ccp_trip_member
   - 字段名使用小写字母，下划线分隔，如user_id
   - 主键名统一使用id
   - 外键名使用关联表名+主键名，如trip_id

6. **接口命名**：
   - URL使用小写字母，斜杠分隔，如/mp/trip/publish
   - 资源名使用复数形式，如/trips而不是/trip
   - 操作名使用动词，如/publish、/join、/quit

## 11 总结

本详细设计文档深入描述了校园拼车系统的模块划分、接口设计、关键Service实现、数据库设计、定时任务、安全性设计、性能优化等方面。文档以实际代码和数据库结构为基础，确保设计与实现的一致性。

系统采用前后端分离架构，基于若依框架开发，具有良好的扩展性和可维护性。通过严格的认证机制、完整的订单状态流转、实时聊天功能和用户信誉体系，确保了系统的安全性、可靠性和用户体验。

未来可根据业务需求，进一步扩展系统功能，如接入地图服务、支持在线支付、增强风控策略等。同时，可根据系统运行情况，对数据库、缓存、接口等进行优化，提高系统的性能和稳定性。