package org.deco.gachicoding.file.presentation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class FileSaveRequest {

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "파일 리스트", required = true, example = "example.img")
    private List<MultipartFile> files;

    private FileSaveRequest() {}

    public FileSaveRequest(List<MultipartFile> files) {
        this.files = files;
    }
}
