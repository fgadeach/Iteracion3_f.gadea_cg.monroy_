package negocio;

public interface VOEspacio {
	public long getId();
	public long getIdCC();
	public double getArea();
	public String getNombre();
	public String getTipo();
	public String getEstado();
	
	@Override
	public String toString(); 
}
