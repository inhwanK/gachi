package org.deco.gachicoding.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.service.impl.FileServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.URI;

// 리팩토링 필요 구현 덜 됬다 건들면 손모가지,,
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestFileController {
    private final FileServiceImpl fileService;

//    private static final String filePath = "C:\\mp\\tempImg\\";
//
//    @ResponseBody
//    @PostMapping("/file/upload")
//    public JSONObject fileUploadImageFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        JSONObject jsonObject = new JSONObject();
////        JsonObject jsonObject = new JsonObject();
//
//        Path root = Path.of(filePath);
//        if (!Files.exists(root)) {
//            Files.createDirectories(root);
//        }
//        String origFileName = multipartFile.getOriginalFilename();
//        String extension = origFileName.substring(origFileName.lastIndexOf("."));
//
//        String saveFileName = UUID.randomUUID() + extension;
//
//        File targetFile = new File(filePath + saveFileName);
//
//        try{
//            InputStream fileStream = multipartFile.getInputStream();
//            FileUtils.copyInputStreamToFile(fileStream, targetFile);    // 파일 저장
//            jsonObject.appendField("url", "/imageFiles/"+saveFileName);
//            jsonObject.appendField("responseCode", "success");
////            jsonObject.addProperty("url", "/imageFiles/"+saveFileName);
////            jsonObject.addProperty("responseCode", "success");
//        } catch (IOException e) {
//            FileUtils.deleteQuietly(targetFile);    // 저장된 파일 삭제
//            jsonObject.appendField("responseCode", "error");
////            jsonObject.addProperty("responseCode", "error");
//            e.printStackTrace();
//        }
//
//        return jsonObject;
//    }

    @ApiOperation(value = "파일 임시 저장")
    @PostMapping(value = "/file/upload",  produces = "application/json; charset=utf8")
    public String fileUploadImageFile(MultipartHttpServletRequest mpRequest) throws IOException {

        return fileService.copyTempImage(mpRequest);
    }
}
