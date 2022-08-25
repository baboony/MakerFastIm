package com.maker.controller;

import com.maker.utils.FileUtil;
import com.maker.utils.Result;
import com.maker.utils.ResultEnums;
import com.maker.utils.UUIDUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lucky winner
 */
@RestController
@RequestMapping("/maker/common")
@CrossOrigin
public class CommonController {

    @PostMapping("/uploadImage")
    public Result upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        fileName = UUIDUtils.get() + "+" + fileName;
        //设置文件上传路径
        String filePath = "D:\\nginx-1.18.0\\html\\dist\\MsgLog\\images";
//        String filePath = FilePathEnum.DISTRIBUTIONMODE.path;

        try {
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUIDUtils.get() + suffixName;
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            return Result.ok(ResultEnums.SUCCESS,   "/MsgLog/images/" + fileName);
        } catch (Exception e) {
            return Result.fail("上传失败");
        }
    }
}
