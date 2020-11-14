package hbm.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hbm.model.StockHistoryDTO;
import hbm.router.HomeBrokerRouter;
import hbm.service.HBMService;
import hbm.util.SystemNotification;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component(NotifierProcessor.NAME)
public class NotifierProcessor implements Processor {

	public static final String NAME = "NotifierProcessor";

	@Override
	public void process(Exchange exchange) throws Exception {
		List<StockHistoryDTO> itens = (List<StockHistoryDTO>) exchange.getIn().getHeader(HomeBrokerRouter.STOCK_LIST_HEADER);
		
		boolean notify = false;
		
		// ===========================================
		
		// IMPLEMENT YOUR OWN ANALYTICS STRATEGY HERE
		notify = true;
		
		// ===========================================
		
		if (notify)
			SystemNotification.send("notification message");

		log.info("NotifierProcessor analysis was finalized");
	}

}
