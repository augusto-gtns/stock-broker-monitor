package sbm;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("sbm.model")
@EnableJpaRepositories("sbm.repository")
@ComponentScan({ "sbm.service", "sbm.router", "sbm.processor", "sbm.util" })
public class SBMApplication {

	public static void main(String[] args) {
		// SpringApplication.run(SBMApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SBMApplication.class);
	    builder.headless(false);
	    ConfigurableApplicationContext context = builder.run(args);
	}
}
