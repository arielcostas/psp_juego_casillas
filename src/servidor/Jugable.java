package servidor;

import comun.Premio;
import servidor.exceptions.CeldaInexistenteException;
import servidor.exceptions.JuegoTerminadoException;

public interface Jugable {
	/**
	 * Realiza una jugada en nombre de un jugador
	 * @param jid El ID del jugador que la realiza
	 * @param coordenadaX coordenada X (la primera)
	 * @param coordenadaY coordenada Y (la segunda)
	 * @return Si se ha acertado
	 * @throws CeldaInexistenteException Si se juega una celda inexistente
	 */
	Premio realizarJugada(int jid, int coordenadaX, int coordenadaY) throws CeldaInexistenteException, JuegoTerminadoException;

	/**
	 * Obtiene cuántos premios hay disponibles
	 * @return La cantidad de premios disponibles (siempre >= 0)
	 */
	int getPremiosDisponibles();

	void abandonaJugador(int idJugador);
}
