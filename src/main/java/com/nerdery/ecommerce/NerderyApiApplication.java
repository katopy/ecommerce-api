package com.nerdery.ecommerce;

import com.nerdery.ecommerce.config.validation.AwsProperties;
import com.nerdery.ecommerce.config.validation.DBProperties;
import com.nerdery.ecommerce.config.validation.SecurityProperties;
import com.nerdery.ecommerce.config.validation.StripeProperties;
import com.nerdery.ecommerce.service.aws.S3Service;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Objects;

@SpringBootApplication
//@EnableConfigurationProperties({AwsProperties.class, DBProperties.class,  SecurityProperties.class})

public class NerderyApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(NerderyApiApplication.class, args);

	}

}
