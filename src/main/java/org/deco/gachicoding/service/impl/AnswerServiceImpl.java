package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.domain.answer.Answer;
import org.deco.gachicoding.domain.answer.AnswerRepository;
import org.deco.gachicoding.domain.board.Board;
import org.deco.gachicoding.domain.question.Question;
import org.deco.gachicoding.domain.question.QuestionRepository;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.AnswerService;
import org.deco.gachicoding.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Override
    @Transactional
    public Long registerAnswer(AnswerSaveRequestDto dto) {
        User writer = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.
//        entity.setQuestion(questionRepository.getOne(dto.getQueIdx()));

        Answer answer = answerRepository.save(dto.toEntity(writer, question));

        Long ansIdx = answer.getAnsIdx();
        String ansContent = answer.getAnsContent();

        try {
            answer.update(fileService.extractImgSrc(ansIdx, ansContent, "answer"));
            log.info("Success Upload Question Idx : {}", ansIdx);
        } catch (Exception e) {
            log.error("Failed To Extract {} File", "Answer Content");
            e.printStackTrace();
            removeAnswer(ansIdx);
        }

        return ansIdx;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnswerResponseDto> getAnswerList(String keyword, Pageable pageable) {
        Page<Answer> answers = answerRepository.findByAnsContentContainingIgnoreCaseAndAnsActivatedTrueOrderByAnsIdxDesc(keyword, pageable);
        Page<AnswerResponseDto> answersList = answers.map(
                result -> new AnswerResponseDto(result)
        );
        return answersList;
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerResponseDto getAnswerDetail(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
                .answer(answer)
                .build();
        return answerDetail;
    }

    @Override
    @Transactional
    public AnswerResponseDto modifyAnswer(AnswerUpdateRequestDto dto) {
        Answer answer = answerRepository.findById(dto.getAnsIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (isSameWriter(answer, user)) {
            return null;
        }

        answer = answer.update(dto.getAnsContent());

        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
                .answer(answer)
                .build();

        return answerDetail;
    }

    // 질문 작성자 확인 로직 추가
    @Override
    @Transactional
    public ResponseEntity<ResponseState> selectAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        Question question = answer.getQuestion();

        if(!question.getQueSolve()) {
            answer.toSelect();
            question.toSolve();
            return ResponseState.toResponseEntity(SELECT_SUCCESS);
        } else {
            return ResponseState.toResponseEntity(ALREADY_SOLVE);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> disableAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        answer.disableAnswer();
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        answer.enableAnswer();
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        answerRepository.delete(answer);
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Answer answer, User user) {
        Long writerIdx = answer.getWriter().getUserIdx();
        Long userIdx = user.getUserIdx();

        return (writerIdx.equals(userIdx)) ? true : false;
    }
}
