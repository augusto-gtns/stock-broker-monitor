package hbm.repository;

import java.util.Date;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hbm.model.Stock;

@Repository
@Transactional(rollbackOn = Exception.class)
public interface StockRepository extends CrudRepository<Stock, Long> {

	Stock findByDescription(String description);

	@Query(value = "CALL prc_stock_hist_avg_weighted(:stockId, :date)", nativeQuery = true)
	Tuple dailyWeightedAverageByStock(@Param("stockId") Integer stockId, @Param("date") Date date);

	@Modifying
	@Query(value = "CALL prc_del_stock_hist_outtime(:date)", nativeQuery = true)
	void clearOutOfTimeData(@Param("date") Date date);

	@Modifying
	@Query(value = "CALL prc_stock_hist_group_to_legacy(:date)", nativeQuery = true)
	void moveToLegacyTable(@Param("date") Date date);

}
