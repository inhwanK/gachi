package org.deco.gachicoding;

import org.deco.gachicoding.user.presentation.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

//@EnableJpaAuditing
@SpringBootTest(properties = {
        "cloud.aws.s3.temp.dir",
        "cloud.aws.s3.url.baseUrl",
        "cloud.aws.s3.url.format",
        "key.value",
        "cloud.aws.credentials.accessKey",
        "cloud.aws.credentials.secretKey",
        "cloud.aws.region.static",
        "cloud.aws.s3.bucket"
})
class GachicodingApplicationTests {

    @Test
    void contextLoads() {
    }

}
