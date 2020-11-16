package negocio;

public class CentroComercial implements VOCentroComercial{

	private long id;
	private String nombre;
	private int aforoMax;
	private long id_horario;

	public CentroComercial() 
	{
		this.id = 0;
		this.nombre = "";
		this.aforoMax = 0;
		this.id_horario=0;

	}

	public CentroComercial(long id, String nombre, int aforo, long horario) 
	{
		this.id = id;
		this.nombre = nombre;
		this.aforoMax=aforo;
		this.id_horario=horario;

	}

	public long getId_horario() {
		return id_horario;
	}

	public void setId_horario(long id_horario) {
		this.id_horario = id_horario;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAforoMax() {
		return aforoMax;
	}

	public void setAforoMax(int aforoMax) {
		this.aforoMax = aforoMax;
	}

	@Override
	public String toString() {
		return "CentroComercial [id=" + id + ", nombre=" + nombre + ", aforoMax=" + aforoMax + ", id_horario="
				+ id_horario + "]";
	}

	
}
