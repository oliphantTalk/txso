package coop.tecso.examen.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.account.AccountCurrencyEnum;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.dto.movement.MovementType;
import coop.tecso.examen.exceptions.AccountServiceException;
import coop.tecso.examen.exceptions.TransactionStrategyException;
import coop.tecso.examen.model.account.AccountEntity;
import coop.tecso.examen.model.movement.MovementEntity;
import coop.tecso.examen.repository.IAccountRepository;
import coop.tecso.examen.service.account.IAccountService;
import coop.tecso.examen.service.account.impl.AccountServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author nahuel.barrena on 10/4/20
 */
public class AccountServiceTest {

	private static final Long ACCOUNT_NUMBER_W_MOV = Long.valueOf("12345");
	private static final Long ACCOUNT_NUMBER_W_NO_MOV = Long.valueOf("98765");
	private static final Long WRONG_ACCOUNT = Long.valueOf("11111");
	private static final Long ACCOUNT_USD = Long.valueOf("223344");
	private static final Long ACCOUNT_EUR = Long.valueOf("554433");

	IAccountRepository repository;

	IAccountService accountService;

	@Before
	public void setUp() throws Exception {
		repository = mock(IAccountRepository.class);
		mockRepository();
		accountService = new AccountServiceImpl(repository);
	}

	@Test
	public void testCreateAccount_shouldSuccess() throws Exception {
		Account accountWNoMov = new Account();
		accountWNoMov.setCurrency(AccountCurrencyEnum.ARS);
		accountWNoMov.setBalance(BigDecimal.valueOf(15000));
		accountWNoMov.setAccountNumber(ACCOUNT_NUMBER_W_NO_MOV);
		Account createdAccount = accountService.createAccount(accountWNoMov);
		verify(repository, times(1)).save(any(AccountEntity.class));
		assertEquals(accountWNoMov.getAccountNumber(), createdAccount.getAccountNumber());
		assertEquals(accountWNoMov.getBalance(), createdAccount.getBalance());
		assertEquals(accountWNoMov.getCurrency(), createdAccount.getCurrency());
		assertEquals(0, createdAccount.getMovements().size());
	}

	@Test
	public void testGetAccount_shouldSuccess() throws AccountServiceException {
		Account withMov = accountService.getAccount(ACCOUNT_NUMBER_W_MOV);
		Account withNoMov = accountService.getAccount(ACCOUNT_NUMBER_W_NO_MOV);
		Account expectedWNoMov = buildAccountWithNoMovements(AccountCurrencyEnum.ARS).toDto();
		Account expectedWMov = buildAccountWithMovements().toDto();
		verify(repository, times(2)).findAccountEntityByAccountNumber(anyLong());
		assertNotNull(withMov);
		assertNotNull(withNoMov);
		assertEquals(expectedWNoMov.getAccountNumber(), withNoMov.getAccountNumber());
		assertEquals(expectedWNoMov.getBalance(), withNoMov.getBalance());
		assertEquals(expectedWNoMov.getCurrency(), withNoMov.getCurrency());
		assertEquals(expectedWMov.getAccountNumber(), withMov.getAccountNumber());
		assertEquals(expectedWMov.getBalance(), withMov.getBalance());
		assertEquals(expectedWMov.getCurrency(), withMov.getCurrency());
	}

	@Test
	public void testGetAccountList() throws Exception {
		List<Account> accountList = accountService.getAccountList();
		assertEquals(2, accountList.size());
	}

	@Test(expected = AccountServiceException.class)
	public void testGetAccount_shouldFail() throws Exception {
		accountService.getAccount(WRONG_ACCOUNT);
	}

	@Test
	public void testDeleteAccount_shouldSuccess() throws Exception {
		Account account = buildAccountWithNoMovements(AccountCurrencyEnum.ARS).toDto();
		accountService.deleteAccount(account.getAccountNumber());
		verify(repository, times(1)).delete(any(AccountEntity.class));
	}

	@Test(expected = AccountServiceException.class)
	public void testDeleteAccount_shouldFail() throws Exception {
		Account withMov = buildAccountWithMovements().toDto();
		accountService.deleteAccount(withMov.getAccountNumber());
	}

	@Test
	public void getAccountMovements_shouldSuccess() throws Exception {
		List<Movement> accountMovementList = accountService.getAccountMovementList(ACCOUNT_NUMBER_W_MOV);
		assertEquals(2, accountMovementList.size());
		assertEquals(ACCOUNT_NUMBER_W_MOV, accountMovementList.get(0).getAccount().getAccountNumber());
		assertEquals(ACCOUNT_NUMBER_W_MOV, accountMovementList.get(1).getAccount().getAccountNumber());
		List<Movement> withNoMov = accountService.getAccountMovementList(ACCOUNT_NUMBER_W_NO_MOV);
		assertEquals(0, withNoMov.size());
	}

	@Test
	public void addAccountMovement_shouldSuccess() throws Exception {
		Movement debit = new Movement(MovementType.DEBIT, "Description 1", BigDecimal.TEN);
		Movement credit = new Movement(MovementType.CREDIT, "Description 2", BigDecimal.valueOf(100));
		Account account = accountService.addAccountMovement(ACCOUNT_NUMBER_W_NO_MOV, debit);
		assertEquals(1, account.getMovements().size());
		assertEquals(debit.getMovementType(), account.getMovements().get(0).getMovementType());
		assertEquals(debit.getDescription(), account.getMovements().get(0).getDescription());
		assertEquals(debit.getAmount(), account.getMovements().get(0).getAmount());
		Account account2 = accountService.addAccountMovement(ACCOUNT_NUMBER_W_NO_MOV, credit);
		assertEquals(2, account2.getMovements().size());
		assertEquals(credit.getMovementType(), account2.getMovements().get(1).getMovementType());
		assertEquals(credit.getDescription(), account2.getMovements().get(1).getDescription());
		assertEquals(credit.getAmount(), account2.getMovements().get(1).getAmount());
	}

