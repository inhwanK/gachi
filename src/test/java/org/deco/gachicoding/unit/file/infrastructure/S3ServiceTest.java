package org.deco.gachicoding.unit.file.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.deco.gachicoding.common.factory.file.FileFactory;
import org.deco.gachicoding.common.factory.file.MockS3Object;
import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.deco.gachicoding.file.infrastructure.S3Service;
import org.deco.gachicoding.file.presentation.dto.request.FileSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3 amazonS3;

    @Test
    @DisplayName("파일 업로드 후 파일 이름과 URL을 반환한다.")
    void s3_store_success() throws IOException {

        List<MultipartFile> files = List.of(
                FileFactory.getTestSuccessImage1()
        );

        FileSaveRequest request = new FileSaveRequest(files);

        given(amazonS3.putObject(any(), any(), any(), any()))
                .willReturn(new PutObjectResult());

        given(amazonS3.getObject(anyString(), anyString()))
                .willReturn(MockS3Object.mockS3Object1());

        System.out.println(FileFactory.getTestSuccessImage1().getName());
        System.out.println(FileFactory.getTestSuccessImage1().getSize());
        System.out.println(FileFactory.getTestSuccessImage1().getOriginalFilename());

        List<FileResponseDto> responseDtos = s3Service.uploadStorage(request);

        for (FileResponseDto dto : responseDtos) {
            System.out.println(dto.getOriginFileName());
            System.out.println(dto.getFilePath());
        }

        verify(amazonS3, times(1))
                .putObject(any(), any(), any(), any());

    }
}
