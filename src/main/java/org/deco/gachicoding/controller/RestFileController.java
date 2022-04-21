package org.deco.gachicoding.controller;

import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestFileController {

    @ResponseBody
    @PostMapping("/file/upload")
    public JSONObject fileUploadImageFile(@RequestParam("file")MultipartFile multipartFile) throws IOException {

        JSONObject jsonObject = new JSONObject();
//        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\imageFiles\\";
        Path root = Path.of(fileRoot);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        String origFileName = multipartFile.getOriginalFilename();
        String extension = origFileName.substring(origFileName.lastIndexOf("."));

        String saveFileName = UUID.randomUUID() + extension;

        File targetFile = new File(fileRoot + saveFileName);

        try{
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);    // 파일 저장
            jsonObject.appendField("url", "/imageFiles/"+saveFileName);
            jsonObject.appendField("responseCode", "success");
//            jsonObject.addProperty("url", "/imageFiles/"+saveFileName);
//            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);    // 저장된 파일 삭제
            jsonObject.appendField("responseCode", "error");
//            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }
}
