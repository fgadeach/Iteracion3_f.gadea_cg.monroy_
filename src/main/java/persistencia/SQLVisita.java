package persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Visita;

public class SQLVisita {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLVisita (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarVisita (PersistenceManager pm, long id_visitante, long id_lector, Date FechaHoraEntrada, Date FechaHoraSalida) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqVisita() + "(id_visitante, id_lector, FechaHoraEntrada, FechaHoraSalida) values (?, ?, ?, ?)");
        q.setParameters(id_visitante, id_lector, FechaHoraEntrada, FechaHoraSalida);
        return (long)q.executeUnique();            
	}

	public List<Visita> darVisitas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqVisita ());
		q.setResultClass(Visita.class);
		return (List<Visita>) q.execute();
	}
	
	public Visita darVisitaPorIdVisitante (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqVisita() + " WHERE id_visitante = ?");
		q.setResultClass(Visita.class);
		q.setParameters(id);
		return (Visita) q.executeUnique();
	}
}
