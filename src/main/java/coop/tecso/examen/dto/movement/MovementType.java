package coop.tecso.examen.dto.movement;

import coop.tecso.examen.service.account.AccountTransactionStrategy;
import coop.tecso.examen.service.account.strategy.CreditStrategy;
import coop.tecso.examen.service.account.strategy.DebitStrategy;

/**
 * @author nahuel.barrena on 4/4/20
 */
public enum  MovementType {
	DEBIT(new DebitStrategy()), CREDIT(new CreditStrategy());

	private AccountTransactionStrategy transactionStrategy;

	MovementType(AccountTransactionStrategy transactionStrategy) {
		this.transactionStrategy = transactionStrategy;
	}

	public AccountTransactionStrategy getTransactionStrategy() {
		return transactionStrategy;
	}
}
