package com.nerdery.ecommerce.service.aws;

import com.nerdery.ecommerce.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class S3Test implements CommandLineRunner {
    private final S3Service s3Service;

    @Override
    public void run(String... args) throws Exception {
        s3Service.listBuckets();
    }
}
