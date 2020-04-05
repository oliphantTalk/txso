package coop.tecso.examen.service.account;

import java.math.BigDecimal;

import coop.tecso.examen.exceptions.TransactionStrategyException;
import coop.tecso.examen.model.account.AccountEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
public interface AccountTransactionStrategy {

	boolean canUpdate(BigDecimal balance, BigDecimal amount, BigDecimal limit);

	void updateBalanceIfCan(AccountEntity account, BigDecimal amount, BigDecimal uncoverLimit)
			throws TransactionStrategyException;

	void updateBalance(AccountEntity account, BigDecimal amount);
}
