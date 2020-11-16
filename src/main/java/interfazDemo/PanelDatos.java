package interfazDemo;

import java.awt.BorderLayout;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class PanelDatos extends JPanel{

	//ATRIBUTOS DE LA INTERFAZ
	
	private JTextArea textArea;

	//CONSTRUCTORES
	
	public PanelDatos ()
    {
        setBorder (new TitledBorder ("Traza de las demostraciones"));
        setLayout( new BorderLayout( ) );
        
        textArea = new JTextArea("Aqu� sale el resultado de la ejecuci�n de las demos");
        textArea.setEditable(false);
        add (new JScrollPane(textArea), BorderLayout.CENTER);
    }
	
	//METODOS
	
	public void actualizarInterfaz (String texto)
    {
    	textArea.setText(texto);
    }

}
