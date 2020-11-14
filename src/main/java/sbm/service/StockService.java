package sbm.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.log4j.Log4j2;
import sbm.model.Stock;
import sbm.model.StockDaily;
import sbm.model.StockHistoryDTO;
import sbm.repository.StockDailyRepository;
import sbm.repository.StockRepository;

@Service
@Transactional(rollbackOn = Exception.class)
@Log4j2
public class StockService {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StockDailyRepository stockDailyHistoryRepository;

	public void saveStockHistoryAndCreateStockIfNotExists(List<StockHistoryDTO> itens) {

		if (CollectionUtils.isEmpty(itens))
			return;

		final Date today = new Date();

		itens.forEach(iten -> {
			StockDaily stockDaily = new StockDaily();
			
			Stock stock = new Stock();
			stock.setDescription(iten.getStockDescription());

			Stock persistedStock = stockRepository.findByDescription(stock.getDescription());
			if (persistedStock != null)
				stockDaily.setStock(persistedStock);
			else {
				stockRepository.save(stock);
				stockDaily.setStock(stock);
			}
						
			Date dtCollect = null;
			try {
				dtCollect= iten.getDtCollect();
			} catch (ParseException e) {
				dtCollect = today;
				log.error(e.getMessage(), e);
			}
			stockDaily.setDtCollect(dtCollect);
			
			stockDaily.setCurrentPrice(iten.getCurrentPrice());
			stockDaily.setVariation(iten.getVariation());
			
			stockDailyHistoryRepository.save(stockDaily);
		});
	}
}
