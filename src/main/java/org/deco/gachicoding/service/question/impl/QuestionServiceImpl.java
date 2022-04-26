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
    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestionDetailById(Long questionIdx) {
        Optional<Question> question = questionRepository.findById(questionIdx);
        QuestionResponseDto questionDetail = QuestionResponseDto.builder()
                .question(question.get())
                .build();
        return questionDetail;
    }

    // 리팩토링 - 검색 조건에 error도 추가
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> getQuestionListByKeyword(String keyword, int page) {
        Page<Question> questions = questionRepository.findByQsContentContainingIgnoreCaseAndQsActivatedTrueOrQsTitleContainingIgnoreCaseAndQsActivatedTrueOrderByQsIdxDesc(keyword, keyword, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "qsIdx")));
//        System.out.println("qContent : "+questions.getContent().get(0).getQsContent());
//        System.out.println("qTitle : "+questions.getContent().get(0).getQsTitle());
        Page<QuestionResponseDto> questionList = questions.map(
                result -> new QuestionResponseDto(result)
        );
        return questionList;
//        return null;
    }

    @Override
    @Transactional
    public Long registerQuestion(QuestionSaveRequestDto dto) {
        Question question = dto.toEntity();

        // findById() -> 실제로 데이터베이스에 도달하고 실제 오브젝트 맵핑을 데이터베이스의 행에 리턴한다. 데이터베이스에 레코드가없는 경우 널을 리턴하는 것은 EAGER로드 한것이다.
        // getOne ()은 내부적으로 EntityManager.getReference () 메소드를 호출한다. 데이터베이스에 충돌하지 않는 Lazy 조작이다. 요청된 엔티티가 db에 없으면 EntityNotFoundException을 발생시킨다.
        question.setUser(userRepository.getOne(dto.getUserIdx()));

        return questionRepository.save(question).getQsIdx();
    }

    @Override
    @Transactional
    public QuestionResponseDto modifyQuestionById(Long questionIdx, QuestionUpdateRequestDto dto) {

        Question question = questionRepository.findById(questionIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + questionIdx));

        question = question.update(dto.getQsTitle(), dto.getQsContent(), dto.getQsError(), dto.getQsCategory());

        QuestionResponseDto questionDetail = QuestionResponseDto.builder()
                .question(question)
                .build();

        return questionDetail;
    }

    @Override
    @Transactional
    public Long removeQuestion(Long questionIdx) {

        Question question = questionRepository.findById(questionIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. 글번호 = " + questionIdx));

        return question.isDisable().getQsIdx();
    }
}
