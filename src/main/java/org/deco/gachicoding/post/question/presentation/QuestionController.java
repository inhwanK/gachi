package org.deco.gachicoding.post.question.presentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final QuestionService questionService;

    @ApiOperation(value = "질문 등록")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question")
    public ResponseEntity<Object> registerQuestion(
            @RequestBody @Valid QuestionSaveRequest request
    ) {
        log.info("{} Register Controller", "Question");

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Long queIdx = questionService.registerQuestion(
                QuestionAssembler.questionSaveRequestDto(userEmail, request)
        );

        String redirectUrl = String.format("/api/question/%d", queIdx);
        return ResponseEntity
                .created(URI.create(redirectUrl))
                .build();
    }

    @ApiOperation(value = "질문 리스트")
    @ApiResponse(code = 200, message = "질문 목록 반환")
    @GetMapping("/question/list")
    public ResponseEntity<List<QuestionListResponse>> getQuestionList(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @ApiIgnore @PageableDefault(size = 10) Pageable pageable
    ) {

        return ResponseEntity.ok(
                QuestionAssembler.questionListResponse(
                        questionService.getQuestionList(keyword, pageable)
                )
        );
    }

    @ApiOperation(value = "질문 디테일")
    @ApiResponse(code = 200, message = "질문 상세 정보 반환")
    @GetMapping("/question/{queIdx}")
    public ResponseEntity<QuestionDetailResponse> getQuestionDetail(
            @PathVariable Long queIdx
    ) {

        return ResponseEntity.ok(
                QuestionAssembler.questionDetailResponse(
                        questionService.getQuestionDetail(queIdx)
                )
        );
    }

    @ApiOperation(value = "질문 수정")
    @ApiResponse(code = 200, message = "수정 후 질문 상세 정보 반환")
    @PreAuthorize("isAuthenticated() and authentication.getName().equals()")
    @PatchMapping("/question/modify")
    public ResponseEntity<Object> modifyQuestion(
            @RequestBody @Valid QuestionUpdateRequest request
    ) {

        QuestionUpdateRequestDto dto = QuestionAssembler.questionUpdateRequestDto(request);

        Long queIdx = questionService.modifyQuestion(dto);

        String redirectUrl = String.format("/api/question/%d", queIdx);

        return ResponseEntity
                .created(URI.create(redirectUrl))
                .build();
    }

    @ApiOperation(value = "질문 비활성화")
    @ApiResponse(code = 200, message = "비활성화 성공")
    @PutMapping("/question/disable")
    public ResponseEntity<Void> disableQuestion(
            @RequestParam Long queIdx,
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto =
                QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.disableQuestion(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "질문 활성화")
    @ApiResponse(code = 200, message = "활성화 성공")
    @PutMapping("/question/enable")
    public ResponseEntity<Void> enableQuestion(
            @RequestParam Long queIdx,
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.enableQuestion(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "질문 삭제")
    @ApiResponse(code = 200, message = "삭제 성공")
    @DeleteMapping("/question")
    public void removeQuestion(
            @RequestParam Long queIdx,
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.removeQuestion(dto);
    }
}
