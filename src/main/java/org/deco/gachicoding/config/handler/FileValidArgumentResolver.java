package org.deco.gachicoding.config.handler;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.file.domain.File;
import org.deco.gachicoding.file.presentation.dto.request.FileSaveRequest;
import org.deco.gachicoding.file.presentation.dto.request.FileValid;
import org.deco.gachicoding.file.presentation.dto.request.FileValidator;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class FileValidArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String FILE_NAME = "files";
    private static final String USER_EMAIL = "userEmail";

    private final List<FileValidator> fileValidators;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FileValid.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        MultipartHttpServletRequest multipartHttpServletRequest = getMultipartHttpServletRequest(webRequest);

        List<MultipartFile> files = multipartHttpServletRequest.getFiles(FILE_NAME);
        files.forEach(this::validateIsRightFile);

        String userEmail = (String) multipartHttpServletRequest.getAttribute(USER_EMAIL);

        return new FileSaveRequest(userEmail, files);
    }

    private void validateIsRightFile(MultipartFile multipartFile) {
        fileValidators.forEach(fileValidator -> fileValidator.execute(multipartFile));
    }

    private MultipartHttpServletRequest getMultipartHttpServletRequest(
            NativeWebRequest webRequest
    ) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Objects.requireNonNull(httpServletRequest);

        return (MultipartHttpServletRequest) httpServletRequest;
    }
}
