package persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.LectorCC;



public class SQLLectorCC {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLLectorCC(PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarLectorCC (PersistenceManager pm, long id, long idCC) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqLectorCC() + "(id, idCC) values (?, ?)");
        q.setParameters(id, idCC);
        return (long)q.executeUnique();            
	}
	
	public long eliminarLectorCC (PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqLectorCC () + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}

	public List<LectorCC> darLectorCCs (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqLectorCC ());
		q.setResultClass(LectorCC.class);
		return (List<LectorCC>) q.execute();
	}
	
	public LectorCC darLectorCCPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqLectorCC() + " WHERE id = ?");
		q.setResultClass(LectorCC.class);
		q.setParameters(id);
		return (LectorCC) q.executeUnique();
	}
}
