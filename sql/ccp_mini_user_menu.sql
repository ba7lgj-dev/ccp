-- 微信小程序用户管理菜单（如与现有 menu_id 冲突请上调数值）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (20060, '小程序管理', 0, 10, 'ccpmp', '', NULL, '', 1, 0, 'M', '0', '0', NULL, 'wechat', 'admin', NOW(), '', NULL, '微信小程序相关功能目录');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (20061, '微信用户管理', 20060, 1, 'miniUser', 'ccp/miniUser/index', NULL, 'CcpMiniUser', 1, 0, 'C', '0', '0', 'ccp:miniUser:list', 'user', 'admin', NOW(), '', NULL, '微信小程序用户管理页面');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (20062, '微信用户查询', 20061, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ccp:miniUser:query', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (20063, '微信用户编辑', 20061, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ccp:miniUser:edit', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (20064, '微信用户导出', 20061, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ccp:miniUser:export', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (20065, '实名认证审核', 20061, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ccp:miniUser:auth', '#', 'admin', NOW(), '', NULL, '');
