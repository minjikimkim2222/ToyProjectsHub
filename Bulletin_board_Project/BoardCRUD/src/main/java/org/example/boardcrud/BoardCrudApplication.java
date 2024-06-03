package org.example.boardcrud;

import org.example.boardcrud.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BoardCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardCrudApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner test(BoardRepository boardRepository){
//        return args -> {
//          boardRepository.findAll().forEach(s -> System.out.println(s));
//        };
//    }
}
