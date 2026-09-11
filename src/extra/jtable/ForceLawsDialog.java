package extra.jtable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.SimulatorObserver;

public class ForceLawsDialog extends JDialog implements SimulatorObserver {

	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private int _status; // NUEVO
	private JButton _OK, _cancel;
	private int _selectedLawsIndex;

	ForceLawsDialog(Frame parent, Controller ctrl) {

		super(parent, true);
		_ctrl = ctrl;
		initGUI();

		_ctrl.addObserver(this);
	}

	private void initGUI() {

		setTitle("Force Laws Selection");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

// Parte de arriba del mainPanel

		JLabel help = new JLabel(
				"<html><p>Select a force law and provide values for the parametes in the <b>Value column</b> "
						+ "(default values are used for parametes with no value).</p></html>");

		help.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(help);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

// _forceLawsInfo se usar para establecer la informacn en la tabla

		_forceLawsInfo = _ctrl.getForceLawsInfo();

// CREACION JTABLE

		_dataTableModel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
// TODO hacer editable solo la columna 1
			}

		};

		JTable table = new JTable(_dataTableModel); // IMPORTANTE

		table.setPreferredSize(new Dimension(500, 250));
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		_dataTableModel.setColumnIdentifiers(_headers);
		_dataTableModel.addRow(_headers);
		
//layout
		
		mainPanel.add(table);
		mainPanel.add(scrollPane);
		

// CREACION DE LOS COMBOBOX Y PANEL DE ABAJO

		JPanel JPanelAbajo = new JPanel();
		JPanelAbajo.setAlignmentX(CENTER_ALIGNMENT);

		_lawsModel = new DefaultComboBoxModel<>();

// TODO a�adir la descripcion de todas las leyes de fuerza a _lawsModel

		for (JSONObject forcelaw : _ctrl.getForceLawsInfo()) {
			_lawsModel.addElement(forcelaw.getString("desc"));
		}

// TODO crear un combobox que use _lawsModel y aniadirlo al panel

		JLabel _lawsLabel = new JLabel("Force laws: ");
		JComboBox<String> _comboLaws = new JComboBox<>(_lawsModel); // herramienta cochina
		JPanelAbajo.add(_lawsLabel);
		JPanelAbajo.add(_comboLaws);

		_comboLaws.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { // PARA QUE APAREZCAN COSAS EN LA TABLA

				limpiarTabla(); // antes de hacer nada limpio la tabla

				JSONObject data = new JSONObject();
				String info = (String) _comboLaws.getSelectedItem(); // selecciono un elemento del combobox ej(moving
																		// towards fixed point)
				_selectedLawsIndex = _comboLaws.getSelectedIndex(); // se usa luego

				for (JSONObject law : _forceLawsInfo) { // recorro la lista de leyes (mirar de donde sale
														// _forceLawsInfo)

					if (law.get("desc").equals(info)) { // si el contenido de la clave desc es igual a la del
														// seleccionado (porque lo que aparece en el combobox son las
														// desc)
						data = law.getJSONObject("data"); // lo guardo en data

					}
				}

				// NO SE SI ESTE FOR VA DENTRO O FUERA DEL PRIMERO
				
				_dataTableModel.addRow(_headers);
			
				
				for (String keydata : data.keySet()) { // itero sobre las claves de data y sus valores

					String descripcion = data.getString(keydata); // valor de la clave
					String[] rowData = { keydata, " ", descripcion }; // creo la fila
					_dataTableModel.addRow(rowData); // aniado fila			    
				    
				}
				
				TableColumn terceraColumna = table.getColumnModel().getColumn(2); //para que la tercera columna sea más ancha
				terceraColumna.setPreferredWidth(200);
				
			}
		});

// TODO crear un combobox que use _groupsModel y aniadirlo al panel

		JLabel _groupsLabel = new JLabel("Groups: ");
		_groupsModel = new DefaultComboBoxModel<>();
		JComboBox<String> _comboGroups = new JComboBox<>(_groupsModel);
		JPanelAbajo.add(_groupsLabel);
		JPanelAbajo.add(_comboGroups);



// BOTONES OK Y CANCEL + OTRO PANEL

		JPanel JPanelAbajo2 = new JPanel();
		JPanelAbajo.setAlignmentX(CENTER_ALIGNMENT);

