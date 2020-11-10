package hbm.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hbm.model.Stock;
import hbm.model.StockDaily;

@Repository
public interface StockDailyRepository extends CrudRepository<StockDaily, Integer>{
	
	StockDaily findByStockAndDtCreat(Stock stock, Date dt);
	
}
