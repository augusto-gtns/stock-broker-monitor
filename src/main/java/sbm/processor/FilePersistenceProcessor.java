package sbm.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;

import lombok.extern.log4j.Log4j2;
import sbm.model.StockHistoryDTO;
import sbm.router.MainRouter;
import sbm.service.StockService;
import sbm.util.SBMUtil;

@Log4j2
@Component(FilePersistenceProcessor.NAME)
public class FilePersistenceProcessor implements Processor {

	public static final String NAME = "FilePersistenceProcessor";

	@Autowired
	private StockService stockService;

	@Override
	public void process(Exchange exchange) throws Exception {
		String json = exchange.getIn().getBody(String.class);

		List<StockHistoryDTO> itens = SBMUtil.gsonConverter().fromJson(json, new TypeToken<List<StockHistoryDTO>>(){}.getType());
		stockService.saveStockHistoryAndCreateStockIfNotExists(itens);
		
		exchange.getIn().setHeader(MainRouter.STOCK_LIST_HEADER, itens);
		
		log.info("Data file persisted");
	}

}
