package coop.tecso.examen.exceptions;

/**
 * @author nahuel.barrena on 10/4/20
 */
public class EntityDeleteException extends Exception {

	public EntityDeleteException(String message) {
		super(message);
	}

	public EntityDeleteException(String message, Throwable cause) {
		super(message, cause);
	}
}
