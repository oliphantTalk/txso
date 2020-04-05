package coop.tecso.examen.exceptions;

/**
 * @author nahuel.barrena on 10/4/20
 */
public class AccountServiceException extends Exception {

	public AccountServiceException(String message) {
		super(message);
	}

	public AccountServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
