package org.deco.gachicoding.file.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.file.S3CopyException;
import org.deco.gachicoding.exception.file.UploadFailureException;
import org.deco.gachicoding.exception.file.UtfDecodingException;
import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.deco.gachicoding.file.presentation.dto.request.FileSaveRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<FileResponseDto> uploadStorage(FileSaveRequest request) {
        List<MultipartFile> multipartFiles = request.getFiles();

        return multipartFiles.stream()
                .map(multipartFile -> upload(
                        multipartFile,
                        FileNameSupporter.uuid(multipartFile)
                )).collect(toList());
    }

    private FileResponseDto upload(MultipartFile multipartFile, String saveFileName) {
        try {
            ObjectMetadata objectMetadata = createObjectMetadata(multipartFile, saveFileName);

            String path = String.format("TEMP/%s", saveFileName);
            log.info("s3Path : " + path);
            putS3(multipartFile, path, objectMetadata);

            return new FileResponseDto(multipartFile.getOriginalFilename(), getS3Url(path));
        } catch (Exception e) {
            e.printStackTrace();
            throw new UploadFailureException();
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile multipartFile, String saveFileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        log.info("size = {}", multipartFile.getSize());
        log.info("contentType = {}", multipartFile.getContentType());
        log.info("originalFileName = {}", multipartFile.getOriginalFilename());
        log.info("saveFileName = {}", saveFileName);
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.addUserMetadata("OriginalFileName", multipartFile.getOriginalFilename());
        objectMetadata.addUserMetadata("SaveFileName", saveFileName);

        return objectMetadata;
    }

    public ObjectMetadata getObjectMetadata(String filePath) {
        // not found 예외 추가
        return s3Client.getObjectMetadata(bucket, filePath);
    }

    private void putS3(MultipartFile multipartFile, String originFileName, ObjectMetadata objectMetadata) throws IOException {
        log.info("bucket : {}", bucket);
        log.info("originFileName : {}", originFileName);
        log.info("multipartFile.getInputStream() : {}", multipartFile.getInputStream());
        log.info("objectMetadata : {}", objectMetadata);
        s3Client.putObject(
                bucket,
                originFileName,
                multipartFile.getInputStream(),
                objectMetadata
        );
    }

    public void replaceS3(String oldPath, String newPath) {
        copyS3(oldPath, newPath);
        deleteS3(oldPath);
    }

    private void copyS3(String oldPath, String newPath) {
        try {
            s3Client.copyObject(bucket, oldPath, bucket, newPath);
        } catch (Exception/*AmazonS3Exception*/ e) {
            throw new S3CopyException();
        }
    }

    private String getS3Url(String filePath) {
        // s3 객체 URL
        try {
            return URLDecoder.decode(s3Client.getUrl(bucket, filePath).toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UtfDecodingException();
        }
    }

    private void deleteS3(String filePath) {
        s3Client.deleteObject(bucket, filePath);
    }
}
