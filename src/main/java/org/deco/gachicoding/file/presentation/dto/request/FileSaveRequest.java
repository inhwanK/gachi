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
    @Email(message = "F0002")
    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "파일 리스트", required = true, example = "example.img")
    private List<MultipartFile> files;

    private FileSaveRequest() {}

    public FileSaveRequest(String userEmail, List<MultipartFile> files) {
        this.userEmail = userEmail;
        this.files = files;
    }
}
