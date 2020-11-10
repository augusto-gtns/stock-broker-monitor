package hbm.app;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

public abstract class HBMBaseApplication {

	protected final static Logger log = LoggerFactory.getLogger(HBMBaseApplication.class);

	@Value("${hb.timezone:}")
	private String timezone;

	@PostConstruct
	public void init() {

		if (!StringUtils.isEmpty(timezone))
			TimeZone.setDefault(TimeZone.getTimeZone(timezone));

		log.info("Application timezone is '" + TimeZone.getDefault().toZoneId() + " | E.g.: '" + new Date() + "'");
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public Logger log(InjectionPoint injectionPoint) {
		return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
}
