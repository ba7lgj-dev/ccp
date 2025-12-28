# 校园拼车小程序 CCP 基础工程规范与阶段一功能提示

────────────────────────────────
## （一）小程序基础规范（全局）

### 工程目录结构规范
```
ccp-miniprogram/
├─ app.js               # 全局初始化、版本检查、基础配置、埋点启动
├─ app.json             # 路由、分包、窗口配置
├─ app.wxss             # 全局样式、主题变量
├─ pages/               # 业务页面（示例：school/select、campus/select、ride/publish 等）
├─ components/          # 复用组件（search-bar、card、dialog、skeleton 等）
├─ utils/               # 工具库 http.js、auth.js、cache.js、date.js、validator.js
├─ services/            # 按模块拆分 API（auth.js、school.js、campus.js、gate.js ...）
├─ store/               # 全局状态（用户、学校、校区、校门选择等）
├─ styles/              # 主题、mixins、公共样式
├─ config/              # 环境配置（接口域名、版本号、埋点开关）
└─ static/              # 静态资源（logo、占位图）
```

### 网络层规范（utils/http.js）
- `request(options)`：统一封装 wx.request，Promise 化。
- **token 注入**：从 storage + 内存获取 token，放入 `Authorization: Bearer <token>`。
- **自动刷新 token**：401/特定 code 触发 `auth.reLogin()`，刷新后重放队列请求（幂等控制）。
- **错误重试机制**：对幂等 GET/HEAD 请求默认 2 次指数退避（200ms/500ms）。
- **超时配置**：全局 10s，可通过 options 覆盖。
- **全局 loading 控制**：可选 `showLoading`；并在 finally 关闭。
- **统一错误提示**：后端 `code != 0` 统一由 http 拦截层调用 `wx.showToast` 或全局弹窗。
- **防重复请求机制**：同 URL+method+data 短时间（默认 1s）内去重，可通过 options.disableDedup 关闭。

### 鉴权规范（utils/auth.js）
- 登录流程：`wx.login` → 后端 `/auth/wxLogin` 换取 token、用户信息 → `storage` 持久化 token、过期时间、用户快照。
- 启动校验：`checkLogin()` 在 `app.js onLaunch` 执行，判断 token 是否过期；过期则 `reLogin()`。
- 访问受限页面前校验学校/校区选择；未选择自动跳转 pages/school/select 或 pages/campus/select。

### 缓存规范（utils/cache.js）
- 使用 `storage + 内存` 双层缓存。
- 缓存项：学校列表、校区列表、校门列表，过期时间默认 10 分钟；使用 `{data, expireAt}` 结构。
- 用户选择的学校/校区需持久存储（无过期），更换校区后清空校门/地点相关缓存。

### 组件使用规范
- `scroll-view`：列表必须使用，`scroll-y` 开启，支持 `refresher-enabled` 下拉刷新与 `bindscrolltolower` 触底加载。
- `picker`：使用 `bindchange` 回调更新状态；数据源来自服务层接口；起点/终点校门各自独立。
- `input`：使用 `bindinput` 双向更新；需防抖搜索；设置 `confirm-type="search"`。
- `bindtap`：所有点击事件使用 `bindtap`（或 `catchtap` 阻止冒泡）。
- `image`：统一 `mode="aspectFit"`，设置占位 & 默认图。
- 表单：使用 form/label 语义化组件，字段校验集中在 `utils/validator.js`。

### UI 交互规范
- 适配 iPhone/Android，不依赖非兼容 API；底部安全区适配刘海屏。
- 所有提交按钮有防重复点击（loading + disable）。
- 列表项需 hover/active 反馈；弱网显示骨架屏或 loading。
- 统一错误提示 `wx.showToast` 或全局弹窗；全局异常兜底（网络/超时）。
- 列表支持下拉刷新 + 触底加载；空状态提供引导和重试。

────────────────────────────────
## （二）后端基础规范（Spring Boot）

### 工程结构规范（示例）
```
ccp-core/src/main/java/com/ccp/
├─ controller/        # AuthController、SchoolController、CampusController、GateController
├─ service/
│   ├─ impl/
├─ mapper/
│   ├─ xml/
├─ domain/            # DO 实体
├─ dto/               # 入参 DTO
├─ vo/                # 出参 VO
├─ config/            # TokenInterceptor、WxConfig、MyBatisConfig
└─ util/
```

