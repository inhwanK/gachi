package org.deco.gachicoding.post.question.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.post.question.QuestionNotFoundException;
import org.deco.gachicoding.exception.user.UserNotFoundException;
import org.deco.gachicoding.exception.user.UserUnAuthorizedException;
import org.deco.gachicoding.post.question.application.dto.QuestionDtoAssembler;
import org.deco.gachicoding.post.question.application.dto.request.QuestionListRequestDto;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.question.domain.repository.QuestionRepository;
import org.deco.gachicoding.file.application.FileService;
import org.deco.gachicoding.tag.application.TagService;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.post.question.application.dto.response.QuestionDetailPostResponseDto;
import org.deco.gachicoding.post.question.application.dto.response.QuestionListResponseDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionSaveRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionUpdateRequestDto;
import org.springframework.data.domain.Page;
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
    public QuestionDetailPostResponseDto getQuestionDetail(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(QuestionNotFoundException::new);

        QuestionDetailPostResponseDto questionDetail = QuestionDetailPostResponseDto.builder()
                .question(question)
                .build();

//        fileService.getFiles(queIdx, BOARD_TYPE, questionDetail);
        tagService.getTags(queIdx, BOARD_TYPE, questionDetail);

        return questionDetail;
    }

    @Transactional
    public QuestionDetailPostResponseDto modifyQuestion(QuestionUpdateRequestDto dto) throws RuntimeException {
        Question question = questionRepository.findById(dto.getQueIdx())
                .orElseThrow(QuestionNotFoundException::new);

        // 작성자와 수정 시도하는 유저가 같은지 판별
        // 아마 제공되는 인증 로직이 있지 않을까 싶음.
        User user = userRepository.findByUserEmail(dto.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!isSameWriter(question, user)) {
            throw new UserUnAuthorizedException();
        }

        // null 문제 해결 못함
        question = question.update(dto);

        QuestionDetailPostResponseDto questionDetail = QuestionDetailPostResponseDto.builder()
                .question(question)
                .build();

        return questionDetail;
    }

    @Transactional
    public void disableQuestion(Long queIdx) {
        Question question = questionRepository.findByQueIdxAndQueActivatedTrue(queIdx)
                .orElseThrow(QuestionNotFoundException::new);

        question.isDisable();
//        return ResponseState.toResponseEntity(DISABLE_SUCCESS);
    }

    @Transactional
    public void enableQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(QuestionNotFoundException::new);

        question.isEnable();
//        return ResponseState.toResponseEntity(ENABLE_SUCCESS);
    }

    @Transactional
    public void removeQuestion(Long queIdx) {
        Question question = questionRepository.findById(queIdx)
                .orElseThrow(QuestionNotFoundException::new);

        questionRepository.delete(question);
//        return ResponseState.toResponseEntity(REMOVE_SUCCESS);
    }

    private Boolean isSameWriter(Question question, User user) {
        String writerEmail = question.getQuestioner().getUserEmail();
        String userEmail = user.getUserEmail();

        return (writerEmail.equals(userEmail)) ? true : false;
    }

    private User findAuthor(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
    }
}
