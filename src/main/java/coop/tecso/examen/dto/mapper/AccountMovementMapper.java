package coop.tecso.examen.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.model.account.AccountEntity;
import coop.tecso.examen.model.movement.MovementEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
@Mapper
public abstract class AccountMovementMapper {

	public static final AccountMovementMapper INSTANCE = Mappers.getMapper(AccountMovementMapper.class);

	@Mapping(target = "account.movements", ignore = true)
	public abstract Movement movementEntityToDto(MovementEntity movementEntity);

	@Mapping(target = "account.movements", ignore = true)
	public abstract MovementEntity movementDtoToEntity(Movement movementEntity);

	public abstract Account accountEntityToDto(AccountEntity accountEntity);

	public abstract AccountEntity accountDtoToEntity(Account account);

}
