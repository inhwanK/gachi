package org.deco.gachicoding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    SAVE_SUCCESS(OK, "저장 성공"),
    REMOVE_SUCCESS(OK, "삭제 성공"),
    DISABLE_SUCCESS(OK, "비활성화 성공"),
    ENABLE_SUCCESS(OK, "활성화 성공"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 오류. 웹 서버가 요청사항을 수행할 수 없을 경우 발생  */
    RESOURCE_NOT_EXIST(INTERNAL_SERVER_ERROR, "데이터가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
