package org.deco.gachicoding.unit.post.question.domain;

import org.deco.gachicoding.common.factory.post.question.QuestionMock;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class QuestionTest {

    User questioner = UserMock.builder().build();

    @Test
    @DisplayName("질문의 제목을 수정한다.")
    public void update_Question_Content_Success() {
        Question question = QuestionMock.builder()
                .questioner(questioner)
//                .queTitle("변경 전 제목")
                .build();
        fail("미구현");
    }
}
