package hbm.processor;

import java.util.Date;

import javax.persistence.Tuple;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hbm.model.StockHistory;
import hbm.model.StockHistoryMetrics;
import hbm.repository.StockRepository;
import hbm.util.BigDecUtil;
import hbm.util.HBMMonitorUtil;
import hbm.util.RouteHeader;
import hbm.util.thread.ExecutorHelper;
import hbm.util.thread.ExecutorImpl;
import lombok.extern.log4j.Log4j2;

@Component(CalculateExtraDataProcessor.NAME)
@Log4j2
public class CalculateExtraDataProcessor implements Processor {

	public static final String NAME = "CalculateExtraDataProcessor";

	@Autowired
	StockRepository stockRepository;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		StockHistory stockHistory = (StockHistory) exchange.getIn().getHeader(RouteHeader.STOCK_HIST.name());

		// Calculate metrics variables
		StockHistoryMetrics metrics = new StockHistoryMetrics();

		final Date today = HBMMonitorUtil.getToday();

		ExecutorImpl<Tuple> average = new ExecutorImpl<Tuple>() {
			@Override
			public Tuple callExecutor() throws Exception {
				return stockRepository.dailyWeightedAverageByStock(stockHistory.getStock().getId(), today);
			}

			@Override
			public void loadResult(Tuple result) throws Exception {
				metrics.setCurrentPriceAvg(BigDecUtil.get(result, "current_price"));
				metrics.setVariationAvg(BigDecUtil.get(result, "variation"));
			}
		};

		ExecutorHelper.execute(CalculateExtraDataProcessor.NAME, average);

		exchange.getIn().setHeader(RouteHeader.STOCK_HIST.name(), stockHistory);
		stockHistory.setMetrics(metrics);

		log.info("Data Calculated");
	}

}
