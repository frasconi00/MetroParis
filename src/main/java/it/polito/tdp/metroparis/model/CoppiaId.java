package it.polito.tdp.metroparis.model;

public class CoppiaId {
	private int idPartenza;
	private int idArrivo;
	
	public CoppiaId(int idPartenza, int idArrivo) {
		this.idPartenza = idPartenza;
		this.idArrivo = idArrivo;
	}

	public int getIdPartenza() {
		return idPartenza;
	}

	public void setIdPartenza(int idPartenza) {
		this.idPartenza = idPartenza;
	}

	public int getIdArrivo() {
		return idArrivo;
	}

	public void setIdArrivo(int idArrivo) {
		this.idArrivo = idArrivo;
	}
	
	

}
