package org.deco.gachicoding.post.question.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.question.QuestionInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.post.question.QuestionDto;
import org.deco.gachicoding.post.question.application.dto.QuestionDtoAssembler;
import org.deco.gachicoding.post.question.application.dto.request.QuestionBasicRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionUpdateRequestDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.post.question.domain.vo.QuestionContents;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return questionRepository.save(createQuestion(questioner, dto)).getQueIdx();
    }

    private Question createQuestion(
            String questioner,
            QuestionDto.SaveRequestDto dto
    ) {
        User user = findUser(questioner);
        return QuestionDtoAssembler.question(user, dto);
    }

    @Transactional(readOnly = true)
    public QuestionDto.DetailResponseDto getQuestionDetail(Long queIdx) {

        Question question = findQuestion(queIdx);
        if (!question.getQueEnabled())
            throw new QuestionInactiveException();

        return QuestionDtoAssembler.questionResponseDto(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionListResponseDto> getQuestionList(
            String keyword,
            Pageable pageable
    ) {
        return null;
//        QuestionDtoAssembler.questionResponseDtos(
//                questionRepository.findAllQuestionByKeyword(keyword, pageable)
//        );
    }


    @Transactional
    public Long modifyQuestion(
            QuestionDto.UpdateRequestDto dto
    ) {
        Question question = findQuestion(dto.getQueIdx());

        if (!question.getQueEnabled())
            throw new QuestionInactiveException();

        QuestionContents updateContents = QuestionContents.builder()
                .queGeneralContent(dto.getQueGeneralContent())
                .queCodeContent(dto.getQueCodeContent())
                .queErrorContent(dto.getQueErrorContent())
                .build();

        question.update(dto.getQueTitle(), updateContents);
        return question.getQueIdx();
    }

    // 활성 -> 비활성
    @Transactional
    public void disableQuestion(QuestionBasicRequestDto dto) {
//        Question question = findQuestion(dto.getQueIdx());
//
//        User user = findAuthor(dto.getUserEmail());
//
//        question.hasSameAuthor(user);
//
//        if (question.getQueSolved())
//            throw new SolvedQuestionDisableFailedException();
//
//        question.disableQuestion();
    }

    @Transactional
    public void enableQuestion(QuestionBasicRequestDto dto) {
//        Question question = findQuestion(dto.getQueIdx());
//
//        User user = findAuthor(dto.getUserEmail());
//
//        question.hasSameAuthor(user);
//        question.enableQuestion();
    }

    @Transactional
    public void removeQuestion(QuestionBasicRequestDto dto) {
//        Question question = findQuestion(dto.getQueIdx());
//
//        User user = findAuthor(dto.getUserEmail());
//
//        question.hasSameAuthor(user);
//
//        if (question.getQueSolved())
//            throw new SolvedQuestionDeleteFailedException();
//
//        questionRepository.delete(question);
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
