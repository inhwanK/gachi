package org.deco.gachicoding.post.answer.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.answer.*;
import org.deco.gachicoding.exception.post.question.QuestionInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.file.domain.ArticleType;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerBasicRequestDto;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerSaveRequestDto;
import org.deco.gachicoding.post.answer.application.dto.request.AnswerUpdateRequestDto;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.answer.domain.repository.AnswerRepository;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional(rollbackFor = Exception.class)
    public Long registerAnswer(AnswerSaveRequestDto dto) {

        Answer answer = answerRepository.save(createAnswer(dto));

        Long ansIdx = answer.getAnsIdx();
        String ansContent = answer.getAnsContents();

        answer.update(
                fileService.extractPathAndS3Upload(ansIdx, ArticleType.ANSWER, ansContent)
        );

        return answer.getQueIdx();
    }

    private Answer createAnswer(AnswerSaveRequestDto dto) {
        User user = findAuthor(dto.getUserEmail());
        Question question = findQuestion(dto.getQueIdx());

        return dto.toEntity(user, question);
    }

    @Transactional
    public Long modifyAnswer(AnswerUpdateRequestDto dto) {
        String updateContents = fileService.compareFilePathAndOptimization(
                dto.getAnsIdx(),
                ArticleType.ANSWER,
                dto.getAnsContents()
        );

        Answer answer = findAnswer(dto.getAnsIdx());

        User user = findAuthor(dto.getUserEmail());

        answer.hasSameAuthor(user);

        if (!answer.getAnsLocked())
            throw new AnswerInactiveException();

        // 이미 채택 된 답변을 수정 할 수 없다.
        if (answer.getAnsSelected())
            throw new CheckedAnswerModifyFailedException();

        answer.update(updateContents);

        return answer.getQueIdx();
    }

    // 이부분 다시 한번 봐 주셈 (2022.11.09)
    @Transactional
    public Long selectAnswer(AnswerBasicRequestDto dto) {
        Answer answer = findAnswer(dto.getAnsIdx());

        Question question = answer.getQuestion();

        User requester = findAuthor(dto.getUserEmail());

        // 답변을 채택하는 사람은 질문을 작성한 작성자이기 때문에
        // 요청을 보낸 요청자와 질문의 작성자가 같아야 채택 가능
        // hasSameAuthor -> unAuthorizedCheck 같은 걸로 바꾸는게 나은 듯
        question.hasSameAuthor(requester);

        if (!answer.getAnsLocked())
            throw new AnswerInactiveException();

        if (!question.getQueEnabled())
            throw new QuestionInactiveException();

        answer.toSelect();

        question.toSolve();

        return question.getQueIdx();
    }

    @Transactional
    public void disableAnswer(AnswerBasicRequestDto dto) {
        Answer answer = findAnswer(dto.getAnsIdx());

        User user = findAuthor(dto.getUserEmail());

        answer.hasSameAuthor(user);

        // 채택 된 답변 비 활성 불가
        if (answer.getAnsSelected())
            throw new CheckedAnswerDisableFailedException();

        answer.disableAnswer();
    }

    @Transactional
    public void enableAnswer(AnswerBasicRequestDto dto) {
        Answer answer = findAnswer(dto.getAnsIdx());

        User user = findAuthor(dto.getUserEmail());

        answer.hasSameAuthor(user);

        answer.enableAnswer();
    }

    @Transactional
    public void removeAnswer(AnswerBasicRequestDto dto) {
        Answer answer = findAnswer(dto.getAnsIdx());

        User user = findAuthor(dto.getUserEmail());

        answer.hasSameAuthor(user);

        // 답변 채택 시 삭제 불가 -> 이건 생각을 좀 해봐야 할 듯?(2022-11-09)
        if (answer.getAnsSelected())
            throw new CheckedAnswerDeleteFailedException();

        answerRepository.delete(answer);
    }

    private Question findQuestion(Long queIdx) {
        return questionRepository.findQuestionByIdx(queIdx)
                .orElseThrow(QuestionNotFoundException::new);
    }

    private Answer findAnswer(Long ansIdx) {
        return answerRepository.findAnswerByIdx(ansIdx)
                .orElseThrow(AnswerNotFoundException::new);
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
