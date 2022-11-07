package org.deco.gachicoding.post.question.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSaveRequestDto {

    private String userEmail;

    private String queTitle;

    private String queContents;

//    @Nullable
//    @ApiModelProperty(value = "태그 목록", required = false, example = "Java")
//    private List<String> tags;

    @Builder
    public QuestionSaveRequestDto(
            String userEmail,
            String queTitle,
            String queContents
    ) {
        this.userEmail = userEmail;
        this.queTitle = queTitle;
        this.queContents = queContents;
    }

    public Question toEntity(User writer) {
        return Question.builder()
                .questioner(writer)
                .queTitle(queTitle)
                .queContents(queContents)
                .build();
    }
}
