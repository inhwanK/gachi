package org.deco.gachicoding.post.question.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.deco.gachicoding.post.question.application.dto.request.QuestionBasicRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionUpdateRequestDto;
import org.deco.gachicoding.post.question.presentation.dto.QuestionAssembler;
import org.deco.gachicoding.post.question.presentation.dto.request.QuestionSaveRequest;
import org.deco.gachicoding.post.question.presentation.dto.request.QuestionUpdateRequest;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionDetailResponse;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@Api(tags = "가치질문 정보 처리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class QuestionController {

    private static final String REDIRECT_URL = "/api/question/%d";

    private final QuestionService questionService;

    @ApiOperation(value = "질문 등록", notes = "하나의 질문 데이터를 등록.")
    @ApiResponse(code = 200, message = "등록된 질문 번호 반환")
    @PostMapping("/question")
    public ResponseEntity<Void> registerQuestion(
            @ApiParam(value = "질문 요청 body 정보")
            @RequestBody @Valid QuestionSaveRequest request
    ) {
        log.info("{} Register Controller", "Question");

        Long queIdx = questionService.registerQuestion(
                QuestionAssembler.questionSaveRequestDto(request)
        );

        String redirectUrl = String.format(REDIRECT_URL, queIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "질문 리스트", notes = "여러 개의 질문 데이터 응답. 이 때, 질문별 답변 데이터는 포함하지 않음.")
    @ApiResponse(code = 200, message = "질문 목록 반환")
    @GetMapping("/question/list")
    public ResponseEntity<List<QuestionListResponse>> getQuestionList(
            @ApiParam(value = "검색어") @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @ApiIgnore @PageableDefault(size = 10) Pageable pageable
    ) {

        return ResponseEntity.ok(
                QuestionAssembler.questionListResponse(
                        questionService.getQuestionList(keyword, pageable)
                )
        );
    }

    @ApiOperation(value = "질문 디테일", notes = "하나의 질문 데이터와 해당 질문의 답변들을 응답")
    @ApiResponse(code = 200, message = "질문 상세 정보 반환")
    @GetMapping("/question/{queIdx}")
    public ResponseEntity<QuestionDetailResponse> getQuestionDetail(
            @ApiParam(value = "질문 번호", example = "1")
            @PathVariable Long queIdx
    ) {

        return ResponseEntity.ok(
                QuestionAssembler.questionDetailResponse(
                        questionService.getQuestionDetail(queIdx)
                )
        );
    }

    @ApiOperation(value = "질문 수정", notes = "질문 데이터를 수정 (리팩토링 필요함)")
    @ApiResponse(code = 200, message = "수정 후 질문 상세 정보 반환")
    @PutMapping("/question/modify")
    public ResponseEntity<Void> modifyQuestion(
            @ApiParam(value = "질문 수정 요청 body 정보")
            @RequestBody @Valid QuestionUpdateRequest request) {

        QuestionUpdateRequestDto dto = QuestionAssembler.questionUpdateRequestDto(request);

        Long queIdx = questionService.modifyQuestion(dto);

        String redirectUrl = String.format(REDIRECT_URL, queIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "질문 비활성화", notes = "사용자 입장에서 질문 데이터를 삭제")
    @ApiResponse(code = 200, message = "비활성화 성공")
    @PutMapping("/question/disable")
    public ResponseEntity<Void> disableQuestion(
            @ApiParam(value = "질문 번호", example = "1") @RequestParam Long queIdx,
            @ApiParam(value = "userEmail")
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.disableQuestion(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "질문 활성화", notes = "사용자 입장에서 삭제된 질문 데이터 복구")
    @ApiResponse(code = 200, message = "활성화 성공")
    @PutMapping("/question/enable")
    public ResponseEntity<Void> enableQuestion(
            @ApiParam(value = "질문 번호", example = "1")
            @RequestParam Long queIdx,
            @ApiParam(value = "userEmail")
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.enableQuestion(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "질문 삭제", notes = "질문 데이터를 DB에서 완전히 삭제")
    @ApiResponse(code = 200, message = "삭제 성공")
    @DeleteMapping("/question")
    public ResponseEntity<Void> removeQuestion(
            @ApiParam(value = "질문 번호", example = "1")
            @RequestParam Long queIdx,
            @ApiParam(value = "userEmail")
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.removeQuestion(dto);

        return ResponseEntity.noContent().build();
    }
}
