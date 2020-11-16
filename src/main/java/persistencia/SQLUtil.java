package persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLUtil {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAforoCC.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAforoCC pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil (PersistenciaAforoCC pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT " + pp.darSeqAforoCC() + ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarAforo (PersistenceManager pm)
	{
		Query qCarnet= pm.newQuery(SQL, "DELETE FROM " + pp.darSeqCarnet());          
        Query qHorario = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqHorario());
        Query qLectorCC = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqLectorCC());
        Query qLectorEspacio = pm.newQuery(SQL, "DELETE FROM " + pp.darLectoresEspacio());
        Query qEspacio = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqEspacio());
        Query qVisita = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqVisita());
        Query qVisitante = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqVisitante());
        Query qCentroComercial = pm.newQuery(SQL, "DELETE FROM " + pp.darSeqCC());

        long carnetEliminados = (long) qCarnet.executeUnique ();
        long horarioEliminados = (long) qHorario.executeUnique ();
        long lectorCCEliminadas = (long) qLectorCC.executeUnique ();
        long lectorEspacioEliminadas = (long) qLectorEspacio.executeUnique ();
        long espacioEliminados = (long) qEspacio.executeUnique ();
        long visitaEliminados = (long) qVisita.executeUnique ();
        long visitanteEliminados = (long) qVisitante.executeUnique ();
        long ccEliminados = (long) qCentroComercial.executeUnique ();
        
        return new long[] {carnetEliminados, horarioEliminados, lectorCCEliminadas, lectorEspacioEliminadas, 
        		espacioEliminados, visitaEliminados, visitanteEliminados,ccEliminados};
	}
}
