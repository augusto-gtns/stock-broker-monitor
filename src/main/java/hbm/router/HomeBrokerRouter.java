package hbm.router;

import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hbm.processor.DeleteFileProcessor;
import hbm.processor.FilePersistenceProcessor;
import hbm.processor.NotifierProcessor;
import hbm.processor.ThrowableProcessor;
import hbm.util.HBMMonitorUtil;

@Component
public class HomeBrokerRouter extends RouteBuilder {

	public static final String NAME = "HomeBrokerRouter";
	public static final String STOCK_LIST_HEADER = "HomeBrokerRouter";

	@Value("${hbm.folder:0}")
	private String folder;

	@Override
	public void configure() throws Exception {
		RouteDefinition route = from("file://" + HBMMonitorUtil.toRoutePath(folder) + "?noop=true").routeId(HomeBrokerRouter.NAME)
				.log(LoggingLevel.INFO, log, "Started");

		route.onException(Throwable.class).process(ThrowableProcessor.NAME).handled(true);

		route.choice().when(isFileNameValid()).log(LoggingLevel.INFO, log, FilePersistenceProcessor.NAME)
				.process(FilePersistenceProcessor.NAME).otherwise().stop();

		route.log(LoggingLevel.INFO, log, NotifierProcessor.NAME).process(NotifierProcessor.NAME);
		
		route.log(LoggingLevel.INFO, log, DeleteFileProcessor.NAME).process(DeleteFileProcessor.NAME);

		route.log(LoggingLevel.INFO, log, "Finished");
	}

	private Predicate isFileNameValid() {
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				String fileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY);
				log.info("File '" + fileName + "' was founded by route");

				String regex = "hbm-\\d+\\.json";
				return Pattern.compile(regex).matcher(fileName).matches();
			}
		};
	}

}
