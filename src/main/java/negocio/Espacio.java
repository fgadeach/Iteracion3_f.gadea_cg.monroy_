package negocio;

public class Espacio implements VOEspacio{

	private long id;
	private long idCC;
	private String nombre;
	private double area;
	private String tipo;
	private String estado;
	
	public Espacio() {
		this.id=0;
		this.idCC=0;
		this.area=0;
		this.nombre="";
		this.tipo="";
		this.estado="";
	}
	
	public Espacio(long i, long icc, String nom, double amax, String t,String e)
	{
		id=i;
		idCC=icc;
		nombre=nom;
		area = amax;
		tipo = t;
		estado=e;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCC() {
		return idCC;
	}

	public void setIdCC(long idCC) {
		this.idCC = idCC;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Espacio [id=" + id + ", idCC=" + idCC + ", nombre=" + nombre + ", area=" + area + ", tipo=" + tipo
				+ ", estado=" + estado + "]";
	}


}
