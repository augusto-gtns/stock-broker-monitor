package sbm.router;

import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sbm.processor.DeleteFileProcessor;
import sbm.processor.FilePersistenceProcessor;
import sbm.processor.NotifierProcessor;
import sbm.processor.ThrowableProcessor;
import sbm.util.SBMUtil;

@Component
public class MainRouter extends RouteBuilder {

	public static final String NAME = "MainRouter";
	public static final String STOCK_LIST_HEADER = "STOCK_LIST_HEADER";

	@Value("${sbm.folder}")
	private String folder;

	@Override
	public void configure() throws Exception {
		RouteDefinition route = from("file://" + SBMUtil.toRoutePath(folder) + "?noop=true").routeId(MainRouter.NAME)
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

				String regex = "sbm-\\d+\\.json";
				return Pattern.compile(regex).matcher(fileName).matches();
			}
		};
	}

}
