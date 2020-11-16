package negocio;

import java.util.Date;

public class Horario implements VOHorario{
	private long id;
	private Date horaApertura;
	private Date horaClausura;

	public Horario() {
		this.id=0;
		this.horaApertura=new Date();
		this.horaClausura=new Date();
	}
	public Horario(long i, Date d, Date da) {
		id=i;
		horaApertura=d;
		horaClausura=da;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getHoraApertura() {
		return horaApertura;
	}
	public void setHoraApertura(Date horaApertura) {
		this.horaApertura = horaApertura;
	}
	public Date getHoraClausura() {
		return horaClausura;
	}
	public void setHoraClausura(Date horaClausura) {
		this.horaClausura = horaClausura;
	}
	@Override
	public String toString() {
		return "Horario [id=" + id + ", horaApertura=" + horaApertura + ", horaClausura=" + horaClausura + "]";
	}

}
