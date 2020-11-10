package hbm.router;

import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hbm.processor.DeleteFileProcessor;
import hbm.processor.HomeBrokerFileToDatatabaseProcessor;
import hbm.processor.ThrowableProcessor;
import hbm.util.HBMMonitorUtil;

@Component
public class HomeBrokerRouter extends RouteBuilder {

	public static final String NAME = "HomeBrokerRouter";

	@Autowired
	Logger log;

	@Value("${hbmonitor.hb.folder}")
	private String folder;
	
	@Override
	public void configure() throws Exception {
		String from = "file://" + HBMMonitorUtil.toRoutePath(folder) + "?noop=true";

		RouteDefinition route = from(from).routeId(NAME).log(LoggingLevel.INFO, log, "Running");

		route.onException(Throwable.class).process(ThrowableProcessor.NAME).handled(true);

		route.choice().when(isFileNameValid()).log(LoggingLevel.INFO, log, HomeBrokerFileToDatatabaseProcessor.NAME)
				.process(HomeBrokerFileToDatatabaseProcessor.NAME).otherwise().stop();
		
		// dedativado temporariamente
		//route.log(LoggingLevel.INFO, log, HomeBrokerFileSplitProcessor.NAME).process(HomeBrokerFileSplitProcessor.NAME);

		if (HBMMonitorUtil.isEnvPRD())
			route.log(LoggingLevel.INFO, log, DeleteFileProcessor.NAME).process(DeleteFileProcessor.NAME);

		route.log(LoggingLevel.INFO, log, "Finished");
	}

	private Predicate isFileNameValid() {
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				String fileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY);
				log.info("File '" + fileName + "' was founded by route");

				String regex = "collector-hb-\\d+\\.json";
				return Pattern.compile(regex).matcher(fileName).matches();
			}
		};
	}

}
