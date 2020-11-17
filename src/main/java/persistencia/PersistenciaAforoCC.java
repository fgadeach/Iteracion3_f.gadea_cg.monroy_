package persistencia;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import negocio.Carnet;
import negocio.CentroComercial;
import negocio.Espacio;
import negocio.Establecimiento;
import negocio.Horario;
import negocio.Lector;
import negocio.LectorCC;
import negocio.LectorEspacio;
import negocio.Visita;
import negocio.Visitante;

public class PersistenciaAforoCC {

	private static PersistenciaAforoCC instance;

	private static Logger log = Logger.getLogger(PersistenciaAforoCC.class.getName());

	public final static String SQL = "javax.jdo.query.SQL";

	private PersistenceManagerFactory pmf;
	private List <String> tablas;
	private SQLUtil sqlUtil;

	private SQLCarnet sqlcarnet;
	private SQLCentroComercial sqlCC;
	private SQLEspacio sqlespacio;
	private SQLEstablecimiento sqlestablecimiento;
	private SQLHorario sqlhorario;
	private SQLLector sqllector;
	private SQLLectorCC sqllectorcc;
	private SQLLectorEspacio sqllectorespacio;
	private SQLVisita sqlvisita;
	private SQLVisitante sqlvisitante;


	private PersistenciaAforoCC()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		crearClasesSQL ();

		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("AforoCC_sequence");
		tablas.add ("CARNET");
		tablas.add("CENTROCOMERCIAL");
		tablas.add("ESPACIO");
		tablas.add("HORARIO");
		tablas.add("LECTOR");
		tablas.add("LECTORCC");
		tablas.add("LECTORESPACIO");
		tablas.add("VISITA");
		tablas.add("VISITANTE");
		tablas.add("ESTABLECIMIENTO");

	}

	private PersistenciaAforoCC (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);

		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAforoCC getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaAforoCC ();
		}
		return instance;
	}

	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAforoCC getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaAforoCC (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}

	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}

		return resp;
	}

	private void crearClasesSQL ()
	{
		sqlcarnet = new SQLCarnet(this);
		sqlCC = new SQLCentroComercial(this);
		sqlespacio = new SQLEspacio(this);
		sqlhorario = new SQLHorario(this);
		sqllector = new SQLLector(this);
		sqllectorcc = new SQLLectorCC(this);
		sqllectorespacio = new SQLLectorEspacio(this);
		sqlvisita = new SQLVisita(this);
		sqlvisitante = new SQLVisitante(this);
		sqlestablecimiento = new SQLEstablecimiento(this);
	}


	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de parranderos
	 */
	public String darSeqAforoCC ()
	{
		return tablas.get (0);
	}

	public String darSeqCarnet()
	{
		return tablas.get(1);
	}
	public String darSeqCC()
	{
		return tablas.get(2);
	}
	public String darSeqEspacio()
	{
		return tablas.get (3);
	}
	public String darSeqHorario()
	{
		return tablas.get (4);
	}
	public String darSeqLector()
	{
		return tablas.get (5);
	}
	public String darSeqLectorCC()
	{
		return tablas.get (6);
	}
	public String darSeqLectorEspacio()
	{
		return tablas.get (7);
	}
	public String darSeqVisita()
	{
		return tablas.get (8);
	}
	public String darSeqVisitante()
	{
		return tablas.get (9);
	}
	public String darSeqEstablecimiento()
	{
		return tablas.get (10);
	}

	private long nextval ()
	{
		long resp = sqlUtil.nextval (pmf.getPersistenceManager());
		log.trace ("Generando secuencia: " + resp);
		return resp;
	}

	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los CARNET
	 *****************************************************************/

	public Carnet adicionarCarnet(long idVisitante) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long idcarnet = nextval ();
			long tuplasInsertadas = sqlcarnet.adicionarCarnet(pm, idcarnet, idVisitante);
			tx.commit();

			log.trace ("Inserción carnet: " + idcarnet + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Carnet (idcarnet, idVisitante);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarCarnetPorId (long id, long idVisitante) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlcarnet.eliminarCarnet(pm, id, idVisitante);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Carnet> darCarnets()
	{
		return sqlcarnet.darCarnets(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar los Centro Comerciales
	 *****************************************************************/

	public CentroComercial adicionarCC( String nombre, int aforo, long horario, int aforoAct, String estado) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long id = nextval ();
			long tuplasInsertadas = sqlCC.adicionarCentroComercial(pm, id, nombre, aforo, horario);
			tx.commit();

			log.trace ("Inserción del Centro Comercial: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
			return new CentroComercial (id, nombre, aforo, horario);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarCCPorId (long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlCC.eliminarCentroComercial(pm, id);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<CentroComercial> darCCs()
	{
		return sqlCC.darCentroComerciales(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar los ESPACIOS
	 *****************************************************************/

	public Espacio adicionarEspacio( long icc, String nom, double area, String t, String estado) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long id = nextval ();
			long tuplasInsertadas = sqlespacio.adicionarEspacio(pm, id, icc, nom, area, t, estado);
			tx.commit();

			log.trace ("Inserción del Espacio: " + nom + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Espacio(id, icc, nom, area, t, estado);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarEspacioPorId (long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlespacio.eliminarEspacio(pm, id);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Espacio> darEspacios()
	{
		return sqlespacio.darEspacios(pmf.getPersistenceManager());
	}
	
	public long cambiarEstadoEspacio(long id, String estado)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlespacio.cambiarEstadoEspacio(pm, id, estado);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	
	public long cambiarEstadoNarajnaEspacio(long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlespacio.deshabilitarEspacio(pm, id);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	
	public long rehabilitarEspacio(long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlespacio.rehabilitarEspacio(pm, id);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/* ****************************************************************
	 * 			Métodos para manejar los Horarios
	 *****************************************************************/

	public Horario adicionarHorario( Date d, Date da) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long id = nextval ();
			long tuplasInsertadas = sqlhorario.adicionarHorario(pm, id, d, da);
			tx.commit();

			log.trace ("Inserción del Horario: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Horario(id, d, da);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarHorairoPorId (long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlhorario.eliminarHorario(pm, id);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Horario> darHorarios()
	{
		return sqlhorario.darHorarios(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar los lectores
	 *****************************************************************/

	public Lector adicionarLector( String espacio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long id = nextval ();
			long tuplasInsertadas = sqllector.adicionarLector(pm, id, espacio);
			tx.commit();

			log.trace ("Inserción del lector: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Lector(id, espacio);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarLectorPorId (long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqllector.eliminarLector(pm, id);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Lector> darLectores()
	{
		return sqllector.darLectors(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar los lectores del Centro Comercial
	 *****************************************************************/

	public LectorCC adicionarLectorCC( long id_lector, long id_cc) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long tuplasInsertadas = sqllectorcc.adicionarLectorCC(pm, id_lector, id_cc);
			tx.commit();

			log.trace ("Inserción del lector: " + id_lector + ": " + tuplasInsertadas + " tuplas insertadas");
			return new LectorCC(id_lector, id_cc);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarLectorCCPorId (long id_lector) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqllectorcc.eliminarLectorCC(pm, id_lector);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<LectorCC> darLectoresCC()
	{
		return sqllectorcc.darLectorCCs(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar los lectores un espacio
	 *****************************************************************/

	public LectorEspacio adicionarLectorEspacio( long id_lector, long id_espacio) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long tuplasInsertadas = sqllectorespacio.adicionarLectorEspacio(pm, id_lector, id_espacio);
			tx.commit();

			log.trace ("Inserción del lector: " + id_lector + ": " + tuplasInsertadas + " tuplas insertadas");
			return new LectorEspacio(id_lector, id_espacio);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarLectorEspacioPorId (long id_lector) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqllectorespacio.eliminarLectorEspacio(pm, id_lector);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<LectorEspacio> darLectoresEspacio()
	{
		return sqllectorespacio.darLectorEspacios(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar las VISITAS
	 *****************************************************************/

	public Visita adicionarVisita( long id_visitante, long id_lector, Date FechaHoraEntrada, Date FechaHoraSalida) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();  
			long tuplasInsertadas = sqlvisita.adicionarVisita(pm, id_visitante, id_lector, FechaHoraEntrada, FechaHoraSalida);
			tx.commit();

			log.trace ("Inserción de la visita: " + id_visitante + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Visita(id_visitante, id_lector, FechaHoraEntrada, FechaHoraSalida);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}


	public List<Visita> darVisitas()
	{
		return sqlvisita.darVisitas(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			Métodos para manejar los VISITANTES
	 *****************************************************************/

	public Visitante adicionarVisitante( String nombre, String tipo, int numTelefono, String correo, String nomContacto, int numContacto, String estado, double temperatura) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long id = nextval ();
			long tuplasInsertadas = sqlvisitante.adicionarVisitante(pm, id, nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura);
			tx.commit();

			log.trace ("Inserción del Horario: " + id + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Visitante(id, nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}



	public List<Visitante> darVisitantes()
	{
		return sqlvisitante.darVisitantes(pmf.getPersistenceManager());
	}

	public Visitante darVisitantesPorId(long id)
	{
		return sqlvisitante.darVisitantePorIdVisitantente(pmf.getPersistenceManager(), id);
	}





	public long [] limpiarAforo ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long [] resp = sqlUtil.limpiarAforo(pm);
			tx.commit ();
			log.info ("Borrada la base de datos");
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return new long[] {-1, -1, -1, -1, -1, -1, -1};
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}
	
	
	public long cambiarEstadoSalud (long id, String estado)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlvisitante.cambiarEstadoVisitante(pm, id, estado);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	



	/* ****************************************************************
	 * 			Métodos para manejar los ESTABLECIMIENTOS
	 *****************************************************************/

	public Establecimiento adicionarEstablecimiento( long iEspacio, long iHorario, String nom, String type, int aforo) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			long id = nextval ();
			long tuplasInsertadas = sqlestablecimiento.adicionarEstablecimiento(pm, id, iEspacio, iHorario, nom, type, aforo);
			tx.commit();

			log.trace ("Inserción del Espacio: " + nom + ": " + tuplasInsertadas + " tuplas insertadas");
			return new Establecimiento(id, iEspacio, iHorario, nom, type, aforo);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarEstablecimientoPorId (long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlestablecimiento.eliminarEstablecimiento(pm, id);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Establecimiento> darEstablecimientos()
	{
		return sqlestablecimiento.darEstablecimientos(pmf.getPersistenceManager());
	}
}


