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
import negocio.VOVisitante;
import negocio.VOcarnet;

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
			log.info ("Se encontr� un archivo de configuraci�n v�lido: " + tipo);
		} 
		catch (Exception e)
		{
			e.printStackTrace ();
			log.info ("NO se encontr� un archivo de configuraci�n v�lido");			
			JOptionPane.showMessageDialog(null, "No se encontr� un archivo de configuraci�n de interfaz v�lido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
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
    		log.info ( "Se aplica configuraci�n por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuraci�n indicada en el archivo de configuraci�n" );
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

    		String resultado = "Demo de creaci�n y listado de CCs\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "Adicionado el CC: " + cc1 + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		//resultado += "\n" + listarHorarios (listaHorarios);
    		//resultado += "\n" + listarCC (listaCc);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += ccEliminados + " Bares eliminados\n";
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
    		int aforoMax = 10;
    		int aforoAct = 5;
    		String tipo = "";
    		String estado = "";
    		VOEspacio espacio = parranderos.adicionarEspacio(cc1.getId(), horario.getId(), nombre, aforoMax, aforoAct, tipo, estado);
    		
    		List <VOHorario> listaHorarios = parranderos.darVOHorarios();
    		List <VOCentroComercial> listaCc = parranderos.darVOCCs();
    		List <VOEspacio> listaEspacios = parranderos.darVOEspacios();
    		
    		long espaciosEliminados = parranderos.eliminarTipoEspacioPorId(espacio.getId());
    		long horariosEliminados = parranderos.eliminarHorarioPorId(horario.getId());
    		long ccEliminados = parranderos.eliminarTipoCCPorId(cc1.getId());

    		String resultado = "Demo de creaci�n y listado de Espacios\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "Adicionado el CC: " + cc1 + "\n";
    		resultado += "Adicionado el Espacio: " + espacio + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		//resultado += "\n" + listarHorarios (listaHorarios);
    		//resultado += "\n" + listarCC (listaCc);
    		//resultado += "\n" + listarEspacio (listaEspacios);
    		resultado += "\n\n************ Limpiando la base de datos ************ \n";
    		resultado += ccEliminados + " Bares eliminados\n";
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

    		String resultado = "Demo de creaci�n y listado de CCs\n\n";
    		resultado += "\n\n************ Generando datos de prueba ************ \n";
    		resultado += "Adicionado el Horario: " + horario + "\n";
    		resultado += "\n\n************ Ejecutando la demo ************ \n";
    		//resultado += "\n" + listarHorarios (listaHorarios);
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

    {

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
		resultado += " * Universidad	de	los	Andes	(Bogot�	- Colombia)\n";
		resultado += " * Departamento	de	Ingenier�a	de	Sistemas	y	Computaci�n\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versi�n 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germ�n Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jim�nez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);
    }
	
	//METODOS PRIVADOS PARA LA PRESENTACION DE RESULTADOS Y OTRAS OPERACIONES
	
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
		String resultado = "************ Error en la ejecuci�n\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para m�s detalles";
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
