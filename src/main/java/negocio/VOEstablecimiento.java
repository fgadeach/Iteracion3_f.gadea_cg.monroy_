package negocio;

public interface VOEstablecimiento {
	public long getId() ;
	public long getIdEspacio() ;
	public long getIdHorario() ;
	public String getNombre() ;
	public String getTipo() ;
	public int getAforomax() ;
	
	@Override
	public String toString() ;
}
