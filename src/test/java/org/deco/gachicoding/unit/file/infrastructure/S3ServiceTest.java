package org.deco.gachicoding.unit.file.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import org.deco.gachicoding.file.infrastructure.S3Service;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3 amazonS3;


}
