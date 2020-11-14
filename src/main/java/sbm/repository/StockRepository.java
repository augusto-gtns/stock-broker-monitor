package sbm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sbm.model.Stock;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

	Stock findByDescription(String description);
}
