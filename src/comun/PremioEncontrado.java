package comun;

import java.io.Serializable;

public record PremioEncontrado(
		Premio premio,
		Integer idGanador
) implements Serializable {
	@Override
	public String toString() {
		return "premio=" + premio;
	}
}
