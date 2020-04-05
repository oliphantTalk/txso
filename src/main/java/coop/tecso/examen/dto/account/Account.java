package coop.tecso.examen.dto.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import coop.tecso.examen.dto.mapper.AccountMovementMapper;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.model.account.AccountEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
public class Account {

	private Long accountNumber;
	private AccountCurrencyEnum currency;
	private BigDecimal balance;
	private List<Movement> movements = new ArrayList<>();

	public Account() {
	}

	public Account(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Account(AccountCurrencyEnum currency, BigDecimal balance) {
		this.currency = currency;
		this.balance = balance;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountCurrencyEnum getCurrency() {
		return currency;
	}

	public void setCurrency(AccountCurrencyEnum currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<Movement> getMovements() {
		return movements;
	}

	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}

	public void addMovement(Movement movement){
		movements.add(movement);
		movement.setAccount(this);
	}

	public AccountEntity toEntity(){
		return AccountMovementMapper.INSTANCE.accountDtoToEntity(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
