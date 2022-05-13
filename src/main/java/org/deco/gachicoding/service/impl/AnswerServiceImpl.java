package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.answer.Answer;
import org.deco.gachicoding.domain.answer.AnswerRepository;
import org.deco.gachicoding.domain.question.QuestionRepository;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.deco.gachicoding.dto.response.CustomException;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long registerAnswer(AnswerSaveRequestDto dto) {
        Answer answer = dto.toEntity();

        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.
        answer.setUser(userRepository.getOne(dto.getUserIdx()));

        answer.setQuestion(questionRepository.getOne(dto.getQueIdx()));

        return answerRepository.save(answer).getAnsIdx();
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
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
                .answer(answer)
                .build();
        return answerDetail;
    }

    @Override
    @Transactional
    public AnswerResponseDto modifyAnswer(AnswerUpdateRequestDto dto) {
        Answer answer = answerRepository.findById(dto.getAnsIdx())
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        answer = answer.update(dto.getAnsContent());

        AnswerResponseDto answerDetail = AnswerResponseDto.builder()
                .answer(answer)
                .build();

        return answerDetail;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> disableAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        answer.disableAnswer();
        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> enableAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        answer.enableAnswer();
        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseState> removeAnswer(Long ansIdx) {
        Answer answer = answerRepository.findById(ansIdx)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_EXIST));

        answerRepository.delete(answer);
        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }
}
