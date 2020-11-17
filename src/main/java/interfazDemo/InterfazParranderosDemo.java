package interfazDemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import negocio.AforoCC;
import negocio.VOCentroComercial;
import negocio.VOEspacio;
import negocio.VOHorario;
import negocio.VOLector;
import negocio.VOLectorCC;
import negocio.VOLectorEspacio;
import negocio.VOVisita;
import negocio.VOVisitante;
import negocio.VOcarnet;

@SuppressWarnings("serial")
public class InterfazParranderosDemo extends JFrame implements ActionListener{

	//CONSTANTES
	
	private static Logger log = Logger.getLogger(InterfazParranderosDemo.class.getName());

	private final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigDemo.json"; 

	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 

	//ATRIBUTOS
	
    private JsonObject tableConfig;

    private AforoCC parranderos;

	//ATRIBUTOS DE INTERFAZ
    
    private JsonObject guiConfig;

    private PanelDatos panelDatos;

    private JMenuBar menuBar;

    //METODOS
	
    public InterfazParranderosDemo( )
    {
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        parranderos = new AforoCC (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
	
	//METODOS PARA LA CONFIG DE LA INTERFAZ
    
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
	
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }
    
    private void crearMenu(  JsonArray jsonMenu )
    {    	
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }

    //DEMOS DE CENTRO COMERCIAL

    public void demoCentroComercial ()
    {
    	try
    	{
    		Date d = null;
    		Date da = null;
    		VOHorario horario = parranderos.adicionarHorario(d, da);
    		VOCentroComercial cc1 = parranderos.adicionarCC("Baltino", 50, horario.getId(), 25, "disponible");

    		List <VOHorario> listaHorarios = parranderos.darVOHorarios();
    		List <VOCentroComercial> listaCc = parranderos.darVOCCs();

    		long ccEliminados = parranderos.eliminarTipoCCPorId(cc1.getId());
    		long horariosEliminados = parranderos.eliminarHorarioPorId(horario.getId());

    		String resultado = "Demo de creación y listado de CCs\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "Adicionado el CC: " + cc1 + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarHorario (listaHorarios);
    		resultado += "\n" + listarCc (listaCc);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += ccEliminados + " CCs eliminados\n";
    		resultado += horariosEliminados + " Horarios eliminados\n";
    		resultado += "\n Demo terminada";

    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}

    }

    //DEMOS DE ESPACIO
    
    public void demoEspacio ()
    {
    	try
    	{
    		Date d = null;
    		Date da = null;
    		VOHorario horario = parranderos.adicionarHorario(d, da);
    		VOCentroComercial cc1 = parranderos.adicionarCC("Baltino", 50, horario.getId(), 25, "disponible");
    		String nombre = "Fedexpres";
    		double area = 20.5;
    		String tipo = "";
    		String estado = "";
    		VOEspacio espacio = parranderos.adicionarEspacio(cc1.getId(),  nombre, area,  tipo, estado);
    		
    		List <VOHorario> listaHorarios = parranderos.darVOHorarios();
    		List <VOCentroComercial> listaCc = parranderos.darVOCCs();
    		List <VOEspacio> listaEspacios = parranderos.darVOEspacios();
    		
    		long espaciosEliminados = parranderos.eliminarTipoEspacioPorId(espacio.getId());
    		long horariosEliminados = parranderos.eliminarHorarioPorId(horario.getId());
    		long ccEliminados = parranderos.eliminarTipoCCPorId(cc1.getId());

    		String resultado = "Demo de creación y listado de Espacios\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "Adicionado el CC: " + cc1 + "\n";
    		resultado += "Adicionado el Espacio: " + espacio + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarHorario (listaHorarios);
    		resultado += "\n" + listarCc (listaCc);
    		resultado += "\n" + listarEspacio (listaEspacios);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += ccEliminados + " CCs eliminados\n";
    		resultado += horariosEliminados + " Horarios eliminados\n";
    		resultado += espaciosEliminados + " Espacios eliminados\n";
    		resultado += "\n Demo terminada";
    		
    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}

    }
    
    //DEMOS DE HORARIO
    
    public void demoHorario ()
    {
    	try
    	{
    		Date d = null;
    		Date da = null;
    		VOHorario horario = parranderos.adicionarHorario(d, da);

    		List <VOHorario> listaHorarios = parranderos.darVOHorarios();

    		long horariosEliminados = parranderos.eliminarHorarioPorId(horario.getId());

    		String resultado = "Demo de creación y listado de Horario\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarHorario (listaHorarios);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += horariosEliminados + " Horarios eliminados\n";
    		resultado += "\n Demo terminada";

    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}

    }

    //DEMOS DE LECTOR
    
    public void demoLector ()
    {
    	try
    	{
    		String espacio = "";
    		VOLector lector = parranderos.adicionarLector(espacio);
    		
    		List <VOLector> listaLectores = parranderos.darVOLectores();
    		
    		long lectoresEliminados = parranderos.eliminarLectorPorId(lector.getId());
    			
    		String resultado = "Demo de creación y listado de Lector\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Lector: " + lector + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarLector (listaLectores);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += lectoresEliminados + " Lectores eliminados\n";
    		resultado += "\n Demo terminada";

    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    //DEMOS DE LECTOR ESPACIO

    public void demoLectorEspacio () {
    	try
    	{
    		Date d = null;
    		Date da = null;
    		VOHorario horario = parranderos.adicionarHorario(d, da);
    		VOCentroComercial cc1 = parranderos.adicionarCC("Baltino", 50, horario.getId(), 25, "disponible");
    		String nombre = "Fedexpres";
    		double area = 20.5;
    		String tipo = "";
    		String estado = "";
    		VOEspacio espacio = parranderos.adicionarEspacio(cc1.getId(),  nombre, area,  tipo, estado);
    		VOLector lector = parranderos.adicionarLector(espacio.getNombre());
    		VOLectorEspacio lectorEspacio = parranderos.adicionarLectorEspacio(lector.getId(), espacio.getId());

    		List <VOHorario> listaHorarios = parranderos.darVOHorarios();
    		List <VOCentroComercial> listaCc = parranderos.darVOCCs();
    		List <VOEspacio> listaEspacios = parranderos.darVOEspacios();
    		List <VOLector> listaLectores = parranderos.darVOLectores();
    		List <VOLectorEspacio> listaLectoresEspacios = parranderos.darVOLectoresEspacio();
    		
    		long espaciosEliminados = parranderos.eliminarTipoEspacioPorId(espacio.getId());
    		long horariosEliminados = parranderos.eliminarHorarioPorId(horario.getId());
    		long ccEliminados = parranderos.eliminarTipoCCPorId(cc1.getId());
    		long lectoresEliminados = parranderos.eliminarLectorPorId(lector.getId());
    		long lectoresEspaciosEliminados = parranderos.eliminarLectorEspacioPorId(lectorEspacio.getIdLector());
    		
    		String resultado = "Demo de creación y listado de Lector Espacio\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "Adicionado el CC: " + cc1 + "\n";
    		resultado += "Adicionado el Espacio: " + espacio + "\n";
    		resultado += "Adicionado el Lector: " + lector + "\n";
    		resultado += "Adicionando el Lector Espacio" + lectorEspacio + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarHorario (listaHorarios);
    		resultado += "\n" + listarCc (listaCc);
    		resultado += "\n" + listarEspacio (listaEspacios);
    		resultado += "\n" + listarLector (listaLectores);
    		resultado += "\n" + listarLectorEspacio (listaLectoresEspacios);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += ccEliminados + " CCs eliminados\n";
    		resultado += horariosEliminados + " Horarios eliminados\n";
    		resultado += espaciosEliminados + " Espacios eliminados\n";
    		resultado += lectoresEliminados + " Lectores eliminados\n";
    		resultado += lectoresEspaciosEliminados + " Lectores espacios eliminados\n";
    		resultado += "\n Demo terminada";

    		panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }

    //DEMOS DE LECTOR CC
    
    public void demoLectorCC ()
    {
    	try
    	{
    		Date d = null;
    		Date da = null;
    		VOHorario horario = parranderos.adicionarHorario(d, da);
    		VOCentroComercial cc1 = parranderos.adicionarCC("Baltino", 50, horario.getId(), 25, "disponible");
    		String espacio = "";
    		VOLector lector = parranderos.adicionarLector(espacio);
    		VOLectorCC lectorCc = parranderos.adicionarLectorCC(lector.getId(), cc1.getId());
    		
    		List <VOHorario> listaHorarios = parranderos.darVOHorarios();
    		List <VOCentroComercial> listaCc = parranderos.darVOCCs();
    		List <VOLector> listaLectores = parranderos.darVOLectores();
    		List <VOLectorCC> listaLectoresCc = parranderos.darVOLectoresCC();
    		
    		long lectoresEliminados = parranderos.eliminarLectorPorId(lector.getId());
    		long ccEliminados = parranderos.eliminarTipoCCPorId(cc1.getId());
    		long horariosEliminados = parranderos.eliminarHorarioPorId(horario.getId());
    		long lectoresCcEliminados = parranderos.eliminarLectorCCPorId(lectorCc.getIdLector());
    		
    		String resultado = "Demo de creación y listado de Lector CC\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "Adicionado el CC: " + cc1 + "\n";
    		resultado += "Adicionado el Lector: " + lector + "\n";
    		resultado += "Adicionando el Lector CC" + lectorCc + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarHorario (listaHorarios);
    		resultado += "\n" + listarCc (listaCc);
    		resultado += "\n" + listarLector (listaLectores);
    		resultado += "\n" + listarLectorCc (listaLectoresCc);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += ccEliminados + " CCs eliminados\n";
    		resultado += horariosEliminados + " Horarios eliminados\n";
    		resultado += lectoresEliminados + " Lectores eliminados\n";
    		resultado += lectoresCcEliminados + " Lectores CC eliminados\n";
    		resultado += "\n Demo terminada";

    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    //DEMO VISITANTE

    public void demoVisitante ()
    {
    	try {

    		String nombre = "Fernando";
    		String tipo = "Cliente";
    		int numTelefono = 310310310;
    		String correo = "Fernando@.com";
    		String nomContacto = "Federico";
    		int numContacto = 315315351;
    		String estado = "";
    		double temperatura = 36.5;
    		VOVisitante visitante = parranderos.adicionarVisitantes(nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura);

    		List <VOVisitante> listaVisitantes = parranderos.darVOVisitante();

    		//long visitantesEliminados = parranderos.eliminarVisitantePorId(visitante.getId());

    		String resultado = "Demo de creación y listado de Visitante\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Visitante: " + visitante + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarVisitante (listaVisitantes);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		//resultado += visitantesEliminados + " Visitantes eliminados\n";
    		resultado += "\n Demo terminada";

    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    //DEMO VISITA
    
    public void demoVisita ()
    {
    	try 
    	{
    		Date d = null;
    		Date da = null;
    		String nombre = "Fernando";
    		String tipo = "Cliente";
    		int numTelefono = 310310310;
    		String correo = "Fernando@.com";
    		String nomContacto = "Federico";
    		int numContacto = 315315351;
    		String estado = "";
    		double temperatura = 36.5;
    		String espacio = "";
    		VOLector lector = parranderos.adicionarLector(espacio);
    		VOVisitante visitante = parranderos.adicionarVisitantes(nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura);
    		VOVisita visita = parranderos.adicionarVisita(visitante.getId(), lector.getId(), d, da);

    		List <VOVisitante> listaVisitantes = parranderos.darVOVisitante();
    		List <VOLector> listaLectores = parranderos.darVOLectores();
    		List <VOVisita> listaVisitas = parranderos.darVOVisitas();

    		//long visitasEliminadas = parranderos.eliminarVisitaPorId(visita.getId());
    		//long visitantesEliminados = parranderos.eliminarVisitantePorId(visitante.getId());
    		long lectoresEliminados = parranderos.eliminarLectorPorId(lector.getId());
    		
    		String resultado = "Demo de creación y listado de Visita\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Lector: " + lector + "\n";
    		resultado += "Adicionado el Visitante: " + visitante + "\n";
    		resultado += "Adicionado la Visita: " + visita + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarVisitante (listaVisitantes);
    		resultado += "\n" + listarVisita (listaVisitas);
    		resultado += "\n" + listarLector (listaLectores);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		//resultado += visitasEliminadas + " Visitas eliminadas\n";
    		//resultado += visitantesEliminados + " Visitantes eliminados\n";
    		resultado += lectoresEliminados + " Lectores eliminados\n";
    		resultado += "\n Demo terminada";
    		
    		panelDatos.actualizarInterfaz(resultado);	
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    //DEMO CARNET
    
    public void demoCarnet ()
    {
    	try 
    	{
    		
    		String nombre = "Fernando";
    		String tipo = "Cliente";
    		int numTelefono = 310310310;
    		String correo = "Fernando@.com";
    		String nomContacto = "Federico";
    		int numContacto = 315315351;
    		String estado = "";
    		double temperatura = 36.5;
    		VOVisitante visitante = parranderos.adicionarVisitantes(nombre, tipo, numTelefono, correo, nomContacto, numContacto, estado, temperatura);
    		VOcarnet carnet = parranderos.adicionarCarnet(visitante.getId());

    		List <VOVisitante> listaVisitantes = parranderos.darVOVisitante();
    		List <VOcarnet> listaCarnets = parranderos.darVOCarnets();

    		//long visitantesEliminados = parranderos.eliminarVisitantePorId(visitante.getId());
    		long carnetsEliminados = parranderos.eliminarTipoCarnetPorId(carnet.getId(), visitante.getId());
    		
    		String resultado = "Demo de creación y listado de Carnet\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Carnet: " + carnet + "\n";
    		resultado += "Adicionado el Visitante: " + visitante + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		resultado += "\n" + listarVisitante (listaVisitantes);
    		resultado += "\n" + listarCarnet (listaCarnets);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		//resultado += visitantesEliminados + " Visitantes eliminados\n";
    		resultado += carnetsEliminados + " Carnets eliminados\n";
    		resultado += "\n Demo terminada";
    		
    		panelDatos.actualizarInterfaz(resultado);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }

    //METODOS ADMINISTRATIVOS

    public void mostrarLogParranderos ()
    {
    	mostrarArchivo ("parranderos.log");
    }

    public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
    
    public void limpiarLogParranderos ()
	{
		boolean resp = limpiarArchivo ("parranderos.log");

		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
    
    public void limpiarLogDatanucleus ()
	{
		boolean resp = limpiarArchivo ("datanucleus.log");

		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
    
    public void limpiarBD ()
	{
		try 
		{
			long eliminados [] = parranderos.limpiarParranderos();
			
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Bebidas eliminadas\n";
			resultado += eliminados [4] + " Tipos de bebida eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    
    public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);
    }
	
	//METODOS PRIVADOS PARA LA PRESENTACION DE RESULTADOS Y OTRAS OPERACIONES
	
	private String listarCarnet(List<VOcarnet> lista) 
    {
    	String resp = "Los carnets existentes son:\n";
    	int i = 1;
        for (VOcarnet tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarCc(List<VOCentroComercial> lista) 
    {
    	String resp = "Los CC existentes son:\n";
    	int i = 1;
        for (VOCentroComercial tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarEspacio(List<VOEspacio> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOEspacio tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarHorario(List<VOHorario> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOHorario tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarLector(List<VOLector> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOLector tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarLectorEspacio(List<VOLectorEspacio> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOLectorEspacio tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarLectorCc(List<VOLectorCC> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOLectorCC tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarVisita(List<VOVisita> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOVisita tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
	
	private String listarVisitante(List<VOVisitante> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOVisitante tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
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
	
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}
	
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//METODOS DE LA INTERACCION
	
	@Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazParranderosDemo.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
	
	//PROGRAMA PRINCIPAL
	
	public static void main( String[] args )
    {
        try
        {
        	
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazParranderosDemo interfaz = new InterfazParranderosDemo( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
	
}
