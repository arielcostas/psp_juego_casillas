package servidor.exceptions;

public class JuegoTerminadoException extends Exception {
	public JuegoTerminadoException() {
		super("El juego ya ha terminado");
	}
}
