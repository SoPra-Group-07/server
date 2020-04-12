package ch.uzh.ifi.seal.soprafs20;

import ch.uzh.ifi.seal.soprafs20.controller.CardController;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@RestController
@SpringBootApplication        //Tells SpringBoot that this is the starting point of our application
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);   //Static method (run) for starting springboot
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String helloWorld() {
        return "The application is running.";
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }
}
