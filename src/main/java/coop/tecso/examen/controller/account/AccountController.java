package coop.tecso.examen.controller.account;

import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import coop.tecso.examen.dto.account.Account;
import coop.tecso.examen.dto.movement.Movement;
import coop.tecso.examen.exceptions.AccountServiceException;
import coop.tecso.examen.exceptions.TransactionStrategyException;
import coop.tecso.examen.service.account.IAccountService;

/**
 * @author nahuel.barrena on 4/4/20
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private final IAccountService accountService;

	public AccountController(IAccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping(value = "/{account_number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> getAccount(@PathVariable(name = "account_number") Long accountNumber) {
		try {
			LOGGER.info("");
			return ResponseEntity.ok(accountService.getAccount(accountNumber));
		} catch (AccountServiceException e) {
			LOGGER.error("Account number {} was not found", accountNumber);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Account>> getAccountList(){
		LOGGER.info("Getting list of accounts.");
		List<Account> accountList = accountService.getAccountList();
		LOGGER.info("Found {} accounts", accountList.size());
		return ResponseEntity.ok(accountList);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
		LOGGER.info("Creating account for request: {}", createAccountRequest);
		Account createdAccount = accountService.createAccount(new Account(createAccountRequest.getAccountCurrency(), createAccountRequest.getBalance()));
		return ResponseEntity.ok(new CreateAccountResponse(createdAccount.getAccountNumber()));
	}

	@DeleteMapping(value = "/delete/{account_number}")
	public ResponseEntity<Void> deleteAccount(@PathVariable(name = "account_number") Long accountNumber){
		try {
			LOGGER.info("Trying to delete account w/ number: {}", accountNumber);
			accountService.deleteAccount(accountNumber);
			LOGGER.info("Account w/ number: {} was deleted successfully", accountNumber);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AccountServiceException e) {
			LOGGER.error("There was an error deleting account w/ number {}", accountNumber);
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
		}
	}

	//MOVEMENTS

	@PutMapping(value = "/{account_number}/movement/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> addAccountMovement(@PathVariable(name = "account_number") Long accountNumber,
			@Valid @RequestBody AddAccountMovementRequest addAccountMovementRequest) {
		try {
			LOGGER.info("Adding movement {} to account {}", addAccountMovementRequest, accountNumber);
			Account account = accountService.addAccountMovement(accountNumber,
					new Movement(
							addAccountMovementRequest.getMovementType(),
							addAccountMovementRequest.getDescription(),
							addAccountMovementRequest.getAmount()
					)
			);
			return ResponseEntity.ok(account);
		} catch (AccountServiceException e) {
			LOGGER.error("Account number {} was not found", accountNumber);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong account number", e);
		} catch (TransactionStrategyException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot perform transaction. Uncover limit exceeded.", e);
		}
	}

	@GetMapping(value = "/{account_number}/movement/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Movement>> getAccountMovement(@PathVariable(name = "account_number") Long accountNumber){
		try {
			LOGGER.info("Getting all account {} movements", accountNumber);
			return ResponseEntity.ok(accountService.getAccountMovementList(accountNumber));
		} catch (AccountServiceException e) {
			LOGGER.error("Account number {} was not found", accountNumber);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong account number", e);
		}
	}

}