// OK BOTON

		_OK = new JButton("OK");
		_OK.setSize(10, 20);
		_OK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JSONObject ja = new JSONObject();
				JSONObject ja2 = new JSONObject();
              
				for (int row = 1; row < table.getRowCount(); row++) {
					
			       String clave = table.getValueAt(row, 0).toString(); // obtener la clave de la primera columna
			       String valor = (String)table.getValueAt(row, 1).toString();// obtener el valor de la segunda columna
                    
			       
			       if (clave == "g"|| clave == "G") {
			    	   
			    	   try {
			    	   	
			    	    double num = Double.parseDouble(valor); //lo convierto a double
						ja.put(clave, num);                     //lo añado al Json
						System.out.println(ja.toString());
						
						}
			    	   catch (NumberFormatException e1 ){
			    		       		   
			    		   System.err.println("El valor '" + valor + "' no es un número válido.");
			               e1.printStackTrace();
			    	   }
			    	   
			    	   
			       }
			    
			       else {  //SOLO FUNCIONA PARA ENTEROS abajo de la clase otras cosas comentadas pero no se si son validas 
			    	   
			    	   try {
			    	      String[] numeros = valor.replace("[", "").replace("]", "").split(",");
				    	  int[] arrayc = new int [numeros.length];
				    	  for(int i=0; i<numeros.length; i++){
				    	      arrayc[i] = Integer.parseInt(numeros[i].trim());
				    	      
				    	  }
				    	      
				    	   JSONArray jsonArrayC = new JSONArray(arrayc);
				    	   ja.put(clave, jsonArrayC);
				    	   System.out.println(ja.toString());
				    	   
			    	   }
			    	   
			    	   catch(NumberFormatException e1 ){
			    		   
			    		   System.err.println("El valor '" + valor + "' no es un número válido.");
			               e1.printStackTrace();
			    		   
			    	   }		    	   		 		       
								       
				}
							
				ja2.put("data", ja);
		        ja2.put("type", _forceLawsInfo.get(_selectedLawsIndex).getString("type"));
		        
		       _ctrl.setForcesLaws(_groupsModel.getSelectedItem().toString(), ja2);
		        
		        System.out.println(ja2.toString());															
            

				_status = 1;
				setVisible(false);			

				}
				
			}
		});
		JPanelAbajo2.add(_OK);

// CANCEL BOTON

		_cancel = new JButton("Cancel");
		_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
// TODO Auto-generated method stub
				_status = 0;
				setVisible(false);
			}
		});
		JPanelAbajo2.add(_cancel);

		mainPanel.add(JPanelAbajo);
		mainPanel.add(JPanelAbajo2);
		
		setPreferredSize(new Dimension(700, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open() {
		if (_groupsModel.getSize() == 0)
//return _status;

		setLocationRelativeTo(null);
		
		 
		pack();
		setVisible(true);
//return _status;
	}
// TODO el resto de metodos van aqui

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
// TODO Auto-generated method stub

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {

		for (BodiesGroup group : groups.values()) {

			_groupsModel.removeElement(group); // borrro todos los elemntos???

		}

		time = 0.0; // reseteo valores??
		dt = 0.0;

	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
// TODO Auto-generated method stub
		for(BodiesGroup group : groups.values()) {
			_groupsModel.addElement(group.getId()); 
			}

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {

		//for(BodiesGroup group : groups.values()) {
		_groupsModel.addElement(g.getId()); 
		//}
		
		//creo que mal
	}
	
	

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
// TODO Auto-generated method stub

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
// TODO Auto-generated method stub

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
// TODO Auto-generated method stub

	}

//COSAS PARA LIMPIAR LA TABLA

	private void limpiarTabla() {

		_dataTableModel.setRowCount(0);
	}
	
	
	//OTRAS OPCIONES PARA EL OK
	
	//1. La que esta puesta solo para enteros
	
	//2. Solo para doubles 
	
	
	/* String[] valores = valor.split(",");
			    	      double valor1 = Double.parseDouble(valores[0].replace("[", ""));
			    	      double valor2 = Double.parseDouble(valores[1].replace("]", ""));
			    	      
			    	      double[] arrayC = new double [valores.length];		    	      
				    	      arrayc[0] = valor1;
				    	      arrayc[1] = valor2;
				    	      JSONArray jsonArrayC2 = new JSONArray(arrayC); 
			    	          ja.put(clave,jsonArrayC2); 
	 */
	
	
	//3. juntando ambas???
	
	/*   String[] valores = valor.replace("[", "").replace("]", "").split(",");
			    	   Object[] array = new Object[valores.length];
			    	   
			    	   for (int i = 0; i < valores.length; i++) {
			    	       String strValue = valores[i].trim();
			    	       
			    	       if (strValue.contains(".")) {
			    	           array[i] = Double.parseDouble(strValue);
			    	       } else {
			    	           array[i] = Integer.parseInt(strValue);
			    	       }
			    	   }
			    	   
			    	   JSONArray jsonArray = new JSONArray();
			    	   for (Object value : array) {
			    	       if (value instanceof Integer) {
			    	    	   
			    	    	
			    	           
			    	       } else {
			    	         
			    	       }
			    	   }
			    	   
			    	   *

			    	   */
	
	
	
}
