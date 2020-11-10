package hbm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hbm.model.StockDailyHistory;

@Repository
public interface StockDailyHistoryRepository extends CrudRepository<StockDailyHistory, Integer>{

}
