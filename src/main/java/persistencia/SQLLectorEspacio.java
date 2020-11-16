package persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.LectorEspacio;

public class SQLLectorEspacio {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLLectorEspacio (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarLectorEspacio (PersistenceManager pm, long id, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqLectorEspacio() + "(id, idEspacio) values (?, ?)");
        q.setParameters(id, idEspacio);
        return (long)q.executeUnique();            
	}
	
	public long eliminarLectorEspacio (PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqLectorEspacio () + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}

	public List<LectorEspacio> darLectorEspacios (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqLectorEspacio ());
		q.setResultClass(LectorEspacio.class);
		return (List<LectorEspacio>) q.execute();
	}
	
	public LectorEspacio darLectorEspacioPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqLectorEspacio() + " WHERE id = ?");
		q.setResultClass(LectorEspacio.class);
		q.setParameters(id);
		return (LectorEspacio) q.executeUnique();
	}
}
