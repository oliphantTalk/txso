package coop.tecso.examen.service.account.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import coop.tecso.examen.exceptions.TransactionStrategyException;
import coop.tecso.examen.model.account.AccountEntity;
import coop.tecso.examen.service.account.AccountTransactionStrategy;

/**
 * @author nahuel.barrena on 9/4/20
 */
public abstract class AbstractAccountStrategy implements AccountTransactionStrategy {

	protected RoundingMode roundingMode = RoundingMode.HALF_UP;

	public void updateBalanceIfCan(AccountEntity account, BigDecimal amount, BigDecimal uncoverLimit)
			throws TransactionStrategyException {
		if(canUpdate(account.getBalance(), amount, uncoverLimit)){
			updateBalance(account, amount);
		} else {
			throw new TransactionStrategyException("Cannot perform movement. Uncover limit is exceeded.");
		}
	}

	@Override
	public void updateBalance(AccountEntity account, BigDecimal amount) {
		account.setBalance(account.getBalance().subtract(amount).setScale(2, roundingMode));
	}
}
