package hbm.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hbm.model.StockHistory;
import hbm.service.HomeBrokerService;
import hbm.util.HBMMonitorUtil;
import lombok.extern.log4j.Log4j2;

@Component(HomeBrokerFileToDatatabaseProcessor.NAME)
@Log4j2
public class HomeBrokerFileToDatatabaseProcessor implements Processor {

	public static final String NAME = "HomeBrokerFileToDatatabaseProcessor";

	@Autowired
	HomeBrokerService homeBrokerService;
	
	@Value("${hbmonitor.hb.folder}")
	private String folder;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		String body = exchange.getIn().getBody(String.class);
		List<StockHistory> itens =  HBMMonitorUtil.parseJsonStockHistoryList(body);

		homeBrokerService.saveStockHistoryAndStockIfNotExists(itens);
		
		log.info("File persisted");
	}

}
