package coop.tecso.examen.dto.account;

import java.math.BigDecimal;

/**
 * @author nahuel.barrena on 4/4/20
 */
public enum	AccountCurrencyEnum {
	ARS(BigDecimal.valueOf(-1000)), USD(BigDecimal.valueOf(-300)), EUR(BigDecimal.valueOf(-150));

	private BigDecimal uncoverLimit;

	AccountCurrencyEnum(BigDecimal uncoverLimit) {
		this.uncoverLimit = uncoverLimit;
	}

	public BigDecimal getUncoverLimit() {
		return uncoverLimit;
	}
}
