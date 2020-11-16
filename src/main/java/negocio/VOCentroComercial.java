package negocio;

public interface VOCentroComercial {

	public long getId();
	
	public long getId_horario();
	
	public String getNombre(); 

	public int getAforoMax();
	@Override
	String toString();
}
