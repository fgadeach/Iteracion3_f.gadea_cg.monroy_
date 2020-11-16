package negocio;

public interface VOVisitante {
	public long getId();
	public String getNombre();
	public String getTipo();
	public int getNumTelefono();
	public String getCorreo();
	public String getNomContacto();
	public int getNumContacto();
	public String getEstado();
	@Override
	public String toString();
}
