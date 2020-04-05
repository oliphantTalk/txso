package coop.tecso.examen.controller.account;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import coop.tecso.examen.dto.account.AccountCurrencyEnum;

/**
 * @author nahuel.barrena on 5/4/20
 */
public class CreateAccountRequest {

	@NotNull
	private AccountCurrencyEnum accountCurrency;

	@NotNull
	@DecimalMin(value = "0")
	private BigDecimal balance;

	public AccountCurrencyEnum getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(AccountCurrencyEnum accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
