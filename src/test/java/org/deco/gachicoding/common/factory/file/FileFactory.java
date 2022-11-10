package org.deco.gachicoding.common.factory.file;

import org.deco.gachicoding.file.application.dto.response.FileResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FileFactory {

    /**
     * getClass().getResource() vs getClass().getClassLoader().getResource()
     * <br><br>
     * getClass().getResource(): 호출한 클래스 패키지 기준으로 상대적인 리소스 경로
     * getClass().getClassLoader().getResource(): 호출한 호출한 클래스 패키지 기준으로 절대적인 리소스 경로를 탐색한다.
     * <br><br>
     * 참고 : http://okminseok.blogspot.com/2019/08/getresource-vs-getclassloadergetresource.html
     */
    private static final ClassLoader classLoader = FileFactory.class.getClassLoader();

    public static MockMultipartFile getTestSuccessImage1() {
        return createMockMultipartFile("testSuccessImage1.png");
    }

    public static FileResponseDto getTestSuccessImage1Dto(String filePath) {
        return FileResponseDto.builder()
                .filePath(filePath)
                .saveFileName("testSuccessImage1.png")
                .build();
    }

    public static MockMultipartFile getTestSuccessImage2() {
        return createMockMultipartFile("testSuccessImage2.png");
    }

    private static File createFile(String testImage) {
        URL resource = classLoader.getResource(testImage);
        Objects.requireNonNull(resource);

        return new File(resource.getFile());
    }

    private static MockMultipartFile createMockMultipartFile(String testImage) {
        File file = createFile(testImage);

        try {
            return new MockMultipartFile(
                    "files",
                    testImage,
                    "image/png",
                    new FileInputStream(file)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
