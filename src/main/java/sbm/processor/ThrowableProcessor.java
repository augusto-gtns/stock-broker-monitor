package sbm.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component(ThrowableProcessor.NAME)
public class ThrowableProcessor implements Processor {

	public static final String NAME = "ThrowableProcessor";
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Throwable throwable = (Throwable) exchange.getProperties().get(Exchange.EXCEPTION_CAUGHT);
		log.error("ThrowableProcessor");
		log.error(throwable.getMessage(), throwable);
	}
}
