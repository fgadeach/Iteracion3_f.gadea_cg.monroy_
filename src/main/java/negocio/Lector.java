package negocio;

public class Lector implements VOLector{
	private long id;
	private String espacio;
	

	public Lector() 
	{
		this.id = 0;
		this.espacio = "";
	}
	
	public Lector(long id, String nombre) 
	{
		this.id = id;
		this.espacio = nombre;
	}
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getUbicacion() {
		return espacio;
	}


	public void setUbicacion(String ubicacion) {
		this.espacio = ubicacion;
	}

	@Override
	public String toString() {
		return "Lector [id=" + id + ", espacio=" + espacio + "]";
	}

	
}
