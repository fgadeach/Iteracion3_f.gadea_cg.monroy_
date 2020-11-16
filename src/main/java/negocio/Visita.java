package negocio;

import java.util.Date;

public class Visita implements VOVisita{

	private long id_visitante;
	private long id_lector;
	private Date FechaHoraEntrada;
	private Date FechaHoraSalida;

	public Visita(){

		this.id_visitante=0;
		this.id_lector=0;
		this.FechaHoraEntrada=new Date();
		this.FechaHoraSalida=new Date();
	}

	public Visita(long idV, long idL, Date en, Date sal) {

		id_visitante=idV;
		id_lector=idL;
		FechaHoraEntrada=en;
		FechaHoraSalida=sal;
	}

	public long getId_visitante() {
		return id_visitante;
	}

	public void setId_visitante(long id_visitante) {
		this.id_visitante = id_visitante;
	}

	public long getId_lector() {
		return id_lector;
	}

	public void setId_lector(long id_lector) {
		this.id_lector = id_lector;
	}

	public Date getFechaHoraEntrada() {
		return FechaHoraEntrada;
	}

	public void setFechaHoraEntrada(Date fechaHoraEntrada) {
		FechaHoraEntrada = fechaHoraEntrada;
	}

	public Date getFechaHoraSalida() {
		return FechaHoraSalida;
	}

	public void setFechaHoraSalida(Date fechaHoraSalida) {
		FechaHoraSalida = fechaHoraSalida;
	}

	@Override
	public String toString() 
	{
		return "Visita [id_visitante=" + id_visitante + ", id_lector=" + id_lector +", FechaHoraEntrada=" + FechaHoraEntrada + ", FechaHoraSalida"+FechaHoraSalida+"]";
	}
}
