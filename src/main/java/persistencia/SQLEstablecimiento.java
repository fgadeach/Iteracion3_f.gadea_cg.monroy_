package persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Establecimiento;

public class SQLEstablecimiento {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLEstablecimiento (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarEstablecimiento (PersistenceManager pm, long i, long iEspacio, long iHorario, String nom, String type, int aforo) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqEstablecimiento() + "(id, idEspacio, idHorario, nombre, tipo, aforo) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(i, iEspacio, iHorario, nom, type, aforo);
        return (long)q.executeUnique();            
	}

	public long eliminarEstablecimiento (PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqEstablecimiento () + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}

	public List<Establecimiento> darEstablecimientos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqEstablecimiento());
		q.setResultClass(Establecimiento.class);
		return (List<Establecimiento>) q.execute();
	}
	
	public Establecimiento darEstablecimientoPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqEstablecimiento() + " WHERE id = ?");
		q.setResultClass(Establecimiento.class);
		q.setParameters(id);
		return (Establecimiento) q.executeUnique();
	}
}
