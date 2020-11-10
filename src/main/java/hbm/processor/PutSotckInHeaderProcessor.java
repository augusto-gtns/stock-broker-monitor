package hbm.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hbm.model.StockHistory;
import hbm.repository.StockRepository;
import hbm.util.HBMMonitorUtil;
import hbm.util.RouteHeader;
import lombok.extern.log4j.Log4j2;

@Component(PutSotckInHeaderProcessor.NAME)
@Log4j2
public class PutSotckInHeaderProcessor implements Processor {

	public static final String NAME = "PutSotckInHeaderProcessor";

	@Autowired
	StockRepository stockRepository;

	@Override
	public void process(Exchange exchange) throws Exception {

		String body = exchange.getIn().getBody(String.class);

		StockHistory stockHistory = HBMMonitorUtil.parseJsonStockHistory(body);
		stockHistory.setStock(stockRepository.findByDescription(stockHistory.getStockDescription()));
		
		exchange.getIn().setHeader(RouteHeader.STOCK_HIST.name(), stockHistory);
		
		log.info("Stock added to route header");
	}

}
