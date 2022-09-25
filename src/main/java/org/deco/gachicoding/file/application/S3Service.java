package org.deco.gachicoding.file.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.exception.file.UploadFailureException;
import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.deco.gachicoding.file.infrastructure.FileNameGenerator;
import org.deco.gachicoding.file.presentation.dto.request.FileSaveRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.temp.dir}")
    private String tempDir;

    public List<FileResponseDto> uploadStorage(FileSaveRequest request) throws IOException {
        List<MultipartFile> multipartFiles = request.getFiles();

        return multipartFiles.stream()
                .map(multipartFile -> upload(
                        multipartFile,
                        FileNameGenerator.uuid(multipartFile)
                )).collect(toList());
    }

    private FileResponseDto upload(MultipartFile multipartFile, String originFileName) {
        try {
            ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);

            String path = tempDir+originFileName;
            putS3(multipartFile, path, objectMetadata);

            return new FileResponseDto(multipartFile.getOriginalFilename(), getS3Url(path));
        } catch (Exception e) {
            e.printStackTrace();
            throw new UploadFailureException();
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        return objectMetadata;
    }

    private void putS3(MultipartFile multipartFile, String originFileName, ObjectMetadata objectMetadata) throws IOException {
        s3Client.putObject(
                bucket,
                originFileName,
                multipartFile.getInputStream(),
                objectMetadata
        );
    }

//        private String putS3(MultipartFile file, String saveFilePath) throws IOException {
//                // try catch 걸까?
//                // s3 저장
//                s3Client.putObject(new PutObjectRequest(bucket, saveFilePath, file.getInputStream(), null)
//                        .withCannedAcl(CannedAccessControlList.PublicRead));
//
//                return getS3Url(saveFilePath);
//        }

    public String replaceS3(String oldPath, String newPath) {
        String result = copyS3(oldPath, newPath);
        deleteS3(oldPath);
        return result;
    }

    private String copyS3(String oldPath, String newPath) {
        s3Client.copyObject(bucket, oldPath, bucket, newPath);

        return getS3Url(newPath);
    }

    private String getS3Url(String filePath) {
        // s3 객체 URL
        try {
            return URLDecoder.decode(s3Client.getUrl(bucket, filePath).toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //디코딩 실패 예외로 바꾸기
            throw new UploadFailureException();
        }
    }

    private void deleteS3(String source) {
        s3Client.deleteObject(bucket, source);
    }
}
