package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.question.Question;
import org.deco.gachicoding.domain.question.QuestionRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.question.QuestionDetailResponseDto;
import org.deco.gachicoding.dto.question.QuestionListResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.FileService;
import org.deco.gachicoding.service.QuestionService;
import org.deco.gachicoding.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final TagService tagService;
    private final String BOARD_TYPE = "QUESTION";

    @Override
    @Transactional
    public Long registerQuestion(QuestionSaveRequestDto dto) throws Exception {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Question question = questionRepository.save(dto.toEntity(writer));

        Long queIdx = question.getQueIdx();
        String queContent = question.getQueContent();
        String queError = question.getQueError();

        if(dto.getTags() != null)
            tagService.registerBoardTag(queIdx, dto.getTags(), BOARD_TYPE);

        try {
            question.updateContent(fileService.extractImgSrc(queIdx, queContent, BOARD_TYPE));
            question.updateError(fileService.extractImgSrc(queIdx, queError, BOARD_TYPE));
            log.info("Success Upload Question Idx : {}", queIdx);
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Question Content");
            e.printStackTrace();
            removeQuestion(queIdx);
            tagService.removeBoardTags(queIdx, BOARD_TYPE);
            throw e;
        }

        return queIdx;
    }

    // 리팩토링 - 검색 조건에 error도 추가
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionListResponseDto> getQuestionList(String keyword, Pageable pageable) {
        Page<Question> questions = questionRepository.findByQueContentContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(keyword, keyword, pageable);

        Page<QuestionListResponseDto> questionList = questions.map(
                result -> new QuestionListResponseDto(result)
        );

        questionList.forEach(
                questionListResponseDto ->
                        tagService.getTags(questionListResponseDto.getQueIdx(), BOARD_TYPE, questionListResponseDto)
        );

        return questionList;
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDetailResponseDto getQuestionDetail(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        QuestionDetailResponseDto questionDetail = QuestionDetailResponseDto.builder()
                .question(question)
                .build();

//        fileService.getFiles(queIdx, BOARD_TYPE, questionDetail);
        tagService.getTags(queIdx, BOARD_TYPE, questionDetail);

        return questionDetail;
    }

    @Override
    @Transactional
    public QuestionDetailResponseDto modifyQuestion(QuestionUpdateRequestDto dto) throws RuntimeException {
        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        // 작성자와 수정 시도하는 유저가 같은지 판별
        // 아마 제공되는 인증 로직이 있지 않을까 싶음.
        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!isSameWriter(question, user)) {
            throw new CustomException(INVALID_AUTH_USER);
        }

        // null 문제 해결 못함
        question = question.update(dto);

        QuestionDetailResponseDto questionDetail = QuestionDetailResponseDto.builder()
                .question(question)
                .build();

        return questionDetail;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> disableQuestion(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        question.isDisable();
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        question.isEnable();
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        questionRepository.delete(question);
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Question question, User user) {
        Long writerIdx = question.getWriter().getUserIdx();
        Long userIdx = user.getUserIdx();

        return (writerIdx.equals(userIdx)) ? true : false;
    }
}
