package persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import negocio.Horario;


public class SQLHorario {

	private final static String SQL = PersistenciaAforoCC.SQL;

	private PersistenciaAforoCC pp;

	public SQLHorario (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	public long adicionarHorario (PersistenceManager pm, long id, Date horaApertura, Date horaClausura) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darSeqHorario() + "(id, horaApertura, horaClausura) values (?, ?, ?)");
        q.setParameters(id, horaApertura, horaClausura);
        return (long)q.executeUnique();            
	}

	public long eliminarHorario (PersistenceManager pm, long id) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqHorario () + " WHERE id = ?");
        q.setParameters(id);
        return (long) q.executeUnique();            
	}

	public List<Horario> darHorarios (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqHorario ());
		q.setResultClass(Horario.class);
		return (List<Horario>) q.execute();
	}
	
	public Horario darHorarioPorId (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqHorario() + " WHERE id = ?");
		q.setResultClass(Horario.class);
		q.setParameters(id);
		return (Horario) q.executeUnique();
	}
}
