package comun;

import java.io.Serializable;

public record Resultado(
		boolean crucero,
		boolean entradasTeatro,
		boolean masajePiernas,
		boolean milEuros
) implements Serializable {
}
