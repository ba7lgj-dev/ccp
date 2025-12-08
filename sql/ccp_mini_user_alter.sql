-- CCP mini program user real-name schema migration
ALTER TABLE `ccp_mini_user`
    DROP COLUMN IF EXISTS `student_no`,
    DROP COLUMN IF EXISTS `auth_status`,
    DROP COLUMN IF EXISTS `auth_fail_reason`,
    DROP COLUMN IF EXISTS `review_by`,
    DROP COLUMN IF EXISTS `review_time`;

ALTER TABLE `ccp_mini_user`
    ADD COLUMN IF NOT EXISTS `id_card_name` varchar(64) DEFAULT NULL COMMENT '证件姓名' AFTER `real_name`,
    ADD COLUMN IF NOT EXISTS `id_card_number` varchar(32) DEFAULT NULL COMMENT '证件号码' AFTER `id_card_name`,
    ADD COLUMN IF NOT EXISTS `face_image_url` varchar(255) DEFAULT NULL COMMENT '人脸图片地址' AFTER `id_card_number`,
    ADD COLUMN IF NOT EXISTS `face_verify_result` varchar(255) DEFAULT NULL COMMENT '人脸校验结果' AFTER `face_image_url`,
    ADD COLUMN IF NOT EXISTS `real_auth_status` tinyint(1) DEFAULT 0 COMMENT '实名状态 0未认证 1待审核 2已认证 3不通过' AFTER `status`,
    ADD COLUMN IF NOT EXISTS `real_auth_fail_reason` varchar(255) DEFAULT NULL COMMENT '实名审核失败原因' AFTER `real_auth_status`,
    ADD COLUMN IF NOT EXISTS `real_auth_review_by` bigint(20) DEFAULT NULL COMMENT '实名审核人' AFTER `real_auth_fail_reason`,
    ADD COLUMN IF NOT EXISTS `real_auth_review_time` datetime DEFAULT NULL COMMENT '实名审核时间' AFTER `real_auth_review_by`;
