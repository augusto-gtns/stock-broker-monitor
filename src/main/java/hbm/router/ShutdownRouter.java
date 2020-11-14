package hbm.router;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShutdownRouter extends RouteBuilder {

	public static final String NAME = "ShutdownRouter";

	@Value("${server.port:}")
	private String serverPort;

	@Override
	public void configure() throws Exception {
		RouteDefinition route = from("cron:shutdown?schedule=0 1 17 * * *").routeId(NAME)
			.log(LoggingLevel.INFO, log, ShutdownRouter.NAME)
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					URL url = new URL("http://localhost:" + serverPort + "/actuator/shutdown");
					URLConnection con = url.openConnection();
					HttpURLConnection http = (HttpURLConnection) con;
					http.setRequestMethod("POST");
					http.getResponseCode();
				}
			});
	}

}
