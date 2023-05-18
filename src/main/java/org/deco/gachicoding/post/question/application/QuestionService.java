package org.deco.gachicoding.post.question.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.question.QuestionAlreadyActiveException;
import org.deco.gachicoding.exception.post.question.QuestionAlreadyInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.post.question.application.dto.QuestionDto;
import org.deco.gachicoding.post.question.application.dto.QuestionAssembler;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long registerQuestion(
            String questioner,
            QuestionDto.SaveRequestDto dto
    ) {
        return questionRepository.save(createQuestion(questioner, dto))
                .getQueIdx();
    }

    private Question createQuestion(
            String questioner,
            QuestionDto.SaveRequestDto dto
    ) {
        User user = findUser(questioner);
        return QuestionAssembler.question(user, dto);
    }

    @Transactional(readOnly = true)
    public QuestionDto.DetailResponseDto getQuestionDetail(Long queIdx) {

        Question question = findQuestion(queIdx);
        if (!question.getQueEnabled())
            throw new QuestionInactiveException();

        return QuestionAssembler.questionResponseDto(question);
    }

    @Transactional(readOnly = true)
    public Page<QuestionDto.ListResponseDto> searchQuestionByKeyword(
            String keyword,
            Pageable pageable
    ) {
        return questionRepository.retrieveQuestionByKeyword(keyword, pageable)
                .map(entity -> QuestionAssembler.questionListResponseDto(entity));
    }

    @Transactional(readOnly = true)
    public Page<QuestionDto.ListResponseDto> searchQuestionFullText(
            String keyword,
            Pageable pageable
    ) {
        return questionRepository.retrieveQuestionFullText(keyword, pageable)
                .map(entity -> QuestionAssembler.questionListResponseDto(entity));
    }

    @Transactional
    public Long modifyQuestion(
            QuestionDto.UpdateRequestDto dto
    ) {
        Question question = findQuestion(dto.getQueIdx());

        if (!question.getQueEnabled())
            throw new QuestionInactiveException();

        question.update(
                dto.getQueTitle(),
                QuestionAssembler.questionContents(dto)
        );

        return question.getQueIdx();
    }


    @Transactional
    public Long disableQuestion(Long queIdx) {
        Question question = findQuestion(queIdx);
        if(!question.getQueEnabled())
            throw new QuestionAlreadyInactiveException();

        question.disableQuestion();
        return question.getQueIdx();
    }

    @Transactional
    public Long enableQuestion(Long queIdx) {
        Question question = findQuestion(queIdx);
        if(question.getQueEnabled())
            throw new QuestionAlreadyActiveException();

        question.enableQuestion();
        return question.getQueIdx();
    }

    @Transactional
    public void removeQuestion(Long queIdx) {
        Question question = findQuestion(queIdx);
        questionRepository.delete(question);
    }

    private Question findQuestion(Long queIdx) {
        return questionRepository.findById(queIdx)
                .orElseThrow(QuestionNotFoundException::new);
    }

    private User findUser(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
