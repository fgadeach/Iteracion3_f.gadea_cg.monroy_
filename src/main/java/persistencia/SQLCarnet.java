package persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Carnet;


public class SQLCarnet 
{
	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;
	
	public SQLCarnet (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarCarnet (PersistenceManager pm, long id, long idVisitante) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqCarnet() + "(id, idvisitante) values (?, ?)");
        q.setParameters(id, idVisitante);
        return (long)q.executeUnique();            
	}

	public long eliminarCarnet (PersistenceManager pm, long id, long idVisitante) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqCarnet () + " WHERE id = ? AND idVisitante = ?");
        q.setParameters(id, idVisitante);
        return (long) q.executeUnique();            
	}

	public List<Carnet> darCarnets (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqCarnet ());
		q.setResultClass(Carnet.class);
		return (List<Carnet>) q.execute();
	}
	
	public Carnet darCarnetPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqCarnet() + " WHERE id = ?");
		q.setResultClass(Carnet.class);
		q.setParameters(id);
		return (Carnet) q.executeUnique();
	}
	
	
}