	@Test(expected = TransactionStrategyException.class)
	public void addAccountMovementARSDebit_shouldFail_becauseUndercover() throws Exception {
		Movement debit = new Movement(MovementType.DEBIT, "Description 1", BigDecimal.valueOf(12100));
		accountService.addAccountMovement(ACCOUNT_NUMBER_W_NO_MOV, debit);
	}

	@Test(expected = TransactionStrategyException.class)
	public void addAccountMovementARSCredit_shouldFail_becauseUndercover() throws Exception {
		Movement credit = new Movement(MovementType.CREDIT, "Description 1", BigDecimal.valueOf(15000));
		accountService.addAccountMovement(ACCOUNT_NUMBER_W_NO_MOV, credit);
	}

	@Test(expected = TransactionStrategyException.class)
	public void addAccountMovementUSDDebit_shouldFail_becauseUndercover() throws Exception {
		Movement debit = new Movement(MovementType.DEBIT, "Description 1", BigDecimal.valueOf(12100));
		accountService.addAccountMovement(ACCOUNT_USD, debit);
	}

	@Test(expected = TransactionStrategyException.class)
	public void addAccountMovementUSDCredit_shouldFail_becauseUndercover() throws Exception {
		Movement credit = new Movement(MovementType.CREDIT, "Description 1", BigDecimal.valueOf(15000));
		accountService.addAccountMovement(ACCOUNT_USD, credit);
	}

	@Test(expected = TransactionStrategyException.class)
	public void addAccountMovementEURDebit_shouldFail_becauseUndercover() throws Exception {
		Movement debit = new Movement(MovementType.DEBIT, "Description 1", BigDecimal.valueOf(12100));
		accountService.addAccountMovement(ACCOUNT_EUR, debit);
	}

	@Test(expected = TransactionStrategyException.class)
	public void addAccountMovementEURCredit_shouldFail_becauseUndercover() throws Exception {
		Movement credit = new Movement(MovementType.CREDIT, "Description 1", BigDecimal.valueOf(15000));
		accountService.addAccountMovement(ACCOUNT_EUR, credit);
	}

	private AccountEntity buildAccountWithMovements() {
		AccountEntity account = new AccountEntity();
		account.setAccountNumber(ACCOUNT_NUMBER_W_MOV);
		account.setBalance(BigDecimal.valueOf(15000));
		account.setCurrency(AccountCurrencyEnum.ARS);
		MovementEntity movement = new MovementEntity();
		movement.setAmount(BigDecimal.TEN);
		movement.setMovementType(MovementType.DEBIT);
		movement.setDescription("Movimiento de debito 10 pesos");
		movement.setCreatedOn(LocalDateTime.now());
		MovementEntity movement1 = new MovementEntity();
		movement1.setAmount(BigDecimal.valueOf(1000));
		movement1.setMovementType(MovementType.CREDIT);
		movement1.setDescription("Movimiento de credito 1000 pesos");
		movement1.setCreatedOn(LocalDateTime.now());
		account.addMovement(movement);
		account.addMovement(movement1);
		return account;
	}

	private AccountEntity buildAccountWithNoMovements(AccountCurrencyEnum currency) {
		AccountEntity account = new AccountEntity();
		account.setAccountNumber(ACCOUNT_NUMBER_W_NO_MOV);
		account.setBalance(BigDecimal.valueOf(12000));
		account.setCurrency(AccountCurrencyEnum.ARS);
		List<MovementEntity> movements = new ArrayList<>();
		account.setMovements(movements);
		return account;
	}

	private void mockRepository() {
		AccountEntity accountWithMovements = buildAccountWithMovements();
		AccountEntity accountWithNoMovements = buildAccountWithNoMovements(AccountCurrencyEnum.ARS);
		doNothing().when(repository).delete(accountWithNoMovements);
		doThrow(new RuntimeException()).when(repository).delete(accountWithMovements);
		when(repository.save(any(AccountEntity.class))).thenAnswer(answer -> answer.getArgument(0));
		when(repository.findAccountEntityByAccountNumber(anyLong())).thenReturn(Optional.of(new AccountEntity()));
		when(repository.findAccountEntityByAccountNumber(eq(WRONG_ACCOUNT))).thenReturn(Optional.empty());
		when(repository.findAccountEntityByAccountNumber(eq(ACCOUNT_NUMBER_W_MOV))).thenReturn(Optional.of(accountWithMovements));
		when(repository.findAccountEntityByAccountNumber(eq(ACCOUNT_NUMBER_W_NO_MOV))).thenReturn(Optional.of(accountWithNoMovements));
		when(repository.findAccountEntityByAccountNumber(eq(ACCOUNT_USD))).thenReturn(Optional.of(buildAccountWithNoMovements(AccountCurrencyEnum.USD)));
		when(repository.findAccountEntityByAccountNumber(eq(ACCOUNT_EUR))).thenReturn(Optional.of(buildAccountWithNoMovements(AccountCurrencyEnum.EUR)));
		when(repository.findAll()).thenReturn(Arrays.asList(accountWithMovements, accountWithNoMovements));
	}

}

