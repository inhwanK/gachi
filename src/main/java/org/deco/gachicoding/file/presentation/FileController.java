package org.deco.gachicoding.file.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.file.infrastructure.S3Service;
import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.deco.gachicoding.file.presentation.dto.request.FileSaveRequest;
import org.deco.gachicoding.file.presentation.dto.request.ImageFileValid;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api(tags = "파일 처리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @ApiOperation(value = "파일 임시 저장")
    @ApiResponses(
            @ApiResponse(code = 200, message = "임시 폴더의 파일 URL 반환")
    )
    @PostMapping("/file/upload")
    public List<FileResponseDto> fileUploadImageFile(@ApiParam(value = "멀티파트 파일") @ImageFileValid FileSaveRequest request) throws IOException {

        return s3Service.uploadStorage(request);
    }
}