### Controller 规范
- 每个模块单独 Controller，类注释说明用途。
- 所有入参 DTO 使用 `@Validated`；分页参数默认 pageNum/pageSize。
- 统一返回 `{code, msg, data}`；不得暴露数据库字段，转换为 VO。
- 白名单接口（如学校列表、校门列表）通过拦截器放行，但仍记录访问日志。

### Service 规范
- 只承载业务逻辑，不写 SQL，全部通过 Mapper。
- 读写方法拆分（query/update/create/delete）；必要时 `@Transactional`。
- 负责缓存穿透保护（如学校列表缓存）。

### Mapper 规范
- Mapper 返回 DO；XML 使用 `<where>` 动态拼接条件；必要时 `<trim>` 去除多余 AND。
- 查询必须支持多条件（名称、城市、状态等）。

### Token 鉴权规范
- 拦截器解析 `Authorization: Bearer <token>`，校验后将 `userId` 注入 `ThreadLocal`（如 `UserContext`）。
- 在 AOP 或拦截器中清理 ThreadLocal，避免泄露。
- 白名单接口配置在 `TokenInterceptor`，如 `/auth/wxLogin`, `/school/list`, `/campus/listBySchool`, `/gate/listByCampus`。

### 错误码规范
- `Bxxxx` 业务错误（如 B1001 学校不存在）。
- `Pxxxx` 参数错误（如 P1001 缺少 schoolId）。
- `Sxxxx` 系统错误（如 S0001 内部异常）。
- 小程序端按 code 决定提示或跳转。

────────────────────────────────
## （三）日志规范与埋点规范

### 后端日志
- 重要操作 info 日志，含用户、参数、结果概要。
- 调用微信 API 记录请求/响应（脱敏处理）。
- 异常 error 日志，包含栈、关键上下文。

### 小程序端埋点
- 页面访问：进入、离开、停留时长（onShow/onHide/onUnload）。
- 用户选择学校/校区埋点：记录学校/校区 ID、名称、时间。
- 拼车流程：后续需求需留埋点入口（选择校门、发布、接单）。

### 错误上报
- 前端 `App.onError`/`Page.onError` 捕获后上报后端接口，含机型、网络、堆栈。

────────────────────────────────
## （四）版本管理与兼容性规范

- 小程序版本号与后端接口版本字段同步（config 中维护 apiVersion）。
- 缓存结构变化时，通过版本号比对自动清理旧缓存。
- 兼容最低微信基础库版本要求（根据使用组件设定，如 ≥ 2.30.0）。
- 组件适配刘海屏/Android 多分辨率，使用 rpx、safe-area。

────────────────────────────────
## （五）阶段一：学校管理模块（C 端 + 后端）页面提示词

### MP1 用户选择学校（`pages/school/select`）
- **目标**：首次进入必须选择学校；有缓存则跳过至下一步。
- **UI**：顶部搜索框（input + 搜索按钮），学校列表使用 `scroll-view`；每项卡片含 logo、名称、城市。
- **交互**：
  - 输入关键词本地过滤（防抖 300ms）。
  - 下拉刷新 → 重新加载学校列表。
  - 点击学校 → 写入缓存（storage + store）→ 跳转校区选择页。
- **缓存**：学校列表缓存 10 分钟；已选学校长期存储，版本变更需清理。
- **风险**：无匹配结果显示兜底提示；弱网显示骨架屏、重试按钮。

### MP2 用户选择校区（`pages/campus/select`）
- **UI**：显示已选学校信息；校区列表展示名称、地址。
- **交互**：点击校区 → 缓存校区 → 跳转首页/发布页；学校无校区则提示“暂未开通”。
- **缓存**：校区持久化存储；切换校区后清空校门/地点缓存。

### MP3 发布拼车（基础，`pages/ride/publish`）
- **目标**：校门选择。
- **流程**：
  - 检查是否已选校区，未选跳转校区选择页。
  - 已选则请求校门列表（按 sort 排序），填充起点/终点 `picker`。
- **交互**：`picker` 使用 `bindchange` 设置当前选择；数据源来自接口返回。

────────────────────────────────
## （四）后端接口提示词（小程序端 API）

