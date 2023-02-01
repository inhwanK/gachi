package org.deco.gachicoding.common.factory.file;

import com.amazonaws.services.s3.model.S3Object;

public class S3ObjectMock {
    private S3ObjectMock() {}


    public static S3Object mockS3Object1() {
        return new S3Object();
    }

}
