package persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Lector;

public class SQLLector {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLLector (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	public long adicionarLector (PersistenceManager pm, long id, String espacio) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqLector() + "(id, espacio) values (?, ?)");
        q.setParameters(id, espacio);
        return (long)q.executeUnique();            
	}
	
	public long eliminarLector (PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqLector () + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}

	public List<Lector> darLectors (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqLector ());
		q.setResultClass(Lector.class);
		return (List<Lector>) q.execute();
	}
	
	public Lector darLectorPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqLector() + " WHERE id = ?");
		q.setResultClass(Lector.class);
		q.setParameters(id);
		return (Lector) q.executeUnique();
	}
}
