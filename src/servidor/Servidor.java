package servidor;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.util.logging.Logger;

public class Servidor {
	private static final Logger logger = Logger.getLogger("Main");

	public static void main(String[] args) {
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tF %1$tT] [%4$-3s] %3$-8s %5$s %n");
		Juego juego = new Juego();

		var ssf = ServerSocketFactory.getDefault();
		try (var ss = ssf.createServerSocket(9696)) {
			logger.info("Escuchando en el puerto 9696");
			while(juego.getPremiosDisponibles() > 0) {
				var hilo = ss.accept();
				juego.nuevoJugador(hilo);
			}
			logger.info("Juego terminado");
		} catch (IOException e) {
			logger.warning(e.getMessage());
			throw new RuntimeException(e);
		}
		logger.info("Saliendo");
	}
}