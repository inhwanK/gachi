package org.deco.gachicoding.common.factory.post.question;

import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestionMock {

    private QuestionMock() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long queIdx;
        private User questioner;
        private List<Answer> answers = new ArrayList<>();
        private String queTitle = "QUESTION TITLE TEST";
        private QuestionContents queContents = QuestionContents.builder()
                .queGeneralContent("QUESTION CONTENTS TEST")
                .build();
        private Boolean queSolved = false;
        private Boolean queEnabled = true;
        private LocalDateTime queCreatedAt = LocalDateTime.now();
        private LocalDateTime queUpdatedAt = LocalDateTime.now();

        public Builder queIdx(Long queIdx) {
            this.queIdx = queIdx;
            return this;
        }

        public Builder questioner(User questioner) {
            this.questioner = questioner;
            return this;
        }

        public Builder answers(List<Answer> answers) {
            this.answers = answers;
            return this;
        }

        public Builder queTitle(String queTitle) {
            this.queTitle = queTitle;
            return this;
        }

        public Builder queContents(String general, String code, String error) {
            this.queContents = new QuestionContents(general, code, error);
            return this;
        }


        public Builder queSolved(Boolean queSolved) {
            this.queSolved = queSolved;
            return this;
        }

        public Builder queEnabled(Boolean queEnabled) {
            this.queEnabled = queEnabled;
            return this;
        }

        public Builder queCreatedAt(LocalDateTime queCreatedAt) {
            this.queCreatedAt = queCreatedAt;
            return this;
        }

        public Builder queUpdatedAt(LocalDateTime queUpdatedAt) {
            this.queUpdatedAt = queUpdatedAt;
            return this;
        }

        public Question build() {
            return Question.builder()
                    .queIdx(queIdx)
                    .questioner(questioner)
                    .queTitle(queTitle)
                    .queContents(queContents)
                    .queEnabled(queEnabled)
                    .queSolved(queSolved)
                    .queEnabled(queEnabled)
                    .queSolved(queSolved)
                    .queCreatedAt(queCreatedAt)
                    .queUpdatedAt(queUpdatedAt)
                    .build();
        }
    }
}
