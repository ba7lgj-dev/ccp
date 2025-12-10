迭代次数：1
本次生成的全部模块列表：
- ccp-mp-ui/app.js：小程序全局入口，启动登录并根据用户学校/校区跳转。
- ccp-mp-ui/app.json：配置小程序页面路由及窗口标题。
- ccp-mp-ui/utils/http.js：封装带 token 与重登录的请求工具。
- ccp-mp-ui/utils/auth.js：登录、token 存储与手机号绑定调用。
- ccp-mp-ui/utils/cache.js：学校、校区、校门本地缓存工具。
- ccp-mp-ui/services/*.js：前端调用后端的认证、学校、校区、校门服务封装。
- ccp-mp-ui/pages/login/*：手机号快捷登录界面与跳转逻辑。
- ccp-mp-ui/pages/school/select/*：学校选择、搜索与缓存。
- ccp-mp-ui/pages/campus/select/*：校区选择及缓存。
- ccp-mp-ui/pages/trip/publish/*：发布拼车页的起点/终点校门选择能力。
- ccp-miniprogram/controller/*：Auth、School、Campus、Gate 控制器接口实现。
- ccp-miniprogram/service 及 impl：微信登录、用户、学校、校区、校门业务逻辑。
- ccp-miniprogram/mapper 与 XML：用户、学校、校区、校门的 MyBatis 映射。
- ccp-miniprogram/domain：用户、学校、校区、校门实体。
- ccp-miniprogram/vo、dto：统一返回包装、登录返回、数据传输对象。
- ccp-miniprogram/util：JWT 生成与解析工具。
- ccp-miniprogram/interceptor 与 config：token 拦截校验与 MVC 注册。

本次更新的内容包含：
- 完成微信小程序端 Phase 1 的登录、学校/校区选择、校门选择 UI 与交互。
- 实现前端网络封装、缓存工具和服务调用，形成与后端接口契合的请求结构。
- 构建后端认证、学校、校区、校门查询接口，提供统一 Result 返回格式。
- 新增 JWT 生成/解析、拦截器白名单校验，并在控制器中返回 LoginVO 结构。
- 搭建 MyBatis 映射与实体，支持用户创建、手机号更新及基础数据查询。
