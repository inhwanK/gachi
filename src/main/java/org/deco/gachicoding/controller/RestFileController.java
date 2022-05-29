package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "파일 처리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestFileController {
    private final FileService fileService;

    @ApiOperation(value = "파일 임시 저장")
    @ApiResponses(
            @ApiResponse(code = 200, message = "임시 폴더의 파일 URL 반환")
    )
    @PostMapping("/file/upload")
    public List<String> fileUploadImageFile(@ModelAttribute("files") List<MultipartFile> files) throws IOException {

        return fileService.uploadTempImg(files);
    }
}