### 控制器与职责
- **AuthController**：`/auth/wxLogin`，处理 wxCode 换 token，返回用户基础信息和 token。
- **SchoolController**：`/school/list`，分页/条件查询学校列表，支持名称/城市过滤，白名单接口。
- **CampusController**：`/campus/listBySchool`，根据 schoolId 查询校区列表，白名单接口。
- **GateController**：`/gate/listByCampus`，根据 campusId 查询校门列表，白名单接口。

### 接口规范（示例）
1. **POST /auth/wxLogin**
   - 参数：{ code (微信登录 code), userInfo(optional) }
   - 返回：{ code:0, msg:"ok", data:{ token, expireAt, userId, nickname, avatar } }
   - 错误码：P1001 缺少 code；B1001 登录失败；S0001 系统异常。
   - 缓存：服务端可缓存会话校验结果，前端持久化 token。

2. **GET /school/list**
   - 参数：keyword、city、pageNum、pageSize
   - 返回：{ code:0, data:{ list:[{id,name,city,logo}], total } }
   - 错误码：P1002 参数错误；S0001 系统异常。
   - 缓存：服务端可加本地缓存，前端 10 分钟缓存。

3. **GET /campus/listBySchool**
   - 参数：schoolId(required)
   - 返回：{ code:0, data:[{id,name,address}] }
   - 错误码：P1003 缺少 schoolId；B1002 学校不存在。
   - 缓存：前端 10 分钟缓存，切换学校清空。

4. **GET /gate/listByCampus**
   - 参数：campusId(required)
   - 返回：{ code:0, data:[{id,name,sort}] }
   - 错误码：P1004 缺少 campusId；B1003 校区不存在。
   - 缓存：前端 10 分钟缓存，切换校区清空。

### 控制器生成规则
- 使用 `@RestController` + `/api/mp` 前缀；方法命名 `list`, `login`, `listBySchool`, `listByCampus` 等。
- 入参 DTO + `@Valid`；返回 `CommonResponse`。

### Service 方法职责
- AuthService：校验 wxCode、调用微信接口、创建/更新用户、生成 token。
- SchoolService：按条件查询、可走缓存。
- CampusService：根据 schoolId 查询，处理无数据提示。
- GateService：按 campusId + sort 查询，校验校区归属。

### Mapper 查询规则
- `SchoolMapper.selectList(keyword, city)` 使用动态 `<where>`；支持 like 名称和城市过滤。
- `CampusMapper.listBySchool(schoolId)`；`GateMapper.listByCampus(campusId)` 按 sort asc。

### 线程上下文注入 userId
- `TokenInterceptor` 解析 token → `UserContext.set(userId)`；Controller/Service 通过 `UserContext.getUserId()` 获取；请求结束 `UserContext.clear()`。

### 异常处理规范
- 全局异常处理器将异常转换为 `{code,msg}`；参数异常→P 前缀，业务异常→B 前缀，系统异常→S 前缀；记录日志。

────────────────────────────────
## （五）小程序服务层和工具库规范（utils + services）

### utils/http.js
- `request({url,method='GET',data,headers,timeout,showLoading})`：封装 wx.request。
- token 注入；401 触发刷新；统一 `code!=0` 提示；超时 10s；幂等 GET 自动重试；防重复请求。

### utils/auth.js
- `login()`：wx.login → /auth/wxLogin → 存储 token、expireAt。
- `checkLogin()`：校验 token 是否过期，必要时 `reLogin()`。
- `reLogin()`：刷新 token，失败时清除缓存并跳转登录/授权页。
- 请求前注入 token；提供 `getToken()`。

### utils/cache.js
- `setCache(key, data, ttl)` / `getCache(key)` / `removeCache(key)`；
- 预设 key：SCHOOL_LIST、CAMPUS_LIST_<schoolId>、GATE_LIST_<campusId>；默认 ttl 10 分钟。
- `setSelectedSchool`/`getSelectedSchool`，`setSelectedCampus`/`getSelectedCampus` 持久化。

### utils/date.js
- 时间格式化、倒计时、友好时间。

### services 约定
- `services/auth.js`：封装 `/auth/wxLogin`。
- `services/school.js`：`fetchSchoolList(params)` 调用 `/school/list`。
- `services/campus.js`：`fetchCampusList(schoolId)` 调用 `/campus/listBySchool`。
- `services/gate.js`：`fetchGateList(campusId)` 调用 `/gate/listByCampus`。
- 所有 services 依赖 `utils/http.js`，不直接使用 wx.request。

