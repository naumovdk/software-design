package stock;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Server.class);
        application.run(args);
    }
}
