package negocio;

public class Establecimiento implements VOEstablecimiento{
	
	
	private long id;
	private long idEspacio;
	private long idHorario;
	private String nombre;
	private String tipo;
	private int aforomax;
	
	public Establecimiento() {
		this.id=0;
		this.idEspacio=0;
		this.idHorario=0;
		this.nombre="";
		this.tipo="";
		this.aforomax = 0;

	}
	
	public Establecimiento(long i, long iEspacio, long iHorario, String nom, String type, int aforoma)
	{
		id=i;
		idEspacio=iEspacio;
		nombre=nom;
		idHorario = iHorario;
		tipo = type;
		aforomax = aforoma;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdEspacio() {
		return idEspacio;
	}

	public void setIdEspacio(long idEspacio) {
		this.idEspacio = idEspacio;
	}

	public long getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(long idHorario) {
		this.idHorario = idHorario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

	public int getAforomax() {
		return aforomax;
	}

	public void setAforomax(int aforomax) {
		this.aforomax = aforomax;
	}

	@Override
	public String toString() {
		return "Establecimiento [id=" + id + ", idEspacio=" + idEspacio + ", idHorario=" + idHorario + ", nombre="
				+ nombre + ", tipo=" + tipo + ", aforomax=" + aforomax + "]";
	}

	
	

}
