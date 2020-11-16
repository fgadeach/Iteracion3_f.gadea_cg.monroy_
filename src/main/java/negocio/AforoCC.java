package negocio;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import persistencia.PersistenciaAforoCC;

public class AforoCC {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(AforoCC.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaAforoCC pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public AforoCC ()
	{
		pp = PersistenciaAforoCC.getInstance();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public AforoCC (JsonObject tableConfig)
	{
		pp = PersistenciaAforoCC.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los CARNETs
	 *****************************************************************/
	
	public Carnet adicionarCarnet (long visitante)
	{
        log.info ("Adicionando Carnet al visitante: " + visitante);
        Carnet parq = pp.adicionarCarnet(visitante);		
        log.info ("Adicionando Carnet: " + parq);
        return parq;
	}
	

	public long eliminarTipoCarnetPorId (long idCarnet, long idVisitante)
	{
		log.info ("Eliminando Carnet por id: " + idCarnet);
        long resp = pp.eliminarCarnetPorId (idCarnet,idVisitante);		
        log.info ("Eliminando Carnet por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<Carnet> darCarnetS ()
	{
		log.info ("Consultando Carnets");
        List<Carnet> Carnets = pp.darCarnets ();	
        log.info ("Consultando Carnets: " + Carnets.size() + " existentes");
        return Carnets;
	}

	
	public List<VOcarnet> darVOCarnets ()
	{
		log.info ("Generando los VO de los Carnets");        
        List<VOcarnet> voCarnets = new LinkedList<VOcarnet> ();
        for (Carnet tb : pp.darCarnets ())
        {
        	voCarnets.add (tb);
        }
        log.info ("Generando los VO de Carnets: " + voCarnets.size() + " existentes");
        return voCarnets;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los Centro Comerciales
	 *****************************************************************/
	
	public CentroComercial adicionarCC (String nombre, int aforo, long horario, int aforoAct, String estado)
	{
        log.info ("Adicionando un Centro Comercial: " + nombre);
        CentroComercial var = pp.adicionarCC(nombre, aforo, horario, aforoAct, estado);		
        log.info ("Adicionando Carnet: " + var);
        return var;
	}
	

	public long eliminarTipoCCPorId (long idCC)
	{
		log.info ("Eliminando CComercial por id: " + idCC);
        long resp = pp.eliminarCCPorId(idCC);		
        log.info ("Eliminando CComercial por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<CentroComercial> darCCS ()
	{
		log.info ("Consultando Carnets");
        List<CentroComercial> var = pp.darCCs();	
        log.info ("Consultando CentroComerciales: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOCentroComercial> darVOCCs()
	{
		log.info ("Generando los VO de los CComerciales");        
        List<VOCentroComercial> voCC= new LinkedList<VOCentroComercial> ();
        for (CentroComercial tb : pp.darCCs())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de CCs: " + voCC.size() + " existentes");
        return voCC;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los Espacio
	 *****************************************************************/
	
	public Espacio adicionarEspacio(long icc, String nom, int area, String t, String estado)
	{
        log.info ("Adicionando un Espacio: " + nom);
        Espacio var = pp.adicionarEspacio(icc, nom, area, t, estado);		
        log.info ("Adicionando Carnet: " + var);
        return var;
	}
	

	public long eliminarTipoEspacioPorId (long id)
	{
		log.info ("Eliminando Espacio por id: " + id);
        long resp = pp.eliminarEspacioPorId(id);		
        log.info ("Eliminando Espacio por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<Espacio> darEspacios ()
	{
		log.info ("Consultando Espacios");
        List<Espacio> var = pp.darEspacios();	
        log.info ("Consultando Espacios: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOEspacio> darVOEspacios()
	{
		log.info ("Generando los VO de los Espacios");        
        List<VOEspacio> voCC= new LinkedList<VOEspacio> ();
        for (Espacio tb : pp.darEspacios())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de Espacios: " + voCC.size() + " existentes");
        return voCC;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los Hotatios
	 *****************************************************************/
	
	public Horario adicionarHorario(Date d, Date da)
	{
        log.info ("Adicionando un Horario: " + d + "/"+ da);
        Horario var = pp.adicionarHorario(d, da);		
        log.info ("Adicionando Horario: " + var);
        return var;
	}
	

	public long eliminarHorarioPorId (long id)
	{
		log.info ("Eliminando Horario por id: " + id);
        long resp = pp.eliminarHorairoPorId(id);		
        log.info ("Eliminando Horario por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<Horario> darHorarios()
	{
		log.info ("Consultando Horarios");
        List<Horario> var = pp.darHorarios();	
        log.info ("Consultando Horarios: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOHorario> darVOHorarios()
	{
		log.info ("Generando los VO de los Horarios");        
        List<VOHorario> voCC= new LinkedList<VOHorario> ();
        for (Horario tb : pp.darHorarios())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de Horarios: " + voCC.size() + " existentes");
        return voCC;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los Lector
	 *****************************************************************/
	
	public Lector adicionarLector(String espacio)
	{
        log.info ("Adicionando un Lector: " + espacio);
        Lector var = pp.adicionarLector(espacio);		
        log.info ("Adicionando Lector: " + var);
        return var;
	}
	

	public long eliminarLectorPorId (long id)
	{
		log.info ("Eliminando Lector por id: " + id);
        long resp = pp.eliminarLectorPorId(id);		
        log.info ("Eliminando Lector por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<Lector> darLectores()
	{
		log.info ("Consultando Lectroes");
        List<Lector> var = pp.darLectores();	
        log.info ("Consultando Lectores: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOLector> darVOLectores()
	{
		log.info ("Generando los VO de los Lectores");        
        List<VOLector> voCC= new LinkedList<VOLector> ();
        for (Lector tb : pp.darLectores())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de Lectores: " + voCC.size() + " existentes");
        return voCC;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los LectorCC
	 *****************************************************************/
	
	public LectorCC adicionarLectorCC(long id_lector, long id_cc)
	{
        log.info ("Adicionando un LectorCC: " + id_lector);
        LectorCC var = pp.adicionarLectorCC(id_lector, id_cc);		
        log.info ("Adicionando LectorCC: " + var);
        return var;
	}
	

	public long eliminarLectorCCPorId (long id)
	{
		log.info ("Eliminando LectorCC por id: " + id);
        long resp = pp.eliminarLectorCCPorId(id);		
        log.info ("Eliminando LectorCC por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<LectorCC> darLectoresCC()
	{
		log.info ("Consultando Lectroes");
        List<LectorCC> var = pp.darLectoresCC();	
        log.info ("Consultando LectoresCC: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOLectorCC> darVOLectoresCC()
	{
		log.info ("Generando los VO de los LectoresCC");        
        List<VOLectorCC> voCC= new LinkedList<VOLectorCC> ();
        for (LectorCC tb : pp.darLectoresCC())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de LectoresCC: " + voCC.size() + " existentes");
        return voCC;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los LectorEspacio
	 *****************************************************************/
	
	public LectorEspacio adicionarLectorEspacio(long id_lector, long id_espacio)
	{
        log.info ("Adicionando un LectorEspacio: " + id_lector);
        LectorEspacio var = pp.adicionarLectorEspacio(id_lector, id_espacio);		
        log.info ("Adicionando LectorEspacio: " + var);
        return var;
	}
	

	public long eliminarLectorEspacioPorId (long id)
	{
		log.info ("Eliminando LectorEspacio por id: " + id);
        long resp = pp.eliminarLectorEspacioPorId(id);		
        log.info ("Eliminando LectorEspacio por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<LectorEspacio> darLectoresEspacio()
	{
		log.info ("Consultando Lectres");
        List<LectorEspacio> var = pp.darLectoresEspacio();	
        log.info ("Consultando LectoresEspacio: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOLectorEspacio> darVOLectoresEspacio()
	{
		log.info ("Generando los VO de los LectoresEspacio");        
        List<VOLectorEspacio> voCC= new LinkedList<VOLectorEspacio> ();
        for (LectorEspacio tb : pp.darLectoresEspacio())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de LectoresEspacio: " + voCC.size() + " existentes");
        return voCC;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las Visitas
	 *****************************************************************/
	
	public Visita adicionarVisita(long id_visitante, long id_lector, Date FechaHoraEntrada, Date FechaHoraSalida)
	{
        log.info ("Adicionando una visita: " + id_visitante);
        Visita var = pp.adicionarVisita(id_visitante, id_lector, FechaHoraEntrada, FechaHoraSalida);		
        log.info ("Adicionando Visita: " + var);
        return var;
	}
	
	public List<Visita> darVisitas()
	{
		log.info ("Consultando Visitas");
        List<Visita> var = pp.darVisitas();	
        log.info ("Consultando Visitas: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOVisita> darVOVisitas()
	{
		log.info ("Generando los VO de las Visitas");        
        List<VOVisita> voCC= new LinkedList<VOVisita> ();
        for (Visita tb : pp.darVisitas())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de las Visitas: " + voCC.size() + " existentes");
        return voCC;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las Visitantes
	 *****************************************************************/
	
	public Visitante adicionarVisitantes(String nombre, String tipo, int numTelefono, String correo, String nomContacto, int numContacto, String estado, double temperatura)
	{
        log.info ("Adicionando un visitante: " + nombre);
        Visitante var = pp.adicionarVisitante(nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura);		
        log.info ("Adicionando visitante: " + var);
        return var;
	}
	
	public List<Visitante> darVisitantes()
	{
		log.info ("Consultando visitantes");
        List<Visitante> var = pp.darVisitantes();	
        log.info ("Consultando Visitantes: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOVisitante> darVOVisitante()
	{
		log.info ("Generando los VO de los visitantes");        
        List<VOVisitante> voCC= new LinkedList<VOVisitante> ();
        for (Visitante tb : pp.darVisitantes())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de los Visitantes: " + voCC.size() + " existentes");
        return voCC;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los Establecimientos
	 *****************************************************************/
	
	public Establecimiento adicionarEstablecimiento(long iEspacio, long iHorario, String nom, String type, int aforo)
	{
        log.info ("Adicionando un Establecimiento: " + nom);
        Establecimiento var = pp.adicionarEstablecimiento(iEspacio, iHorario, nom, type, aforo);		
        log.info ("Adicionando Establecimiento: " + var);
        return var;
	}
	

	public long eliminarEstablecimientoPorId (long id)
	{
		log.info ("Eliminando Establecimiento por id: " + id);
        long resp = pp.eliminarEstablecimientoPorId(id);		
        log.info ("Eliminando Establecimiento por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<Establecimiento> darEstablecimientos()
	{
		log.info ("Consultando Establecimientos");
        List<Establecimiento> var = pp.darEstablecimientos();	
        log.info ("Consultando Establecimientos: " + var.size() + " existentes");
        return var;
	}

	
	public List<VOEstablecimiento> darVOEstablecimientos()
	{
		log.info ("Generando los VO de los Establecimientos");        
        List<VOEstablecimiento> voCC= new LinkedList<VOEstablecimiento> ();
        for (Establecimiento tb : pp.darEstablecimientos())
        {
        	voCC.add (tb);
        }
        log.info ("Generando los VO de Establecimientos: " + voCC.size() + " existentes");
        return voCC;
	}
	

	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Aforo
	 */
	public long [] limpiarParranderos ()
	{
        log.info ("Limpiando la BD de Aforo");
        long [] borrrados = pp.limpiarAforo();	
        log.info ("Limpiando la BD de Aforo: Listo!");
        return borrrados;
	}
}
