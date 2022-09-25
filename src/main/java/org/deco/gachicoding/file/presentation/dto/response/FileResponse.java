package org.deco.gachicoding.file.presentation.dto.response;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

public class FileResponse {

    @ApiModelProperty(value = "파일 리스트", required = true, example = "example.img")
    private List<String> urls;
}
