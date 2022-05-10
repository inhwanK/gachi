package org.deco.gachicoding.service.question.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.question.Question;
import org.deco.gachicoding.domain.question.QuestionRepository;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.question.QuestionResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.deco.gachicoding.service.question.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Page<QuestionResponseDto> getQuestionList(String keyword, Pageable pageable) {
        Page<Question> questions = questionRepository.findByQueContentContainingIgnoreCaseAndQueActivatedTrueOrQueTitleContainingIgnoreCaseAndQueActivatedTrueOrderByQueIdxDesc(keyword, keyword, pageable);

        Page<QuestionResponseDto> questionList = questions.map(
                result -> new QuestionResponseDto(result)
        );

        return questionList;
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestionDetail(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + queIdx));

        QuestionResponseDto questionDetail = QuestionResponseDto.builder()
                .question(question)
                .build();

        return questionDetail;
    }

    @Override
    @Transactional
    public QuestionResponseDto modifyQuestion(QuestionUpdateRequestDto dto) {
        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + dto.getQueIdx()));

        question = question.update(dto.getQueTitle(), dto.getQueContent(), dto.getQueError(), dto.getQueCategory());

        QuestionResponseDto questionDetail = QuestionResponseDto.builder()
                .question(question)
                .build();

        return questionDetail;
    }

    @Override
    @Transactional
    public void disableQuestion(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + queIdx));

        question.isDisable();
    }

    @Override
    @Transactional
    public void enableQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + queIdx));

        question.isEnable();
    }

    @Override
    @Transactional
    public Long removeQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + queIdx));

        questionRepository.delete(question);
        return queIdx;
    }
}
