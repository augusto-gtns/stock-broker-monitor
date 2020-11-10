package hbm.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "stock_daily")
@Data
public class StockDaily {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@ManyToOne
	private Stock stock;
	
	private BigDecimal openPrice;
	
	private BigDecimal closePrice;
	
	private BigDecimal minPriceDay;
	
	private BigDecimal maxPriceDay;
	
	private Long negotiationVolume;
	
	@Temporal(TemporalType.DATE)
	private Date dtCreat;
	
}
