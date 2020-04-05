package coop.tecso.examen.model.movement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import coop.tecso.examen.dto.mapper.AccountMovementMapper;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.dto.movement.MovementType;
import coop.tecso.examen.model.AbstractPersistentObject;
import coop.tecso.examen.model.account.AccountEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
@Entity
@Table(name = "movement")
public class MovementEntity extends AbstractPersistentObject {

	@Column(name = "created_on")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdOn;

	@Enumerated(EnumType.STRING)
	@Column(name = "movement_type")
	private MovementType movementType;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "amount", scale = 2)
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private AccountEntity account;

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

	@Override
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

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	@PrePersist
	public void prePersist(){
		this.createdOn = LocalDateTime.now();
	}

	public Movement toDto(){
		return AccountMovementMapper.INSTANCE.movementEntityToDto(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		MovementEntity that = (MovementEntity) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(createdOn, that.createdOn)
				.append(movementType, that.movementType).append(description, that.description)
				.append(amount, that.amount).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(createdOn).append(movementType)
				.append(description).append(amount).toHashCode();
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
