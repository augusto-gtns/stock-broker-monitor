package hbm.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;

import hbm.model.StockHistoryDTO;
import hbm.router.HomeBrokerRouter;
import hbm.service.HBMService;
import hbm.util.HBMMonitorUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component(FilePersistenceProcessor.NAME)
public class FilePersistenceProcessor implements Processor {

	public static final String NAME = "FilePersistenceProcessor";

	@Autowired
	private HBMService hbmService;

	@Override
	public void process(Exchange exchange) throws Exception {
		String json = exchange.getIn().getBody(String.class);

		List<StockHistoryDTO> itens = HBMMonitorUtil.gsonConverter().fromJson(json, new TypeToken<List<StockHistoryDTO>>(){}.getType());
		hbmService.saveStockHistoryAndCreateStockIfNotExists(itens);
		
		exchange.getIn().setHeader(HomeBrokerRouter.STOCK_LIST_HEADER, itens);
		
		log.info("Data file persisted");
	}

}
