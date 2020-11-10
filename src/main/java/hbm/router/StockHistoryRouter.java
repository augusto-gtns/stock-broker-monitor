package hbm.router;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hbm.processor.AlarmHomeBrokerProcessor;
import hbm.processor.CalculateExtraDataProcessor;
import hbm.processor.DeleteFileProcessor;
import hbm.processor.HomeBrokerFileSplitProcessor;
import hbm.processor.PutSotckInHeaderProcessor;
import hbm.util.HBMMonitorUtil;

@Component
public class StockHistoryRouter extends RouteBuilder {

	public static final String NAME = "StockHistoryRouter";
	
	@Autowired
	Logger log;

	@Value("${hbmonitor.hb.folder}")
	private String folder;

	@Override
	public void configure() throws Exception {
		String from = "file://" + HBMMonitorUtil.toRoutePath(folder) + HomeBrokerFileSplitProcessor.STOCK_ITEM_FOLDER
				+ "?noop=true";
		RouteDefinition route = from(from).routeId(NAME).log(LoggingLevel.INFO, log, "Running");

		route.log(LoggingLevel.INFO, log, PutSotckInHeaderProcessor.NAME).process(PutSotckInHeaderProcessor.NAME);
		
		route.log(LoggingLevel.INFO, log, CalculateExtraDataProcessor.NAME).process(CalculateExtraDataProcessor.NAME);

		route.log(LoggingLevel.INFO, log, AlarmHomeBrokerProcessor.NAME).process(AlarmHomeBrokerProcessor.NAME);

		route.log(LoggingLevel.INFO, log, DeleteFileProcessor.NAME).process(DeleteFileProcessor.NAME);
		
		route.log(LoggingLevel.INFO, log, "Finished");
	}

}
