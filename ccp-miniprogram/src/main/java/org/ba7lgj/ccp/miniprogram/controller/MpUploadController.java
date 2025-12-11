package org.ba7lgj.ccp.miniprogram.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import java.util.HashMap;
import java.util.Map;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.service.MpUserProfileService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/mp/upload")
public class MpUploadController {

    private final MpUserProfileService mpUserProfileService;

    public MpUploadController(MpUserProfileService mpUserProfileService) {
        this.mpUserProfileService = mpUserProfileService;
    }

    @PostMapping("/avatar")
    public MpResult<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        if (file == null || file.isEmpty()) {
            return MpResult.error(400, "文件不能为空");
        }
        if (file.getSize() > 2 * 1024 * 1024L) {
            return MpResult.error(400, "头像大小不能超过2MB");
        }
        try {
            String url = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
            mpUserProfileService.updateUserAvatar(userId, url);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            return MpResult.ok(data);
        } catch (Exception ex) {
            return MpResult.error(500, "上传失败：" + ex.getMessage());
        }
    }

    @PostMapping("/realAuthImage")
    public MpResult<Map<String, String>> uploadRealAuthImage(@RequestParam("file") MultipartFile file) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        if (file == null || file.isEmpty()) {
            return MpResult.error(400, "文件不能为空");
        }
        if (file.getSize() > 5 * 1024 * 1024L) {
            return MpResult.error(400, "图片大小不能超过5MB");
        }
        try {
            String path = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file, MimeTypeUtils.IMAGE_EXTENSION, true);
            Map<String, String> data = new HashMap<>();
            data.put("path", path);
            return MpResult.ok(data);
        } catch (Exception ex) {
            return MpResult.error(500, "上传失败：" + ex.getMessage());
        }
    }
}
