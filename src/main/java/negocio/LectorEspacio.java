package negocio;

public class LectorEspacio implements VOLectorEspacio{
private long idLector;
private long idEspacio;

public LectorEspacio() {
	this.idLector=0;
	this.idEspacio=0;
}
public LectorEspacio(long l,long e) {
	idLector=l;
	idEspacio=e;
}
public long getIdLector() {
	return idLector;
}
public void setIdLector(long idLector) {
	this.idLector = idLector;
}
public long getIdEspacio() {
	return idEspacio;
}
public void setIdEspacio(long idEspacio) {
	this.idEspacio = idEspacio;
}
@Override
public String toString() {
	return "LectorEspacio [idLector=" + idLector + ", idEspacio=" + idEspacio + "]";
}

}
