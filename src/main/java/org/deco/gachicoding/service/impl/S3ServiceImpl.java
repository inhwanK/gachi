package org.deco.gachicoding.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.dto.file.FileSaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class S3ServiceImpl {
    private AmazonS3 s3Client;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public List<String> uploadTempImg(List<MultipartFile> files) throws IOException {
        List<String> result = new ArrayList<>();

        // 저장 경로 바꿔야 함 (날짜도 추가)
        for(MultipartFile f : files) {
            String origFileName = null;
            String saveFileName = null;
            String filePath = null;

            System.out.println("f : "+f.getOriginalFilename());
            // 파일 정보 추출 (이걸 dto 생성 시 바로 해버리면?)
            origFileName = f.getOriginalFilename();
            // 경로에 userId 추가
            saveFileName = "temp/" + UUID.randomUUID() + "_" + origFileName;

            filePath = putS3(f, saveFileName);

            result.add(filePath);
        }
        return result;
    }

    public void uploadRealImg(List<String> paths, Long boardIdx, String category) throws IOException, URISyntaxException {
        String origFileName = null;
        String origFileExtension = null;
        String tamperingFileName = null;
        String filePath = null;
        String oldPath = null;
        String newPath = null;
        Long fileSize = null;  // bytes size    -> 이 세키가 골때리네

        // 저장 경로 바꿔야 함 (날짜도 추가)
        for(String path : paths) {
            System.out.println("path : " + path);

            // oldPath -> substring, indexOf, lastIndexOf -> 뒤에서 부터 인덱스 찾기
            oldPath = URLDecoder.decode(path.substring(path.indexOf("temp")),"UTF-8");
            tamperingFileName = oldPath.split("temp/")[1];  //[4] -> /의 수에따라 바뀜

            origFileName = tamperingFileName.substring(tamperingFileName.indexOf("_")).replaceFirst("_", "");
            origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));

            // 날짜 추가
            newPath = category + "/" + boardIdx + "/" + tamperingFileName;

            fileSize = convertFile(path, tamperingFileName);

            filePath = update(oldPath, newPath);

            System.out.println("oldPath : " + oldPath);
            System.out.println("tamperingFileName : " + tamperingFileName);
            System.out.println("origFileName : " + origFileName);
            System.out.println("origFileExtension : " + origFileExtension);
            System.out.println("newPath : " + newPath);
            System.out.println("fileSize : " + fileSize);
            System.out.println("filePath : " + filePath);

            FileSaveDto dto = FileSaveDto.builder()
                    .boardIdx(boardIdx)
                    .boardCategory(category)
                    .origFilename(origFileName)
                    .fileExt(origFileExtension)
                    .saveFilename(tamperingFileName)
                    .fileSize(fileSize)
                    .filePath(filePath)
                    .build();

            fileService.registerFile(dto);

            System.out.println("----------------------------------------------------");
        }
    }

//    private Long getFileSize() {
//        return null;
//    }

    private Long convertFile(String filePath, String tamperingFileName) throws IOException {
        URL url = new URL(filePath);
        String canonicalPath = new File("").getCanonicalPath();
        Path savePath = Paths.get(canonicalPath + "/src/main/resources/tempImg/", tamperingFileName);

        InputStream is = url.openStream();
        Files.copy(is, savePath);

        is.close();

        Long bytes = Files.size(savePath);
        Long kilobyte = bytes/1024;
        Long megabyte = kilobyte/1024;

        Files.delete(savePath);

        return bytes;
    }

    private String update(String oldPath, String newPath) {
        String result = replaceS3(oldPath, newPath);
        deleteS3(oldPath);

        return result;
    }

    private String getS3Url(String filePath) {
        // s3 객체 URL
        return s3Client.getUrl(bucket, filePath).toString();
    }

    private String putS3(MultipartFile file, String saveFilePath) throws IOException {
        // s3 저장
        s3Client.putObject(new PutObjectRequest(bucket, saveFilePath, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return getS3Url(saveFilePath);
    }

    private String replaceS3(String oldPath, String newPath) {
        s3Client.copyObject(bucket, oldPath, bucket, newPath);

        return getS3Url(newPath);
    }

    private void deleteS3(String source) {
        s3Client.deleteObject(bucket, source);
    }
}