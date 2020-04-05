package coop.tecso.examen.model.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.account.AccountCurrencyEnum;
import coop.tecso.examen.dto.mapper.AccountMovementMapper;
import coop.tecso.examen.exceptions.EntityDeleteException;
import coop.tecso.examen.model.AbstractPersistentObject;
import coop.tecso.examen.model.movement.MovementEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
@Entity
@Table(name = "account")
public class AccountEntity extends AbstractPersistentObject {

	private static final long serialVersionUID = -8901152343511467206L;

	@Column(name = "account_number", updatable = false, nullable = false)
	private Long accountNumber;

	@Enumerated(EnumType.STRING)
	private AccountCurrencyEnum currency;

	@Column(name = "balance", scale = 2)
	private BigDecimal balance;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<MovementEntity> movements = new ArrayList<>();

	// G&S

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

	public List<MovementEntity> getMovements() {
		return movements;
	}

	public void setMovements(List<MovementEntity> movements) {
		this.movements = movements;
	}

	public void addMovement(MovementEntity movementEntity){
		movements.add(movementEntity);
		movementEntity.setAccount(this);
	}

	@PreRemove
	public void preRemove() throws EntityDeleteException {
		if(!movements.isEmpty()) throw new EntityDeleteException("Cannot perform delete. Entity has movements");
	}

	public Account toDto(){
		return AccountMovementMapper.INSTANCE.accountEntityToDto( this);
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		AccountEntity that = (AccountEntity) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(accountNumber, that.accountNumber)
				.append(currency, that.currency).append(balance, that.balance).append(movements, that.movements)
				.isEquals();
	}

	@Override public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(accountNumber).append(currency)
				.append(balance).append(movements).toHashCode();
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
