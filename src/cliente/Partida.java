package cliente;

import comun.Mensajes;
import comun.Premio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase principal de una partida. No se hace en el main para evitar el contexto estático.
 */
public class Partida {
	public Partida() {
	}

	public void jugar() {
		int idCliente;

		try (Socket sock = new Socket("localhost", 9696)) {
			var pw = new PrintWriter(sock.getOutputStream());
			var br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			var teclado = new Scanner(System.in);

			while (!sock.isClosed()) {
				var line = br.readLine();
				var parts = line.split(Mensajes.SEPARADOR);

				switch (parts[0]) {
					case Mensajes.ID_CLIENTE -> {
						if (parts.length != 2) {
							cantidadParametrosInvalida(line, parts.length, 2);
							continue;
						}
						idCliente = Integer.parseInt(parts[1]);
						System.out.println("Se te ha asignado el ID " + idCliente);
					}
					case Mensajes.PREMIOS_DISPONIBLES -> {
						if (parts.length != 2) {
							cantidadParametrosInvalida(line, parts.length, 2);
							continue;
						}
						var premiosDisponibles = Integer.parseInt(parts[1]);
						System.out.println("Hay " + premiosDisponibles + " premios disponibles");
					}
					case Mensajes.JUGADAS_RESTANTES -> {
						if (parts.length != 2) {
							cantidadParametrosInvalida(line, parts.length, 2);
							continue;
						}
						var jugadasDisponibles = Integer.parseInt(parts[1]);
						if (jugadasDisponibles == 0) {
							System.out.println("==== FINAL DEL JUEGO ====");
							return;
						}
						System.out.println("Hay " + jugadasDisponibles + " jugadas disponibles para usted.");
						System.out.println("Introduzca C1: ");
						var c1 = teclado.nextInt();
						System.out.println("Introduzca C2: ");
						var c2 = teclado.nextInt();

						pw.println(Mensajes.REALIZAR_JUGADA + Mensajes.SEPARADOR + c1 + Mensajes.SEPARADOR + c2);
						pw.flush();
					}
					case Mensajes.CELDA_INEXISTENTE -> {
						if (parts.length != 3) {
							cantidadParametrosInvalida(line, parts.length, 3);
							continue;
						}
						System.out.println("La casilla [" + parts[1] + ", " + parts[2] + "] no existe");
					}
					case Mensajes.CELDA_FALLIDA -> {
						System.out.println("¡Mierda! No había premio en la casilla");
					}
					case Mensajes.CELDA_ACERTADA -> {
						if (parts.length != 2) {
							cantidadParametrosInvalida(line, parts.length, 2);
							continue;
						}
						var premio = Premio.valueOf(parts[1]);
						System.out.println("¡Enhorabuena! Ganaste un flamante " + premio);
					}
					case Mensajes.PARTIDA_FINALIZADA -> {
						System.out.println("==== FINAL DEL JUEGO ====");
						return;
					}

					default -> {
						mensajeDesconocido(line);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Servidor desconectado: " + e.getMessage());
		}
	}

	private void cantidadParametrosInvalida(String mensaje, int obtenidos, int esperados) {
		System.out.println("Mensaje inválido: '" + mensaje + "'. Se obtuvieron " + obtenidos + " partes pero esperaban " + esperados);
	}

	private void mensajeDesconocido(String mensaje) {
		System.out.println("Mensaje inesperado del servidor: '" + mensaje + "'");
	}
}
