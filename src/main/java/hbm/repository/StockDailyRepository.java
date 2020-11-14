package hbm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hbm.model.StockDaily;

@Repository
public interface StockDailyRepository extends CrudRepository<StockDaily, Integer>{

}
