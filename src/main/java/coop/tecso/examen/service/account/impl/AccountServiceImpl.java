package coop.tecso.examen.service.account.impl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.exceptions.AccountServiceException;
import coop.tecso.examen.exceptions.TransactionStrategyException;
import coop.tecso.examen.model.account.AccountEntity;
import coop.tecso.examen.model.movement.MovementEntity;
import coop.tecso.examen.repository.IAccountRepository;
import coop.tecso.examen.service.account.AccountTransactionStrategy;
import coop.tecso.examen.service.account.IAccountService;

/**
 * @author nahuel.barrena on 4/4/20
 */
@Service
public class AccountServiceImpl implements IAccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

	private final IAccountRepository accountRepository;

	public AccountServiceImpl(IAccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Account getAccount(Long accountNumber) throws AccountServiceException {
		return this.getAccountEntityByNumber(accountNumber).toDto();
	}

	@Override
	public List<Account> getAccountList() {
		return accountRepository.findAll().stream().map(AccountEntity::toDto).collect(Collectors.toList());
	}

	@Override
	public Account createAccount(Account account) {
		account.setAccountNumber(Math.abs(new SecureRandom().nextLong()));
		return accountRepository.save(account.toEntity()).toDto();
	}

	@Override
	public void deleteAccount(Long accountNumber) throws AccountServiceException {
		try {
			accountRepository.delete(this.getAccountEntityByNumber(accountNumber));
		} catch (Exception e) {
			LOGGER.error("Exception trying to delete account {}", accountNumber);
			throw new AccountServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Account addAccountMovement(Long accountNumber, Movement movement)
			throws AccountServiceException, TransactionStrategyException {
		AccountEntity accountEntity = this.getAccountEntityByNumber(accountNumber);
		AccountTransactionStrategy transactionStrategy = movement.getMovementType().getTransactionStrategy();
		BigDecimal amount = movement.getAmount();
		BigDecimal uncoverLimit = accountEntity.getCurrency().getUncoverLimit();
		try {
			transactionStrategy.updateBalanceIfCan(accountEntity, amount, uncoverLimit);
			accountEntity.addMovement(movement.toEntity());
			return accountRepository.save(accountEntity).toDto();
		} catch (TransactionStrategyException e) {
			LOGGER.error("Cannot perform transaction update for movement {}", movement);
			throw e;
		}
	}

	@Override
	public List<Movement> getAccountMovementList(Long accountNumber) throws AccountServiceException {
		return this.getAccountEntityByNumber(accountNumber).getMovements()
				.stream()
				.map(MovementEntity::toDto)
				.sorted(Comparator.comparing(Movement::getCreatedOn).reversed())
				.collect(Collectors.toList());
	}

	private AccountEntity getAccountEntityByNumber(Long accountNumber) throws AccountServiceException {
		return accountRepository.findAccountEntityByAccountNumber(accountNumber)
				.orElseThrow(() -> {
					LOGGER.error("Cannot found account w/ number {}", accountNumber);
					return new AccountServiceException("Cannot found account w/ number " + accountNumber);
				});
	}

}
