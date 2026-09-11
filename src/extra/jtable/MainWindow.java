package extra.jtable;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//Vamos a sacar los datos de un ArrayList en un JTable
//Para esto necesitamos un modelo de tabla.
//Pues no siempre los datos van a venir en un array bidimensional
//
// In this example we will show the information stored in an List using
// a JTable
public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 1);
	private Border _blackBorder = BorderFactory.createLineBorder(Color.black, 2);
 	private Controller _ctrl;
	private EventsTableModel _model;
	private JTable _eventsTable;
	private JButton addButton;

	// this is what we show in the table
	// esto es lo que mostramos en la table
	private List<EventEx> _events;
	private JSpinner _time;
	private JComboBox<Integer> _priotiry;

	public MainWindow() {

		super("JTable Example");
		initGUI();
	}

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	public void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		JPanel buttonsPanel = new JPanel();
		mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);

		// spinner for selecting time
		_time = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		_time.setToolTipText("Simulation tick to run: 1-10000");
		_time.setMaximumSize(new Dimension(80, 40));
		_time.setMinimumSize(new Dimension(80, 40));
		_time.setPreferredSize(new Dimension(80, 40));

		// combo-box for selecting priority
		_priotiry = new JComboBox<Integer>();
		for (int i = 0; i < 10; i++) {
			_priotiry.addItem(i);
		}

		addButton = new JButton("Add Event");
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addEvent();
			}
		});

		buttonsPanel.add( new JLabel("Time: "));
		buttonsPanel.add(_time);
		buttonsPanel.add( new JLabel("Priority: "));
		buttonsPanel.add(_priotiry);
		buttonsPanel.add(addButton);

		//controlpaneladd
		ControlPanel controPanel = new ControlPanel(_ctrl);
		mainPanel.add(controPanel, BorderLayout.PAGE_START);
		

		
		JPanel tablePanel = new JPanel();
		//esto es para q se vea vertical
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		//creas la tabla de grupos
		InfoTable tablaGrupos = new InfoTable("Groups", new GroupsTableModel(_ctrl));
		//creas un borde + el titulo y to la wea
		tablaGrupos.setBorder(BorderFactory.createTitledBorder(_blackBorder, "Groups", TitledBorder.LEFT, TitledBorder.TOP));
		tablaGrupos.setPreferredSize(new Dimension(400, 300));
		//el scrollPane x lo q tengo entendido es para q la tabla sea deslizable, no fija
		JScrollPane scrollPane1 = new JScrollPane(tablaGrupos);
		tablePanel.add(tablaGrupos);




		InfoTable tablaCuerpos = new InfoTable("Bodies", new BodiesTableModel(_ctrl));
		tablaCuerpos.setBorder(BorderFactory.createTitledBorder(_blackBorder, "Bodies", TitledBorder.LEFT, TitledBorder.TOP));
		tablaCuerpos.setPreferredSize(new Dimension(400, 300));
		JScrollPane scrollPane2 = new JScrollPane(tablaCuerpos);
		tablePanel.add(tablaCuerpos);

		mainPanel.add(tablePanel);
		


		
		
		// table
		JPanel eventsPanel = new JPanel(new BorderLayout());
		mainPanel.add(eventsPanel,BorderLayout.CENTER);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
				
		// add border
		eventsPanel.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Events", TitledBorder.LEFT,
				TitledBorder.TOP));

		
		// the model
		_model = new EventsTableModel();
		_eventsTable = new JTable(_model);

		eventsPanel.add(new JScrollPane(_eventsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		// the actual events list
		_events = new ArrayList<EventEx>();
		_model.setEventsList(_events);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 300);
		pack();
		setVisible(true);
		
		StatusBar bar = new StatusBar(_ctrl);
		mainPanel.add(bar, BorderLayout.PAGE_END);
	}

	public void addEvent() {
		try {
			Integer time = (Integer) _time.getValue();
			Integer priority = (Integer) _priotiry.getSelectedItem();
			_events.add(new EventEx(time, priority));
			
			// avisamos al modelo de tabla para que se actualize. En la
			// práctica esto no hace falta porque se le avisa directamente 
			// el TrafficSimulator
			//
			// We notify the table model that the list has been changed. This is not
			// required in the assignment, it will be notified directly by the 
			// TrafficSimulator since it is an observer
			//
			_model.update();
		} catch (Exception e) {
			JOptionPane.showMessageDialog( //
					(Frame) SwingUtilities.getWindowAncestor(this), //
					"Something went wrong ...",
					"ERROR", //
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {

		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				new MainWindow();
			}
		});
	}

}
