package comun;

/**
 * Clase con las constantes que son utilizadas para serializar/deserializar mensajes entre clientes y servidor.
 */
public final class Mensajes {
	/**
	 * COMPARTIDO
	 * <p>
	 * Sirve para separar las distintas partes de un mensaje de red.
	 */
	public static final String SEPARADOR = "::";

	/**
	 * SERVIDOR
	 * <p>
	 * Indica cuántos premios hay disponibles (que aún no han sido acertados)
	 */
	public static final String PREMIOS_DISPONIBLES = "disponibles";

	/**
	 * SERVIDOR
	 * <p>
	 * Indica al cliente cuál es su ID.
	 */
	public static final String ID_CLIENTE = "id";

	/**
	 * SERVIDOR
	 * <p>
	 * Indica al cliente cuántas jugadas tiene restantes. Si es cero, la conexión se cierra. Si es más, el cliente DEBE enviar al servidor su jugada.
	 */
	public static final String JUGADAS_RESTANTES = "jugadasRestantes";

	/**
	 * CLIENTE
	 * <p>
	 * Realiza una jugada. Esta debe contener varios campos del siguiente modo:
	 * CODIGO_MENSAJE - SEPARADOR - COORDENADA X - SEPARADOR - COORDENADA Y
	 */
	public static final String REALIZAR_JUGADA = "realizaJugada";

	/**
	 * SERVIDOR.
	 * <p>
	 * Lanzado cuando la celda solicitada no existe. NO indica que sea incorrecto, sino que no se puede utilizar esa celda.
	 */
	public static final String CELDA_INEXISTENTE = "celdaInexistente";

	/**
	 * SERVIDOR
	 * <p>
	 * La celda adivinada tiene un premio. Debe contener el premio como segundo parámetro.
	 */
	public static final String CELDA_ACERTADA = "acierto";

	/**
	 * SERVIDOR
	 * <p>
	 * Enviado cuando la celda no tiene premio.
	 */
	public static final String CELDA_FALLIDA = "fallo";

	/**
	 * SERVIDOR
	 * <p>
	 * Enviado cuando la partida ha finalizado porque todos los premios fueron conseguidos. El servidor se desconectará.
	 */
	public static final String PARTIDA_FINALIZADA = "finalizada";
}
