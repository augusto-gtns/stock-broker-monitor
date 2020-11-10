package hbm.router;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hbm.repository.StockRepository;
import hbm.util.HBMMonitorUtil;

@Component
public class ShutdownRouter extends RouteBuilder {
	
	public static final String NAME = "ShutdownRouter";

	@Autowired
	Logger log;
	
	@Autowired
	StockRepository stockRepository;
	
	@Value("${server.port}")
	private String serverPort;
	
	@Override
	public void configure() throws Exception {
		String from = "cron:shutdown?schedule=0 1 17 * * *";
		RouteDefinition route = from(from).routeId(NAME).log(LoggingLevel.INFO, log, ShutdownRouter.NAME);
		
		route.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				final Date today = HBMMonitorUtil.getToday();
				
				stockRepository.clearOutOfTimeData(today);
				
				stockRepository.moveToLegacyTable(today);
			}
		});
		
		route.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				URL url = new URL("http://localhost:"+serverPort+"/actuator/shutdown");
				URLConnection con = url.openConnection();
				HttpURLConnection http = (HttpURLConnection) con;
				http.setRequestMethod("POST");
				http.getResponseCode();
			}
		});
	}

}
