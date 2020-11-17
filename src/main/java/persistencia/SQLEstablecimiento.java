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

	public List<Establecimiento> establecimientoConAforoDisponible (PersistenceManager pm)
	{		
		String sql1 = "SELECT est.nombre, lece.idEspacio, lece.id, vis.id_lector";
		sql1 += " FROM ";
		sql1+= pp.darSeqEstablecimiento() + " est, ";
		sql1 += pp.darLectoresEspacio() + " lece, ";
		sql1 += pp.darVisitas() + " vis ";
		sql1+=" WHERE ";
		sql1 += "AND est.id = lece.idEspacio";
		sql1 += "AND lece.id = vis.id_lector";
		sql1 += "AND vis.id_lector = lec.id";
		sql1 += "AND est.aforo > COUNT(vis.id_visitante)";
		
		Query q = pm.newQuery(SQL, sql1);
		return (List<Establecimiento>) q.executeList();
	}
}
