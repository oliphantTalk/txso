package coop.tecso.examen.service.account.strategy;

import java.math.BigDecimal;

/**
 * @author nahuel.barrena on 4/4/20
 */
public class CreditStrategy extends AbstractAccountStrategy {

	@Override
	public boolean canUpdate(BigDecimal balance, BigDecimal amount, BigDecimal limit) {
		return balance.subtract(amount).setScale(2, roundingMode).compareTo(limit) > -1;
	}
}
