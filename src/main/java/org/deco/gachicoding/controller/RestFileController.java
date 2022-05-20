package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.service.impl.S3ServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestFileController {
    private final S3ServiceImpl s3Service;

    @ApiOperation(value = "파일 임시 저장")
    @PostMapping("/file/upload")
    public List<String> fileUploadImageFile(@ModelAttribute("files") List<MultipartFile> files) throws IOException {

        return s3Service.uploadTempImg(files);
    }
}
