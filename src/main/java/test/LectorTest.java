package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileReader;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import negocio.AforoCC;
import negocio.VOLector;

public class LectorTest {
	

	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(LectorTest.class.getName());
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
	 */
	private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
	/**
	 * La clase que se quiere probar
	 */
    private AforoCC aforocc;
	
    /* ****************************************************************
	 * 			Métodos de prueba para la tabla Lector - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla Lector
	 * 1. Adicionar unlector
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar unlector por su identificador
	 * 4. Borrar unlector por su nombre
	 */
    @Test
	public void CRDLectorTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre Lector");
			aforocc = new AforoCC (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de Lector incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de Lector incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de aforocc y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los lectores con la tabla vacía
			List <VOLector> lista = aforocc.darVOLectores();
			assertEquals ("No debe haber lectores!!", 0, lista.size ());

			// Lectura de los lectores con unlector adicionado
			String nombreLector1 = "Vino tinto";
			VOLector Lector1 = aforocc.adicionarLector (nombreLector1);
			lista = aforocc.darVOLectores();
			assertEquals ("Debe haber un Lector!!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", Lector1, lista.get (0));

			// Lectura de los lectores con dos lectores adicionados
			String nombreLector2 = "Cerveza";
			VOLector Lector2 = aforocc.adicionarLector (nombreLector2);
			lista = aforocc.darVOLectores();
			assertEquals ("Debe haber dos lectores creados !!", 2, lista.size ());
			assertTrue ("El primerlector adicionado debe estar en la tabla", Lector1.equals (lista.get (0)) || Lector1.equals (lista.get (1)));
			assertTrue ("El segundolector adicionado debe estar en la tabla", Lector2.equals (lista.get (0)) || Lector2.equals (lista.get (1)));

			// Prueba de eliminación de unlector, dado su identificador
			long tbEliminados = aforocc.eliminarLectorPorId (Lector1.getId ());
			assertEquals ("Debe haberse eliminado unlector !!", 1, tbEliminados);
			lista = aforocc.darVOLectores();
			assertEquals ("Debe haber un sololector !!", 1, lista.size ());
			assertFalse ("El primerlector adicionado NO debe estar en la tabla", Lector1.equals (lista.get (0)));
			assertTrue ("El segundolector adicionado debe estar en la tabla", Lector2.equals (lista.get (0)));

		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla Lector.\n";
			msg += "Revise el log de aforocc y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla Lector");
		}
		finally
		{
			aforocc.limpiarParranderos();
    		aforocc.cerrarUnidadPersistencia ();    		
		}
	}

    /**
     * Método de prueba de la restricción de unicidad sobre el nombre de Lector
     */
	@Test
	public void unicidadLectorTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del nombre dellector");
			aforocc = new AforoCC (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Lector incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de Lector incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de aforocc y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los lectores con la tabla vacía
			List <VOLector> lista = aforocc.darVOLectores();
			assertEquals ("No debe haber lectores creados!!", 0, lista.size ());

			// Lectura de los lectores con unlector adicionado
			String nombreLector1 = "Vino tinto";
			VOLector Lector1 = aforocc.adicionarLector (nombreLector1);
			lista = aforocc.darVOLectores();
			assertEquals ("Debe haber un lector creado !!", 1, lista.size ());

			VOLector Lector2 = aforocc.adicionarLector (nombreLector1);
			assertNull ("No puede adicionar dos lectores con el mismo nombre !!", Lector2);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla Lector.\n";
			msg += "Revise el log de aforocc y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla Lector");
		}    				
		finally
		{
			aforocc.limpiarParranderos();
    		aforocc.cerrarUnidadPersistencia ();    		
		}
	}

	/* ****************************************************************
	 * 			Métodos de configuración
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración de tablas válido");
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "LectorTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
}
