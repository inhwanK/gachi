package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
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
import org.deco.gachicoding.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.deco.gachicoding.dto.response.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long registerQuestion(QuestionSaveRequestDto dto) {
        Question question = dto.toEntity();

        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.
        question.setUser(userRepository.getOne(dto.getUserIdx()));

        return questionRepository.save(question).getQueIdx();
    }

    // 리팩토링 - 검색 조건에 error도 추가
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionListResponseDto> getQuestionList(String keyword, Pageable pageable) {
        Page<Question> questions = questionRepository.findByQueContentContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(keyword, keyword, pageable);

        Page<QuestionListResponseDto> questionList = questions.map(
                result -> new QuestionListResponseDto(result)
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

        return questionDetail;
    }

    @Override
    @Transactional
    public QuestionDetailResponseDto modifyQuestion(Long userIdx, QuestionUpdateRequestDto dto) {
        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(() -> new CustomException(DATA_NOT_EXIST));

        // 작성자와 수정 시도하는 유저가 같은지 판별
        // 아마 제공되는 인증 로직이 있지 않을까 싶음.
        Optional<User> user = userRepository.findById(userIdx);
        if (question.getWriter().getUserIdx() != user.get().getUserIdx()) {
            return null;
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
}
