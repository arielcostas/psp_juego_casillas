package servidor;

import servidor.exceptions.CeldaInexistenteException;
import comun.Premio;
import comun.PremioEncontrado;
import servidor.exceptions.JuegoTerminadoException;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Juego implements Jugable {
	private final PremioEncontrado[][] tablero;
	private final Map<Integer, Jugador> jugadores;
	private final AtomicInteger id = new AtomicInteger();
	private final Logger logger = Logger.getLogger("Juego");

	public Juego() {
		jugadores = new HashMap<>();
		tablero = new PremioEncontrado[3][4];

		tablero[0][0] = new PremioEncontrado(Premio.CRUCERO, null);
		tablero[1][2] = new PremioEncontrado(Premio.ENTRADAS_TEATRO, null);
		tablero[2][1] = new PremioEncontrado(Premio.MASAJE_PIERNAS, null);
		tablero[2][3] = new PremioEncontrado(Premio.MIL_EUROS, null);

		logger.info("Se ha iniciado el juego con premios en: ");
		for (PremioEncontrado[] fila : tablero) {
			logger.info(Arrays.toString(fila));
		}
	}

	public void nuevoJugador(Socket socket) {
		var jid = id.addAndGet(1);
		var jugador = new Jugador(socket, jid, this);
		jugadores.put(jid, jugador);
		logger.info("Cliente conectado desde " + socket.getInetAddress() + " con ID " + jid);

		var hilo = new Thread(jugador);
		hilo.start();
	}

	@Override
	public synchronized Premio realizarJugada(
			int jid,
			int cx,
			int cy
	) throws CeldaInexistenteException, JuegoTerminadoException {
		puedeRealizarseJugada(cx, cy);

		var celda = tablero[cx][cy];

		if (celda == null) {
			return null;
		}

		if (celda.idGanador() == null) {
			tablero[cx][cy] = new PremioEncontrado(celda.premio(), jid);
			logger.info("Usuario " + jid + " encontró premio " + celda.premio());
		} else {
			logger.info("Usuario " + jid + " ¡¡AGUA!! ");
		}

		if (getPremiosDisponibles() == 0) {
			logger.warning("No quedan premios disponibles. Finalizando.");
			finalizarParaTodos();
		}

		return tablero[cx][cy].idGanador() == jid ? tablero[cx][cy].premio() : null;
	}

	private void finalizarParaTodos() {
		for (Jugador j : jugadores.values()) {
			j.expulsar();
		}
		System.exit(0);
	}

	private void puedeRealizarseJugada(int coordenadaX, int coordenadaY) throws CeldaInexistenteException, JuegoTerminadoException {
		if (coordenadaX > tablero.length - 1) {
			logger.log(Level.WARNING, "La coordenada X " + coordenadaX + " no existe");
			throw new CeldaInexistenteException();
		}

		if (tablero[coordenadaX].length - 1 < coordenadaY) {
			logger.log(Level.WARNING, "La coordenada Y " + coordenadaY + " no existe");
			throw new CeldaInexistenteException();
		}

		if (getPremiosDisponibles() == 0) {
			throw new JuegoTerminadoException();
		}
	}

	@Override
	@SuppressWarnings("MethodWithMultipleLoops")
	public int getPremiosDisponibles() {
		int premiosDisponibles = 0;
		for (PremioEncontrado[] fila : tablero) {
			for (PremioEncontrado premioEncontrado : fila) {
				if (premioEncontrado == null) continue;
				if (premioEncontrado.idGanador() == null) {
					premiosDisponibles++;
				}
			}
		}
		return premiosDisponibles;
	}

	@Override
	public void abandonaJugador(int idJugador) {
		this.jugadores.remove(idJugador);
	}
}
