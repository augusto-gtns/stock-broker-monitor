package hbm.model;

import java.math.BigDecimal;

public class StockHistoryMetrics {

	private BigDecimal currentPriceAvg;
	
	private BigDecimal variationAvg;
	
	public BigDecimal getCurrentPriceAvg() {
		return currentPriceAvg;
	}

	public void setCurrentPriceAvg(BigDecimal currentPriceAvg) {
		this.currentPriceAvg = currentPriceAvg;
	}

	public BigDecimal getVariationAvg() {
		return variationAvg;
	}

	public void setVariationAvg(BigDecimal variationAvg) {
		this.variationAvg = variationAvg;
	}
	
}
