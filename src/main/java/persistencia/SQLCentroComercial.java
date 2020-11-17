package persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.CentroComercial;

public class SQLCentroComercial {
	
	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLCentroComercial (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarCentroComercial (PersistenceManager pm, long id, String nombre,int aforoMax,long id_horario) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqCC() + "(id, nombre, aforoMax, idhorario) values (?, ?, ?, ?)");
        q.setParameters(id, nombre, aforoMax, id_horario);
        return (long)q.executeUnique();            
	}
	
	public List<CentroComercial> darCentroComerciales (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqCC ());
		q.setResultClass(CentroComercial.class);
		return (List<CentroComercial>) q.execute();
	}
	
	public long eliminarCentroComercial (PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqCC () + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}
	
	public CentroComercial darCCPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqCC() + " WHERE id = ?");
		q.setResultClass(CentroComercial.class);
		q.setParameters(id);
		return (CentroComercial) q.executeUnique();
	}
	
	public long cambiarHorarioCC(PersistenceManager pm, long idBebedor, long id_horario) 
	{
		 Query q = pm.newQuery(SQL, "UPDATE " + pp.darSeqCC() + " SET id_horario = ? WHERE id = ?");
	     q.setParameters(id_horario, idBebedor);
	     return (long) q.executeUnique();            
	}	
	
	
}
