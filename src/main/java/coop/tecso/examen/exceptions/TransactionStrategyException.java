package coop.tecso.examen.exceptions;

/**
 * @author nahuel.barrena on 10/4/20
 */
public class TransactionStrategyException extends Exception {

	public TransactionStrategyException(String message) {
		super(message);
	}

	public TransactionStrategyException(String message, Throwable cause) {
		super(message, cause);
	}
}
