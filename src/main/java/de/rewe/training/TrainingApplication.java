package de.rewe.training;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info =
                @Info(
                        title = "rewe-training",
                        version = "0.0.1",
                        description =
                                "Exercise project for the Base AI Training. "
                                        + "The product catalogue works. POST /api/returns does not — building it is the exercise."))
public class TrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingApplication.class, args);
    }
}
