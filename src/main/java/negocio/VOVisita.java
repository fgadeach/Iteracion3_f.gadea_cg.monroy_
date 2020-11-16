package negocio;

import java.util.Date;

public interface VOVisita {
	public long getId_visitante();
	public long getId_lector();
	public Date getFechaHoraEntrada();
	public Date getFechaHoraSalida();
	@Override
	public String toString();
}
