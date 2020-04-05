package coop.tecso.examen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import coop.tecso.examen.model.account.AccountEntity;

/**
 * @author nahuel.barrena on 4/4/20
 */
public interface IAccountRepository extends JpaRepository<AccountEntity, Long> {

	Optional<AccountEntity> findAccountEntityByAccountNumber(Long accountNumber);

}
