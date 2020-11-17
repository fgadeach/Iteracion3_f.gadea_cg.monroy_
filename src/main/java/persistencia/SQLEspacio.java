package persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Espacio;



public class SQLEspacio {
	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLEspacio (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}

	public long adicionarEspacio (PersistenceManager pm, long id, long idCC, String nombre, double area, String tipo, String estado) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqEspacio() + "(id, idCC, nombre, area, tipo, estado) values (?, ?, ?, ?, ?, ?, ?, ?)");
		q.setParameters(id, idCC, nombre, area, tipo, estado);
		return (long)q.executeUnique();            
	}

	public long eliminarEspacio (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqEspacio () + " WHERE id = ?");
		q.setParameters(id);
		return (long) q.executeUnique();            
	}

	public List<Espacio> darEspacios (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqEspacio ());
		q.setResultClass(Espacio.class);
		return (List<Espacio>) q.execute();
	}

	public Espacio darEspacioPorId (PersistenceManager pm, long id) 
	{

		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqEspacio() + " WHERE id = ?");
		q.setResultClass(Espacio.class);
		q.setParameters(id);
		return (Espacio) q.executeUnique();
	}

	public long cambiarEstadoEspacio(PersistenceManager pm, long idEspacio, String estado) 

	{	 String sql1 = "set transaction isolation level read serializable;";
	sql1+="UPDATE " + pp.darSeqEspacio() + " SET estado = ? WHERE id = ?";
	Query q = pm.newQuery(SQL, sql1);
	q.setParameters(estado, idEspacio);
	return (long) q.executeUnique();            
	}

	public long deshabilitarEspacio(PersistenceManager pm, long idEspacio) 
	{	 
	String sql1 = "set transaction isolation level read serializable;";
	sql1+="UPDATE " + pp.darSeqEspacio() + " SET estado = ? WHERE id = ?";
	Query q = pm.newQuery(SQL, sql1);
	q.setParameters("Deshabilitado", idEspacio);
	return (long) q.executeUnique();            
	}
	
	public long rehabilitarEspacio(PersistenceManager pm, long idEspacio) 
	{	 
	String sql1 = "set transaction isolation level read serializable;";
	sql1+="UPDATE " + pp.darSeqEspacio() + " SET estado = ? WHERE id = ?";
	Query q = pm.newQuery(SQL, sql1);
	q.setParameters("Verde", idEspacio);
	return (long) q.executeUnique();            
	}
	
	public long deshabilitarVisitante (PersistenceManager pm, long idVisitante) 
	{	
		String sql1 = "set transaction isolation level read serializable;";
		sql1+= "UPDATE " + pp.darSeqVisitante() + " SET estado = ? WHERE id = ?";
		Query q = pm.newQuery(SQL, sql1);
		q.setParameters("Naranja", idVisitante);
		return (long) q.executeUnique();            
	}

}
