package sbm.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import sbm.model.StockHistoryDTO;
import sbm.router.MainRouter;
import sbm.util.SystemNotification;

@Log4j2
@Component(NotifierProcessor.NAME)
public class NotifierProcessor implements Processor {

	public static final String NAME = "NotifierProcessor";

	@Override
	public void process(Exchange exchange) throws Exception {
		List<StockHistoryDTO> itens = (List<StockHistoryDTO>) exchange.getIn().getHeader(MainRouter.STOCK_LIST_HEADER);
		
		boolean notify = false;
		
		// ===========================================
		// DO IMPLEMENT YOUR OWN ANALYTICS STRATEGY HERE

		notify = true;
		
		// ===========================================
		
		if (notify)
			SystemNotification.send("Take a look at your stock broker!");

		log.info("NotifierProcessor analysis was finalized");
	}

}
