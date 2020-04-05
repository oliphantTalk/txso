package coop.tecso.examen.service.account;

import java.util.List;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.exceptions.AccountServiceException;
import coop.tecso.examen.exceptions.TransactionStrategyException;

/**
 * @author nahuel.barrena on 4/4/20
 */
public interface IAccountService {

	Account getAccount(Long accountNumber) throws AccountServiceException;

	List<Account> getAccountList();

	Account createAccount(Account account);

	void deleteAccount(Long accountNumber) throws AccountServiceException;

	Account addAccountMovement(Long accountNumber, Movement movement)
			throws AccountServiceException, TransactionStrategyException;

	List<Movement> getAccountMovementList(Long accountNumber) throws AccountServiceException;

}
