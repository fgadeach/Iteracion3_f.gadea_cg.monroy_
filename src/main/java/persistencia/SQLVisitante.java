package persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Visitante;

public class SQLVisitante {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLVisitante(PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}

	public long adicionarVisitante (PersistenceManager pm, long id, String nombre,String tipo,int numTelefono,String correo,String nomContacto,int numContacto,String estado, Double temperatura) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqVisitante() + "(id, nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		q.setParameters(id, nombre, tipo, numTelefono,correo,nomContacto,numContacto,estado, temperatura);
		return (long)q.executeUnique();            
	}

	public List<Visitante> darVisitantes (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqVisitante ());
		q.setResultClass(Visitante.class);
		return (List<Visitante>) q.execute();
	}

	public long eliminarVisitante(PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqVisitante() + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}

	public Visitante darVisitantePorIdVisitantente (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqVisitante() + " WHERE id = ?");
		q.setResultClass(Visitante.class);
		q.setParameters(id);
		return (Visitante) q.executeUnique();
	}
	
	

	public List<Visitante> darVisitasRealizadas (PersistenceManager pm, long idVisitante)
	{
		String sql = "SELECT lec.id, lece.id, lece.idEspacio, est.id, vis.id_lector";
		sql += " FROM ";
		sql += pp.darSeqVisitante() + " visi, ";
		sql += pp.darSeqEstablecimiento() + " est, ";
		sql += pp.darLectores() + " lec, ";
		sql += pp.darLectoresEspacio() + " lece, ";
		sql += pp.darVisitas() + " vis ";
		sql	+= " WHERE ";
		sql += "visi.id = ?";
		sql += " AND visi.id = vis.id_visitante";
		sql += " AND vis.id_lector = lec.id";
		sql += " AND lec.id = lece.id";
		sql += " AND lece.idEspacio = est.id";

		Query q = pm.newQuery(SQL, sql);
		q.setParameters(idVisitante);
		return (List<Visitante>)q.executeList();
	}

	public long cambiarEstadoVisitante (PersistenceManager pm, long idVisitante, String estado) 
	{	
		String sql1 = "set transaction isolation level read serializable;";
		sql1+= "UPDATE " + pp.darSeqVisitante() + " SET estado = ? WHERE id = ?";
		
		darVisitasRealizadas(pm,idVisitante);
		
		Query q = pm.newQuery(SQL, sql1);
		q.setParameters(estado, idVisitante);
		return (long) q.executeUnique();            
	}


	public List<Visitante>  darAforo (PersistenceManager pm)
	{		
		String sql1 = "SELECT idvisitante, count (*) as numVisitas";
		sql1 += " FROM " + pp.darSeqVisita();
		sql1 += " GROUP BY id_visitante";

		String sql = "SELECT id, nombre, NVL (numVisitas, 0)";
		sql += " FROM " + pp.darSeqVisitante() + " LEFT OUTER JOIN (" + sql1 + ")";
		sql += " ON id = id_visitante";
		Query q = pm.newQuery(SQL, sql);
		return (List<Visitante>) q.executeList();
	}
}
