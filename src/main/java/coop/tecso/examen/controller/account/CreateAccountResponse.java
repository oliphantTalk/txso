package coop.tecso.examen.controller.account;

/**
 * @author nahuel.barrena on 5/4/20
 */
public class CreateAccountResponse {

	private Long accountNumber;

	public CreateAccountResponse() {
	}

	public CreateAccountResponse(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getAccountNumber() {
		return accountNumber;
	}

}
