package top.seven.assistant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.seven.assistant.mapper")
public class CampusAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAssistantApplication.class, args);
    }

}
