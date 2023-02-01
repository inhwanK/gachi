package org.deco.gachicoding.unit.file.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.deco.gachicoding.common.factory.file.FileMockFactory;
import org.deco.gachicoding.exception.file.UploadFailureException;
import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.deco.gachicoding.file.infrastructure.S3Service;
import org.deco.gachicoding.file.presentation.dto.request.FileSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.url}")
    private String filePath = "https://amazonaws.s3bucket/";

    @Test
    @DisplayName("파일 업로드 후 파일 이름과 URL을 반환한다.")
    void s3_store_success() throws IOException {

        // given
        List<MultipartFile> files = List.of(
                FileMockFactory.getTestSuccessImage1()
        );

        given(amazonS3.putObject(any(), any(), any(), any()))
                .willReturn(new PutObjectResult());

        given(amazonS3.getUrl(any(), any()))
                .willReturn(new URL(filePath));

        List<FileResponseDto> responseDtos = List.of(
                FileMockFactory.getTestSuccessImage1Dto(filePath)
        );

        // when
        List<FileResponseDto> fileResponseDtos = s3Service.uploadStorage(new FileSaveRequest(files));

        // then
        assertThat(fileResponseDtos)
                .usingRecursiveComparison()
                .isEqualTo(responseDtos);

        verify(amazonS3, times(1))
                .putObject(any(), any(), any(), any());

        verify(amazonS3, times(1))
                .getUrl(any(), any());
    }

    @Test
    @DisplayName("복수의 이미지 파일을 업로드 할 때 한 장이라도 실패하면 예외가 발생한다.")
    void s3_store_failure() {

        // given
        List<MultipartFile> files = List.of(
                FileMockFactory.getTestSuccessImage1(),
                FileMockFactory.getTestSuccessImage2()
        );

        given(amazonS3.putObject(any(), any(), any(), any()))
                .willThrow(RuntimeException.class);

        // when, then
        assertThatCode(() -> s3Service.uploadStorage(new FileSaveRequest(files)))
                .isInstanceOf(UploadFailureException.class)
                .extracting("message")
                .isEqualTo("업로드 실패");

        verify(amazonS3, times(1))
                .putObject(any(), any(), any(), any());
    }
}
