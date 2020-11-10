package hbm.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Table(name = "stock_daily_history")
@Data
public class StockDailyHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@ManyToOne
	private Stock stock;
	
	@NotNull
	private BigDecimal currentPrice;
	
	@NotNull
	private BigDecimal variation;

	private BigDecimal buyPrice;
	
	private BigDecimal sellPrice;
	
	private Date dtCollect;
	
}
