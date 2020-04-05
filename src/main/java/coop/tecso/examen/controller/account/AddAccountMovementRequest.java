package coop.tecso.examen.controller.account;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import coop.tecso.examen.dto.movement.MovementType;

/**
 * @author nahuel.barrena on 5/4/20
 */
public class AddAccountMovementRequest {

	@NotNull
	private MovementType movementType;

	private String description;

	@NotNull
	@DecimalMin(value = "0.01")
	private BigDecimal amount;

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
}
