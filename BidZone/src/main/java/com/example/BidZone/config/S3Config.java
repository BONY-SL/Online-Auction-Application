package com.example.BidZone.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@PropertySource("classpath:s3config.properties")
public class S3Config {
    @Value("${bucketName}")
    private String bucketName;

    @Value("${bucketRegion}")
    private String bucketRegion;

    @Bean
    public AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();
    }
}
