package negocio;

import java.util.Date;

public interface VOHorario {
	public long getId();
	public Date getHoraApertura();
	public Date getHoraClausura();
	@Override
	public String toString();
}
