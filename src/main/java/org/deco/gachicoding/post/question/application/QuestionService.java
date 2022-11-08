package org.deco.gachicoding.post.question.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.question.QuestionInactiveException;
import org.deco.gachicoding.exception.post.question.QuestionNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.post.question.application.dto.QuestionDtoAssembler;
import org.deco.gachicoding.post.question.application.dto.request.QuestionBasicRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionUpdateRequestDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionDetailResponse;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionSaveRequestDto;
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
    private final FileService fileService;
    private final TagService tagService;

    @Transactional(rollbackFor = Exception.class)
    public Long registerQuestion(
            QuestionSaveRequestDto dto
    ) {

        Question question = questionRepository.save(createQuestion(dto));

        Long queIdx = question.getQueIdx();
        String queContent = question.getQueContents();

        question.updateContent(
                fileService.extractPathAndS3Upload(queIdx, "QUESTION", queContent)
        );

        return queIdx;
    }

    private Question createQuestion(QuestionSaveRequestDto dto) {
        User user = findAuthor(dto.getUserEmail());

        return QuestionDtoAssembler.question(user, dto);
    }

    @Transactional(readOnly = true)
    public List<QuestionListResponseDto> getQuestionList(
            String keyword,
            Pageable pageable
    ) {

        return QuestionDtoAssembler.questionResponseDtos(
                questionRepository.findAllQuestionByKeyword(
                        keyword,
                        pageable
                )
        );
    }


    @Transactional(readOnly = true)
    public QuestionDetailResponseDto getQuestionDetail(Long queIdx) {
        Question question = findQuestion(queIdx);

        if (!question.getQueLocked())
            throw new QuestionInactiveException();

        return QuestionDtoAssembler.questionResponseDto(question);
    }

    @Transactional
    public QuestionDetailResponseDto modifyQuestion(QuestionUpdateRequestDto dto) throws RuntimeException {
        String updateContents = fileService.compareFilePathAndOptimization(
                dto.getQueIdx(),
                "QUESTION",
                dto.getQueContents()
        );

        Question question = findQuestion(dto.getQueIdx());

        if (!question.getQueLocked())
            throw new QuestionInactiveException();

        User user = findAuthor(dto.getUserEmail());

        question.hasSameAuthor(user);

        question.update(
                dto.getQueTitle(),
                dto.getQueContents()
        );

        return QuestionDtoAssembler.questionResponseDto(question);
    }

    // 활성 -> 비활성
    @Transactional
    public void disableQuestion(QuestionBasicRequestDto dto) {
        Question question = findQuestion(dto.getQueIdx());

        User user = findAuthor(dto.getUserEmail());

        question.hasSameAuthor(user);

        question.disableQuestion();
    }

    @Transactional
    public void enableQuestion(QuestionBasicRequestDto dto) {
        Question question = findQuestion(dto.getQueIdx());

        User user = findAuthor(dto.getUserEmail());

        question.hasSameAuthor(user);

        question.enableQuestion();
    }

    @Transactional
    public void removeQuestion(QuestionBasicRequestDto dto) {
        Question question = findQuestion(dto.getQueIdx());

        User user = findAuthor(dto.getUserEmail());

        question.hasSameAuthor(user);

        questionRepository.delete(question);
    }

    private Question findQuestion(Long queIdx) {
        return questionRepository.findQuestionByIdx(queIdx)
                .orElseThrow(QuestionNotFoundException::new);
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
