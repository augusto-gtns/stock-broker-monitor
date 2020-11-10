package hbm.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Transient;

import hbm.util.HBMMonitorUtil;
import lombok.Data;

@Data
public class StockHistory {

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

	@Transient
	private StockHistoryMetrics metrics;

	public Date getDtCollect() throws ParseException {
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		Date dtCollect = dtFormat.parse(dtCollectStr);

		if (!HBMMonitorUtil.isEnvPRD()) {
			Calendar today = Calendar.getInstance();
			today.setTime(HBMMonitorUtil.getToday());

			Calendar _dtCollect = Calendar.getInstance();
			_dtCollect.setTime(dtCollect);
			_dtCollect.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));
			_dtCollect.set(Calendar.MONTH, today.get(Calendar.MONTH));
			_dtCollect.set(Calendar.YEAR, today.get(Calendar.YEAR));

			dtCollect = _dtCollect.getTime();
		}

		return dtCollect;
	}
}
