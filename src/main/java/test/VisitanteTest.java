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
import negocio.VOVisitante;

public class VisitanteTest {
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(VisitanteTest.class.getName());
	
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
	 * 			Métodos de prueba para la tabla Visitante - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla Visitante
	 * 1. Adicionar unVisitante
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar unVisitante por su identificador
	 * 4. Borrar unVisitante por su nombre
	 */
    @Test
	public void CRDVisitanteTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre Visitante");
			aforocc = new AforoCC (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de Visitante incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de Visitante incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de aforocc y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los Visitantees con la tabla vacía
			List <VOVisitante> lista = aforocc.darVOVisitante();
			assertEquals ("No debe haber Visitantees!!", 0, lista.size ());

			// Lectura de los Visitantees con unVisitante adicionado
			String nombreVisitante1 = "Vino tinto";
			int num = 12;
			String estado = "Verde";
			String tipo = "Cliente";
			VOVisitante Visitante1 = aforocc.adicionarVisitantes(nombreVisitante1, tipo, num, nombreVisitante1, nombreVisitante1, num, estado, num);
			lista = aforocc.darVOVisitante();
			assertEquals ("Debe haber un Visitante!!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", Visitante1, lista.get (0));

			// Lectura de los Visitantees con dos Visitantees adicionados
			String nombreVisitante2 = "Cerveza";
			int numb  = 13;
			VOVisitante Visitante2 = aforocc.adicionarVisitantes(nombreVisitante2, tipo, numb, nombreVisitante2, nombreVisitante2, numb, estado, numb);
			lista = aforocc.darVOVisitante();
			assertEquals ("Debe haber dos Visitantees creados !!", 2, lista.size ());
			assertTrue ("El primerVisitante adicionado debe estar en la tabla", Visitante1.equals (lista.get (0)) || Visitante1.equals (lista.get (1)));
			assertTrue ("El segundoVisitante adicionado debe estar en la tabla", Visitante2.equals (lista.get (0)) || Visitante2.equals (lista.get (1)));

			// Prueba de eliminación de unVisitante, dado su identificador
			long tbEliminados = aforocc.eliminarVisitantePorId(Visitante1.getId ());
			assertEquals ("Debe haberse eliminado unVisitante !!", 1, tbEliminados);
			lista = aforocc.darVOVisitante();
			assertEquals ("Debe haber un soloVisitante !!", 1, lista.size ());
			assertFalse ("El primerVisitante adicionado NO debe estar en la tabla", Visitante1.equals (lista.get (0)));
			assertTrue ("El segundoVisitante adicionado debe estar en la tabla", Visitante2.equals (lista.get (0)));

		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla Visitante.\n";
			msg += "Revise el log de aforocc y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla Visitante");
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
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "VisitanteTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
}
