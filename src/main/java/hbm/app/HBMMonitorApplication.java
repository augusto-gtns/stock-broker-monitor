package hbm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import hbm.util.Icons;

@SpringBootApplication
@EntityScan("hbmonitor.model*")
@EnableJpaRepositories("hbmonitor.repository*")
@ComponentScan({ "hbmonitor.service*", "hbmonitor.router*", "hbmonitor.processor*", "hbmonitor.util*" })
public class HBMMonitorApplication extends HBMBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(HBMMonitorApplication.class, args);
		log.info(Icons.ARROW_UP.getUnicode()
				+ "...............................................................................");
	}

}
