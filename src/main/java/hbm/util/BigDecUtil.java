package hbm.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Tuple;

public class BigDecUtil {

	public static BigDecimal get(Tuple tuple, String key) {
		return tuple.get(key)==null ? null : new BigDecimal(tuple.get(key).toString());
	}
	
	public static BigDecimal round(BigDecimal n) {
		return n.setScale(2, RoundingMode.HALF_EVEN);
	}

	public static BigDecimalComparator is(BigDecimal value) {
		return new BigDecimalComparator(value);
	}

	public static class BigDecimalComparator {

		private final BigDecimal value;

		public BigDecimalComparator(BigDecimal value) {
			this.value = value;
		}

		public boolean equalsTo(BigDecimal value2) {
			return value.compareTo(value2) == 0;
		}

		public boolean greaterThan(BigDecimal value2) {
			return value.compareTo(value2) == 1;
		}

		public boolean lessThan(BigDecimal value2) {
			return value.compareTo(value2) == -1;
		}

		public boolean greaterThanOrEqualsTo(BigDecimal value2) {
			return greaterThan(value2) || equalsTo(value2);
		}

		public boolean lessThanOrEqualsTo(BigDecimal value2) {
			return lessThan(value2) || equalsTo(value2);
		}
	}

}
