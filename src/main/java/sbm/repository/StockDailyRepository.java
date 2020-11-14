package sbm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sbm.model.StockDaily;

@Repository
public interface StockDailyRepository extends CrudRepository<StockDaily, Integer>{

}
