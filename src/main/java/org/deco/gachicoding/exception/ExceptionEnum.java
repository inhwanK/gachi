package org.deco.gachicoding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

    F0001("F0001", "널이어서는 안됩니다."),
    F0002("F0002", "올바른 형식의 아이디가 아닙니다."),
    F0004("F0004", "제한길이를 초과하였습니다.");

    private final String code;
    private final String detail;
}
