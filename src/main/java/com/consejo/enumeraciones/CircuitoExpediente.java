package com.consejo.enumeraciones;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CircuitoExpediente {

	INGRESO {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(INGRESO, COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
					COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL, AMBAS_COMISIONES, ARCHIVO, PRESIDENCIA, BLOQUE_A,
					BLOQUE_B, BLOQUE_C, TODOS_LOS_BLOQUES);
		}
	},

	OFICINA_PARLAMENTARIA {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(OFICINA_PARLAMENTARIA, COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
					COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL, AMBAS_COMISIONES, ARCHIVO, PRESIDENCIA, BLOQUE_A,
					BLOQUE_B, BLOQUE_C, TODOS_LOS_BLOQUES, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	AMBAS_COMISIONES {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(AMBAS_COMISIONES, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	ARCHIVO {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(ARCHIVO, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	PRESIDENCIA {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(PRESIDENCIA, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	BLOQUE_A {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	BLOQUE_B {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(BLOQUE_A, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	BLOQUE_C {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(BLOQUE_C, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	TODOS_LOS_BLOQUES {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(TODOS_LOS_BLOQUES, DESPACHOS_DE_COMISION, NOTAS_DE_COMISION);
		}
	},

	DESPACHOS_DE_COMISION {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(DESPACHOS_DE_COMISION, LEGISLACION);
		}
	},

	LEGISLACION {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(LEGISLACION, FIN);
		}
	},

	NOTAS_DE_COMISION {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(NOTAS_DE_COMISION, REPUESTA_AL_CIUDADANO, NOTA_AL_MUNICIPIO);
		}
	},

	REPUESTA_AL_CIUDADANO {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(REPUESTA_AL_CIUDADANO, FIN);
		}
	},

	NOTA_AL_MUNICIPIO {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(NOTA_AL_MUNICIPIO, REPUESTA_DEL_MUNICIPIO, FIN);
		}
	},

	REPUESTA_DEL_MUNICIPIO {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Arrays.asList(REPUESTA_DEL_MUNICIPIO, OFICINA_PARLAMENTARIA, FIN);
		}
	},

	FIN {
		@Override
		public List<CircuitoExpediente> getEstadosValidos() {
			return Collections.emptyList();
		}
	};

	public abstract List<CircuitoExpediente> getEstadosValidos();
}