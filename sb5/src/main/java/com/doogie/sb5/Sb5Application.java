package com.doogie.sb5;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class Sb5Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext c = SpringApplication.run(Sb5Application.class, "a", "b");
    }

    @Bean
    public CommandLineRunner run() {
        return args -> System.out.println("run()...");
    }

    @Bean
    public ApplicationRunner run2() {
        return args -> {
            System.out.println("run2()...");

            for (String arg : args.getSourceArgs()) {
                System.out.println(arg);
            }
        };
    }

    // HTTP Request: request line, header, Body
    // HTTP Response: status code, header, Body -> return value

    @RestController
    public static class MyController {
        @RequestMapping("/hello")
        public String hello() {
            return "<h1>hello</h1>";
        }

        @RequestMapping("/reactive")
        public Mono<String> reactive() {
            return Mono
                    .just("<h1>Hello Reactive</h1>")
                    .map(String::toUpperCase)
                    .publishOn(Schedulers.newSingle("publishOn"))
                    .log();
        }

        @RequestMapping("/future")
        public CompletableFuture<String> future() {
            return CompletableFuture
                    .supplyAsync(() -> "Hello World")
                    .thenApply(String::toUpperCase);
        }
    }
}
