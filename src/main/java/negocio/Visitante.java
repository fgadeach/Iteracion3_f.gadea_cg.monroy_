package negocio;

public class Visitante implements VOVisitante{
	private long id;
	private String nombre;
	private String tipo;
	private int numTelefono;
	private String correo;
	private String nomContacto;
	private int numContacto;
	private String estado;
	private double temperatura;

	public Visitante(){
		this.id =0;
		this.nombre ="";
		this.tipo="";
		this.numTelefono =0;
		this.correo="";
		this.nomContacto ="";
		this.numContacto =0;
		this.estado ="";
		this.temperatura=0;
	}

	public Visitante(long idt, String nom, String tipop, int numTel, String correop, String nomC, int numC, String estadop, double temp) {
		id=idt;
		nombre=nom;
		tipo =tipop;
		numTelefono=numTel;
		correo=correop;
		nomContacto=nomC;
		numContacto=numC;
		estado=estadop;
		temperatura = temp;
		
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNumTelefono() {
		return numTelefono;
	}

	public void setNumTelefono(int numTelefono) {
		this.numTelefono = numTelefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNomContacto() {
		return nomContacto;
	}

	public void setNomContacto(String nomContacto) {
		this.nomContacto = nomContacto;
	}

	public int getNumContacto() {
		return numContacto;
	}

	public void setNumContacto(int numContacto) {
		this.numContacto = numContacto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	

	public double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}

	@Override
	public String toString() {
		return "Visitante [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", numTelefono=" + numTelefono
				+ ", correo=" + correo + ", nomContacto=" + nomContacto + ", numContacto=" + numContacto + ", estado="
				+ estado + ", temperatura=" + temperatura + "]";
	}
}
