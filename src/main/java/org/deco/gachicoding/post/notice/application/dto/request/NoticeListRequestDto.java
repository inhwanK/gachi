package org.deco.gachicoding.post.notice.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class NoticeListRequestDto {
    @NotNull
    @ApiModelProperty(value = "검색어", required = true, example = "운영")
    private String keyword;

    @NotNull
    private Pageable pageable;
}
