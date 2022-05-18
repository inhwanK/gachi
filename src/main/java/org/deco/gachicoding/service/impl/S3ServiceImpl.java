package org.deco.gachicoding.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.dto.file.FileSaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    public void upload(List<MultipartFile> files, Long boardIdx, String category) throws IOException {
        String origFileName = null;
        String origFileExtension = null;
        String tamperingFileName = null;
        String saveFileName = null;
        Long fileSize = null;  // bytes size
        String filePath = null;
        
        // 저장 경로 바꿔야 함 (날짜도 추가)
        for(MultipartFile f : files) {
            // 파일 정보 추출 (이걸 dto 생성 시 바로 해버리면?)
            origFileName = f.getOriginalFilename();
            origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));
            tamperingFileName = UUID.randomUUID().toString();
            saveFileName = tamperingFileName + origFileExtension;
            fileSize = f.getSize();
            
            // s3 저장 경로
            String saveFilePath = category + "/" + boardIdx + "/" + saveFileName;

            // s3 저장
            s3Client.putObject(new PutObjectRequest(bucket, saveFilePath, f.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // s3 객체 URL
            filePath = s3Client.getUrl(bucket, saveFileName).toString();

            FileSaveDto dto = FileSaveDto.builder()
                    .boardIdx(boardIdx)
                    .boardCategory(category)
                    .origFilename(origFileName)
                    .fileExt(origFileExtension)
                    .saveFilename(saveFileName)
                    .fileSize(fileSize)
                    .filePath(filePath)
                    .build();

            fileService.registerFile(dto);
        }
    }

    public List<String> copyTempImage(List<MultipartFile> files) throws IOException {
        List<String> result = new ArrayList<>();
        String origFileName = null;
        String filePath = null;

        // 저장 경로 바꿔야 함 (날짜도 추가)
        for(MultipartFile f : files) {
            System.out.println("f : "+f.getOriginalFilename());
            // 파일 정보 추출 (이걸 dto 생성 시 바로 해버리면?)
            origFileName = f.getOriginalFilename();

            // s3 저장 경로
            String saveFilePath = "temp/" + origFileName;

            // s3 저장
            s3Client.putObject(new PutObjectRequest(bucket, saveFilePath, f.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // s3 객체 URL
            filePath = s3Client.getUrl(bucket, origFileName).toString();
            result.add(filePath);
        }
        return result;
    }
}