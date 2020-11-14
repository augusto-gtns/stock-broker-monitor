package hbm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("hbm.model*")
@EnableJpaRepositories("hbm.repository*")
@ComponentScan({ "hbm.service*", "hbm.router*", "hbm.processor*", "hbm.util*" })
public class HBMApplication {

	public static void main(String[] args) {
		SpringApplication.run(HBMApplication.class, args);
	}
}
