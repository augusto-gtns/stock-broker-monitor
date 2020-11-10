package hbm.processor;

import java.math.BigDecimal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hbm.model.StockHistory;
import hbm.service.HomeBrokerService;
import hbm.util.BigDecUtil;
import hbm.util.Icons;
import hbm.util.RouteHeader;
import hbm.util.SystemNotification;
import hbm.util.thread.ExecutorHelper;
import hbm.util.thread.ExecutorVoidImpl;
import lombok.extern.log4j.Log4j2;

@Component(AlarmHomeBrokerProcessor.NAME)
@Log4j2
public class AlarmHomeBrokerProcessor implements Processor {

	public static final String NAME = "AlarmHomeBrokerProcessor";

	@Autowired
	HomeBrokerService homeBrokerService;

	@Override
	public void process(Exchange exchange) throws Exception {
		StockHistory stockHistory = (StockHistory) exchange.getIn().getHeader(RouteHeader.STOCK_HIST.name());

		ExecutorVoidImpl alarm = new ExecutorVoidImpl() {
			@Override
			public void callVoidExecutor() throws Exception {

				if( stockHistory.getMetrics().getVariationAvg()==null ) return;
				if( stockHistory.getMetrics().getCurrentPriceAvg()==null ) return;
				
				boolean isPositiveVariation = BigDecUtil.is(stockHistory.getVariation()).greaterThan(BigDecimal.ZERO)
						&& BigDecUtil.is(stockHistory.getMetrics().getVariationAvg()).greaterThan(BigDecimal.ZERO);

				boolean isNegativeVariation = BigDecUtil.is(stockHistory.getVariation()).lessThan(BigDecimal.ZERO)
						&& BigDecUtil.is(stockHistory.getMetrics().getVariationAvg()).lessThan(BigDecimal.ZERO);

				BigDecimal multiplicador = stockHistory.getMetrics().getVariationAvg().multiply(new BigDecimal("0.85"))
						.divide(new BigDecimal("100"));

				BigDecimal targetVariation = stockHistory.getMetrics().getCurrentPriceAvg().multiply(multiplicador);
				if (BigDecUtil.is(targetVariation).lessThan(BigDecimal.ZERO))
					targetVariation = targetVariation.multiply(new BigDecimal(-1));

				BigDecimal lowValue = stockHistory.getMetrics().getCurrentPriceAvg().subtract(targetVariation);

				BigDecimal highValue = stockHistory.getMetrics().getCurrentPriceAvg().add(targetVariation);

				boolean upTendence = isPositiveVariation && BigDecUtil.is(stockHistory.getVariation())
						.greaterThan(stockHistory.getMetrics().getVariationAvg());

				boolean downTendence = isNegativeVariation && BigDecUtil.is(stockHistory.getVariation())
						.lessThan(stockHistory.getMetrics().getVariationAvg());

				boolean nextToDayMinPrice = BigDecUtil.is(stockHistory.getCurrentPrice()).lessThan(lowValue)
						&& BigDecUtil.is(stockHistory.getCurrentPrice()).greaterThan(stockHistory.getMinPriceDay());

				boolean nextToDayMaxPrice = BigDecUtil.is(stockHistory.getCurrentPrice()).greaterThan(highValue);

				// TODO: alarm by occurrence: identify N occurrences in last N minutes
				boolean alarmed = (nextToDayMinPrice && upTendence) || (nextToDayMaxPrice && downTendence);

				StringBuilder alarmLog = new StringBuilder();

				alarmLog.append(stockHistory.getStockDescription());

				if (upTendence)
					alarmLog.append(Icons.ARROW_UP.getUnicodeWithSpace());

				if (downTendence)
					alarmLog.append(Icons.ARROW_DOWN.getUnicodeWithSpace());

				if (nextToDayMinPrice)
					alarmLog.append(Icons.ARROW_LEFT.getUnicodeWithSpace());

				if (nextToDayMaxPrice)
					alarmLog.append(Icons.ARROW_RIGHT.getUnicodeWithSpace());

				alarmLog.append(" | Price: $").append(stockHistory.getCurrentPrice()).append(" x $")
						.append(BigDecUtil.round(stockHistory.getMetrics().getCurrentPriceAvg())).append(" [Avg]");

				alarmLog.append(" | Variation: ").append(BigDecUtil.round(stockHistory.getVariation())).append("% x ")
						.append(BigDecUtil.round(stockHistory.getMetrics().getVariationAvg())).append("% [Avg]");

				alarmLog.append(" | Low: $").append(BigDecUtil.round(lowValue)).append(" x High: $")
						.append(BigDecUtil.round(highValue));
				
				if (alarmed) {
					SystemNotification.send(alarmLog.toString());
					alarmLog.append(" | ALARMTRACE");
				}
				
				log.warn(alarmLog.toString());
			}
		};

		ExecutorHelper.execute(AlarmHomeBrokerProcessor.NAME, alarm);

		log.info("Alarms analysis was finalized");
	}

}
