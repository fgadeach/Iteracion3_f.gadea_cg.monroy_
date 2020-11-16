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
	
	public Visitante darVisitantePorIdVisitantente (PersistenceManager pm, long id) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darSeqVisitante() + " WHERE id = ?");
		q.setResultClass(Visitante.class);
		q.setParameters(id);
		return (Visitante) q.executeUnique();
	}
}
