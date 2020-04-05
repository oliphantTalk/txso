package coop.tecso.examen.dto.movement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.mapper.AccountMovementMapper;
import coop.tecso.examen.model.movement.MovementEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
public class Movement {

	private LocalDateTime createdOn;
	private MovementType movementType;
	private String description;
	private BigDecimal amount;
	private Account account;

	public Movement(){}

	public Movement(MovementType movementType, String description, BigDecimal amount) {
		this.movementType = movementType;
		this.description = description;
		this.amount = amount;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public MovementType getMovementType() {
		return movementType;
	}

	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public MovementEntity toEntity(){
		return AccountMovementMapper.INSTANCE.movementDtoToEntity(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
