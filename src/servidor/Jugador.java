package servidor;

import comun.Mensajes;
import servidor.exceptions.CeldaInexistenteException;
import servidor.exceptions.JuegoTerminadoException;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Representa a un jugador conectado a través de la red. Cuando un jugador se conecta, instancia esta clase y se ejecuta el método Run a través de un hilo o un {@link java.util.concurrent.Executor} que realiza lo que solicita el jugador a través del objeto compartido {@code jugable}
 */
public class Jugador implements Runnable {
	private final Socket socket;
	private final int idJugador;
	private final Jugable juego;
	private final Logger logger;
	private int jugadasRestantes;

	private final PrintWriter pw;
	private final BufferedReader br;

	public Jugador(Socket socket, int idJugador, Jugable juego) {
		this.socket = socket;
		this.idJugador = idJugador;
		this.juego = juego;
		this.jugadasRestantes = 4;
		this.logger = Logger.getLogger("Jugador" + idJugador);

		try {
			pw = new PrintWriter(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void run() {
		logger.info("Comenzando partida...");

		var disponibles = juego.getPremiosDisponibles();

		enviarMensaje(Mensajes.ID_CLIENTE + Mensajes.SEPARADOR + idJugador);
		enviarMensaje(Mensajes.PREMIOS_DISPONIBLES + Mensajes.SEPARADOR + disponibles);

		while (jugadasRestantes > 0 && !socket.isClosed()) {
			enviarMensaje(Mensajes.JUGADAS_RESTANTES + Mensajes.SEPARADOR + jugadasRestantes);
			String jugada;
			try {
				jugada = br.readLine();
			} catch (IOException e) {
				juego.abandonaJugador(idJugador);
				return;
			}

			var partes = jugada.split(Mensajes.SEPARADOR);
			if (!partes[0].equals(Mensajes.REALIZAR_JUGADA)) {
				continue;
			}

			if (partes.length != 3) {
				continue;
			}

			logger.info("Probando [" + partes[1] + ", " + partes[2] + "]");
			int coordX;
			int coordY;

			try {
				coordX = Integer.parseInt(partes[1]);
				coordY = Integer.parseInt(partes[2]);
			} catch (NumberFormatException e) {
				logger.warning("No se puede convertir coordenada a número");
				continue;
			}

			try {
				var result = juego.realizarJugada(this.idJugador, coordX, coordY);
				if (result != null) {
					enviarMensaje(Mensajes.CELDA_ACERTADA + Mensajes.SEPARADOR + result);
				} else {
					enviarMensaje(Mensajes.CELDA_FALLIDA);
				}
				this.jugadasRestantes--;
			} catch (CeldaInexistenteException e) {
				enviarMensaje(Mensajes.CELDA_INEXISTENTE + Mensajes.SEPARADOR + coordX + Mensajes.SEPARADOR + coordY);
			} catch (JuegoTerminadoException e) {
				enviarMensaje(Mensajes.PREMIOS_DISPONIBLES + Mensajes.SEPARADOR + juego.getPremiosDisponibles());
				try {
					socket.close();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				return;
			}
		}

		logger.info("Jugador al carrer");
		expulsar();
	}

	private void enviarMensaje(String mensaje) {
		pw.println(mensaje);
		pw.flush();
		logger.log(Level.FINE, ">>> " + mensaje);
	}

	public void expulsar() {
		pw.println(Mensajes.PARTIDA_FINALIZADA);
		pw.flush();
	}
}
