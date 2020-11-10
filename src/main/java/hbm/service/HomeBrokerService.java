package hbm.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import hbm.model.Stock;
import hbm.model.StockDaily;
import hbm.model.StockDailyHistory;
import hbm.model.StockHistory;
import hbm.repository.StockDailyHistoryRepository;
import hbm.repository.StockDailyRepository;
import hbm.repository.StockRepository;
import hbm.util.ExceptionUtil;
import hbm.util.HBMMonitorUtil;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional(rollbackOn = Exception.class)
@Log4j2
public class HomeBrokerService {

	@Autowired
	StockRepository stockRepository;

	@Autowired
	StockDailyRepository stockDailyRepository;

	@Autowired
	StockDailyHistoryRepository stockDailyHistoryRepository;

	public void saveStockHistoryAndStockIfNotExists(List<StockHistory> itens) {

		if (CollectionUtils.isEmpty(itens))
			return;

		Date today = HBMMonitorUtil.getToday();

		itens.forEach(iten -> {
			StockDailyHistory stockDailyHistory = new StockDailyHistory();
			
			// stock
			Stock stock = new Stock();
			stock.setDescription(iten.getStockDescription());

			Stock persistedStock = stockRepository.findByDescription(stock.getDescription());
			if (persistedStock == null) {
				stockRepository.save(stock);
				stockDailyHistory.setStock(stock);
			} else
				stockDailyHistory.setStock(persistedStock);
						
			//dt
			
			Date dtCollect = null;
			try {
				dtCollect= iten.getDtCollect();
			} catch (ParseException e) {
				dtCollect = today;
				log.error(ExceptionUtil.getStackTraceMinified(e));
			}
			
			stockDailyHistory.setDtCollect(dtCollect);
			
			//
			
			stockDailyHistory.setCurrentPrice(iten.getCurrentPrice());
			stockDailyHistory.setVariation(iten.getVariation());
			stockDailyHistory.setBuyPrice(iten.getBuyPrice());
			stockDailyHistory.setSellPrice(iten.getSellPrice());
			
			stockDailyHistoryRepository.save(stockDailyHistory);
			
			// stockDaily
			StockDaily stockDaily = stockDailyRepository.findByStockAndDtCreat(stockDailyHistory.getStock(), dtCollect);
			
			if (stockDaily == null) {
				stockDaily = new StockDaily();
				stockDaily.setStock(stockDailyHistory.getStock());
				stockDaily.setDtCreat(dtCollect);
			}

			stockDaily.setOpenPrice(iten.getOpenPrice());
			stockDaily.setClosePrice(iten.getClosePrice());
			stockDaily.setMinPriceDay(iten.getMinPriceDay());
			stockDaily.setMaxPriceDay(iten.getMaxPriceDay());
			stockDaily.setNegotiationVolume(iten.getNegotiationVolume());
			
			stockDailyRepository.save(stockDaily);
		});
	}

}
