-- Campus Carpool admin menu inserts
-- Aligns with RuoYi sys_menu schema

-- Parent directory: 校园拼车管理（仅包含已上线后台页面）
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES
(20040, '校园拼车管理', 0, 6, 'ccpTrip', NULL, '', '', 1, 0, 'M', '0', '0', NULL, 'car', 'admin', NOW(), '', NULL, '校园拼车管理目录');

-- 拼车订单管理（后端：/ccp/trip/trip，前端：ruoyi-ui/src/views/ccp/trip/trip/index.vue）
INSERT INTO `sys_menu` VALUES (20041, '拼车订单管理', 20040, 1, 'trip', 'ccp/trip/trip/index', '', '', 1, 0, 'C', '0', '0', 'ccp:trip:list', 'list', 'admin', NOW(), '', NULL, '拼车订单管理菜单');
INSERT INTO `sys_menu` VALUES (200410, '订单查询', 20041, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'ccp:trip:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (200411, '订单编辑', 20041, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'ccp:trip:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (200412, '订单导出', 20041, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'ccp:trip:export', '#', 'admin', NOW(), '', NULL, '');

-- 拼车成员管理（后端：/ccp/trip/member，前端：ruoyi-ui/src/views/ccp/trip/member/index.vue）
INSERT INTO `sys_menu` VALUES (20042, '拼车成员管理', 20040, 2, 'member', 'ccp/trip/member/index', '', '', 1, 0, 'C', '0', '0', 'ccp:tripMember:list', 'peoples', 'admin', NOW(), '', NULL, '拼车成员管理菜单');
INSERT INTO `sys_menu` VALUES (200420, '成员编辑', 20042, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'ccp:tripMember:edit', '#', 'admin', NOW(), '', NULL, '');
