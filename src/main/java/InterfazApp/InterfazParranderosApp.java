package InterfazApp;

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
import java.util.Date;

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
import negocio.VOEspacio;
import negocio.VOEstablecimiento;
import negocio.VOLector;
import negocio.VOVisita;
import negocio.VOVisitante;

@SuppressWarnings("serial")
public class InterfazParranderosApp extends JFrame implements ActionListener{

	//CONSTANTES
	
	private static Logger log = Logger.getLogger(InterfazParranderosApp.class.getName());
	
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 

	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 

	//ATRIBUTOS
	
    private JsonObject tableConfig;

    private AforoCC parranderos;
    
    //ATRIBUTOS DE LA INTERFAZ
    
    private JsonObject guiConfig;

    private PanelDatos panelDatos;

    private JMenuBar menuBar;

    //METODOS
    
    public InterfazParranderosApp( )
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
    
    //METODOS DE LA INTERFAZ
    
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontrï¿½ un archivo de configuraciï¿½n vï¿½lido: " + tipo);
		} 
		catch (Exception e)
		{
			e.printStackTrace ();
			log.info ("NO se encontrï¿½ un archivo de configuraciï¿½n vï¿½lido");			
			JOptionPane.showMessageDialog(null, "No se encontrï¿½ un archivo de configuraciï¿½n de interfaz vï¿½lido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
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
    		log.info ( "Se aplica configuraciï¿½n por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuraciï¿½n indicada en el archivo de configuraciï¿½n" );
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

    //CRUD DE ESPACIO

    public void adicionarEspacio ()
    {
    	try
    	{
    		String nombreEspacio = JOptionPane.showInputDialog (this, "Nombre del espacio?", "Adicionar espacio", JOptionPane.QUESTION_MESSAGE);
    		if (nombreEspacio != null)
    		{
    			String area = JOptionPane.showInputDialog (this, "Area?", "Adicionar area", JOptionPane.QUESTION_MESSAGE);
    			if (area != null)
    			{
    				String tipo = JOptionPane.showInputDialog (this, "Tipo?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    				if (tipo != null)
    				{
    					String estado = JOptionPane.showInputDialog (this, "Estado?", "Adicionar estado", JOptionPane.QUESTION_MESSAGE);
    					if (estado != null)
    					{
    						VOEspacio espacio = parranderos.adicionarEspacio(1, nombreEspacio, Double.parseDouble(area), tipo, estado);
    						if (espacio == null)
    						{
    							throw new Exception ("No se pudo crear un espacio con nombre: " + nombreEspacio);
    						}
    						String resultado = "En adicionarEspacio\n\n";
    						resultado += "Espacio adicionado exitosamente: " + espacio;
    						resultado += "\n Operación terminada";
    						panelDatos.actualizarInterfaz(resultado);
    					}
    				}
    			}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    //CRUD DE ESTABLECIMIENTO
    
    public void adicionarEstablecimiento ()
    {
    	try
    	{
    		String idEspacio = JOptionPane.showInputDialog (this, "Id del espacio?", "Adicionar espacio", JOptionPane.QUESTION_MESSAGE);
    		if (idEspacio != null)
    		{
    			String idHorario = JOptionPane.showInputDialog (this, "Id del horario?", "Adicionar horario", JOptionPane.QUESTION_MESSAGE);
    			if (idHorario != null)
    			{
    				String nombre = JOptionPane.showInputDialog (this, "Nombre del establecimiento?", "Adicionar nombre", JOptionPane.QUESTION_MESSAGE);
    				if (nombre != null)
    				{
    					String tipo = JOptionPane.showInputDialog (this, "Tipo del establecimiento?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    					if (tipo != null)
    					{
    						String aforoMax = JOptionPane.showInputDialog (this, "Aforo del establecimiento?", "Adicionar aforo", JOptionPane.QUESTION_MESSAGE);
    						if (aforoMax != null)
    						{
    							VOEstablecimiento establecimiento = parranderos.adicionarEstablecimiento(Long.parseLong(idEspacio), Long.parseLong(idHorario), nombre, tipo, Integer.parseInt(aforoMax));
        						if (establecimiento == null)
        						{
        							throw new Exception ("No se pudo crear un establecimiento con nombre: " + nombre);
        						}
        						String resultado = "En adicionarEstablecimiento\n\n";
        						resultado += "Establecimiento adicionado exitosamente: " + establecimiento;
        						resultado += "\n Operación terminada";
        						panelDatos.actualizarInterfaz(resultado);
    						}
    						
    					}
    				}
    			}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    public void eliminarEstablecimiento( )
    {
    	try 
    	{
    		String establecimientoStr = JOptionPane.showInputDialog (this, "Establecimiento?", "Borrar establecimiento", JOptionPane.QUESTION_MESSAGE);
    		if (establecimientoStr != null)
    		{
    			long establecimiento = Long.valueOf (establecimientoStr);
    			long establecimientosEliminados = parranderos.eliminarEstablecimientoPorId (establecimiento);

    			String resultado = "En eliminar Establecimiento\n\n";
    			resultado += establecimientosEliminados + " Establecimientos eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    //CRUD DE VISITANTE
    
    public void adicionarVisitante ()
    {
    	try
    	{
    		String nombre = JOptionPane.showInputDialog (this, "Nombre del visitante?", "Adicionar nombre", JOptionPane.QUESTION_MESSAGE);
			if (nombre != null)
			{
				String tipo = JOptionPane.showInputDialog (this, "Tipo del visitante?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
				if (tipo != null)
				{
					String numTelefono = JOptionPane.showInputDialog (this, "Numero de telefono del visitante?", "Adicionar numero de telefono", JOptionPane.QUESTION_MESSAGE);
					if (numTelefono != null)
					{
						String correo = JOptionPane.showInputDialog (this, "Correo del visitante?", "Adicionar correo", JOptionPane.QUESTION_MESSAGE);
						if (correo != null)
						{
							String nomContacto = JOptionPane.showInputDialog (this, "Nombre del contacto del visitante?", "Adicionar nombre de contacto", JOptionPane.QUESTION_MESSAGE);
							if (nomContacto != null)
							{
								String numContacto = JOptionPane.showInputDialog (this, "Numero del contacto del visitante?", "Adicionar numero de contacto", JOptionPane.QUESTION_MESSAGE);
								if (numContacto != null)
								{
									String estado = JOptionPane.showInputDialog (this, "Estado del visitante?", "Adicionar estado", JOptionPane.QUESTION_MESSAGE);
			    					if (estado != null)
			    					{
			    						String temperatura = JOptionPane.showInputDialog (this, "Temperatura del visitante?", "Adicionar temperatura", JOptionPane.QUESTION_MESSAGE);
			        					if (temperatura != null) 
			        					{
			        						VOVisitante visitante = parranderos.adicionarVisitantes(nombre, tipo, Integer.parseInt(numTelefono), correo, nomContacto, Integer.parseInt(numContacto), estado, Double.parseDouble(temperatura));
			        						if (visitante == null)
			        						{
			        							throw new Exception ("No se pudo crear un visitante con nombre: " + nombre);
			        						}
			        						String resultado = "En adicionarVisitante\n\n";
			        						resultado += "Visitante adicionado exitosamente: " + visitante;
			        						resultado += "\n Operación terminada";
			        						panelDatos.actualizarInterfaz(resultado);
			        					}
			    					}
								}
							}
						}
					}
				}
			}
			else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    //CRUD DE LECTOR

    public void adicionarLector ()
    {
    	try
    	{
    		String espacio = JOptionPane.showInputDialog (this, "Id del espacio?", "Adicionar espacio", JOptionPane.QUESTION_MESSAGE);
    		if (espacio != null)
    		{
    			VOLector lector = parranderos.adicionarLector(espacio);
    			if (lector == null)
    			{
					throw new Exception ("No se pudo crear un lector para el espacio: " + espacio);
    			}
    			String resultado = "En adicionarLector\n\n";
				resultado += "Lector adicionado exitosamente: " + lector;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    	}
    	catch (Exception e) 
    	{
    		e.printStackTrace();
    		String resultado = generarMensajeError(e);
    		panelDatos.actualizarInterfaz(resultado);
    	}
    }

    //CRUD DE VISITA

    public void adicionarVisita ()
    {
    	try
    	{
    		String id_visitante = JOptionPane.showInputDialog (this, "Id del visitante?", "Adicionar visitante", JOptionPane.QUESTION_MESSAGE);
    		if (id_visitante != null)
    		{
    			String id_lector = JOptionPane.showInputDialog (this, "Id del lector?", "Adicionar lector", JOptionPane.QUESTION_MESSAGE);
        		if (id_lector != null) 
        		{
        			String FechaHoraEntrada = JOptionPane.showInputDialog (this, "Hora entrada?", "Adicionar hora entrada", JOptionPane.QUESTION_MESSAGE);
            		if (FechaHoraEntrada != null) 
            		{
            			String FechaHoraSalida = JOptionPane.showInputDialog (this, "Hora salida?", "Adicionar hora salida", JOptionPane.QUESTION_MESSAGE);
                		if (FechaHoraSalida != null) 
                		{
                			VOVisita visita = parranderos.adicionarVisita(Long.parseLong(id_visitante), Long.parseLong(id_lector), Date.parse(FechaHoraEntrada), Date.parse(FechaHoraSalida));
                			if (visita == null)
                			{
            					throw new Exception ("No se pudo crear una visita: " + visita);
                			}
                			String resultado = "En adicionarVisita\n\n";
            				resultado += "Visita adicionada exitosamente: " + visita;
            				resultado += "\n Operación terminada";
            				panelDatos.actualizarInterfaz(resultado);
                		}
            		}
        		}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
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
		resultado += " * Universidad	de	los	Andes	(Bogotï¿½	- Colombia)\n";
		resultado += " * Departamento	de	Ingenierï¿½a	de	Sistemas	y	Computaciï¿½n\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versiï¿½n 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germï¿½n Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jimï¿½nez, Christian Ariza\n";
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
		String resultado = "************ Error en la ejecuciï¿½n\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para mï¿½s detalles";
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
	
	//METODOS DE INTERACCION
	
	@Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazParranderosApp.class.getMethod ( evento );			
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
            InterfazParranderosApp interfaz = new InterfazParranderosApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
	
}
