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

        for(MultipartFile f : files) {
            origFileName = f.getOriginalFilename();
            origFileExtension = origFileName.substring(origFileName.lastIndexOf("."));
            tamperingFileName = UUID.randomUUID().toString();
            saveFileName = tamperingFileName + origFileExtension;
            fileSize = f.getSize();

//                System.out.println("origFileName : " + origFileName);
//                System.out.println("origFileExtension : " + origFileExtension);
//                System.out.println("tamperingFileName : " + tamperingFileName);
//                System.out.println("saveFileName : " + saveFileName);
//                System.out.println("fileSize : " + fileSize);

            s3Client.putObject(new PutObjectRequest(bucket, saveFileName, f.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

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

            fileService.saveFile(dto);

//                System.out.println("filePath : " + filePath);
        }
    }

    public void readObject(String filePath) throws IOException {
        S3Object o = s3Client.getObject(bucket, filePath);

    }
}