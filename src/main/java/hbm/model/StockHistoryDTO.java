package hbm.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class StockHistoryDTO {

	private BigDecimal currentPrice;
	private BigDecimal variation;
	private BigDecimal buyPrice;
	private BigDecimal sellPrice;
	private BigDecimal openPrice;
	private BigDecimal closePrice;
	private BigDecimal minPriceDay;
	private BigDecimal maxPriceDay;
	private Long buyVolume;
	private Long negotiationVolume;
	private String dtCollectStr;
	private String stockDescription;
	private Stock stock;

	public Date getDtCollect() throws ParseException {
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		return dtFormat.parse(dtCollectStr);
	}
}
