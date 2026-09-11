package extra.jtable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.view.Utils;
//import simulator.view.SimulatorViewer;

class ControlPanel extends JPanel implements SimulatorObserver {
	private Controller _ctrl;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _fileChooser;
	private JButton _playButton;
	private JButton _stopButton;
	private JSpinner _steps;
	private JTextField _deltaTime;
	private JButton _simulatorButton;
	private JButton _viewerButton;
	private JLabel _textillo;
	private JLabel _textillo1;
	private int _pasitos = 5000;
	double tiempoDeltoide = 1000;
	private JFrame _pater;
	private ForceLawsDialog _ForceLawsDialog;
	
	// TODO añade más atributos aquí …
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		// TODO registrar this como observador
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);
		// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten
		// TODO crear el selector de ficheros
		_fc = new JFileChooser();
		
		_fileChooser = new JButton();
		_fileChooser.setIcon(new ImageIcon("resources/icons/open.png"));
		_fc.setCurrentDirectory(new File("resources/examples/input"));
		
		//ACTIONLISTERNERN FILECHOOSER
		_fileChooser.addActionListener((e) -> 
		{
			_fc.showOpenDialog(Utils.getWindow(this));
			
			if(_fc.getSelectedFile() != null) {
				_ctrl.reset();
				try {
					_ctrl.loadData(new FileInputStream(_fc.getSelectedFile().getPath()));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		_toolaBar.add(_fileChooser);
		_toolaBar.addSeparator();
		_simulatorButton = new JButton();
		_toolaBar.add(_simulatorButton);
		_simulatorButton.setIcon(new ImageIcon("resources/icons/physics.png"));

		//NUEVO crea una instancia del forcelawsdialog y la muestra en el medio
		_simulatorButton.addActionListener((e) ->
		{
			if (_ForceLawsDialog == null) {
				_ForceLawsDialog = new ForceLawsDialog((Frame) SwingUtilities.getWindowAncestor(this), _ctrl);
				_ForceLawsDialog.open();
			}

			else
				_ForceLawsDialog.open();


		});

		
		
		_viewerButton = new JButton();
		_toolaBar.add(_viewerButton);
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_toolaBar.addSeparator();
		
		_playButton = new JButton();
		_toolaBar.add(_playButton);
		_playButton.setIcon(new ImageIcon("resources/icons/run.png"));
		
		
		_stopButton = new JButton();
		_toolaBar.add(_stopButton);
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_toolaBar.addSeparator();
		
		
		_textillo = new JLabel("Steps: ");
		_toolaBar.add(_textillo);
		Dimension preferredSize1 = new Dimension(100, 20);
		//maniobras para el JSpinner de l
		
		//valor x defecto, valor minimo, valor maximo, de cuanto en cuanto incrementa
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(_pasitos, 0, 10000, 250);
		
		_steps = new JSpinner(spinnerModel);
		
		_steps.addChangeListener(new ChangeListener()
		{
			@Override
	        public void stateChanged(ChangeEvent e) 
			{
	            _pasitos = (int) _steps.getValue();
			}
		});
		_steps.setPreferredSize(preferredSize1);
		
		
		_toolaBar.add(_steps);
		//fiN maniobras jSPinner
		
		
		_textillo1 = new JLabel("Delta-Time: ");
		_toolaBar.add(_textillo1);
		_deltaTime = new JTextField(Double.toString(tiempoDeltoide));
		Dimension preferredSize = new Dimension(100, 20);
		_deltaTime.setPreferredSize(preferredSize);
		_toolaBar.add(_deltaTime);
		
		//================================
		_toolaBar.addSeparator();
		_toolaBar.addSeparator();
		_toolaBar.addSeparator();
		_toolaBar.addSeparator();
		_toolaBar.addSeparator();
		_toolaBar.addSeparator();
		_toolaBar.addSeparator();
		//================================
		
		
		//actionListener playButton
		_playButton.addActionListener((e) ->{
			
			_fileChooser.setEnabled(false);
			_steps.setEnabled(false);
			_deltaTime.setEnabled(false);
			_simulatorButton.setEnabled(false);
			_viewerButton.setEnabled(false);
			_stopped = false;
		
			String tiempoDelta = _deltaTime.getText();
			if (!tiempoDelta.isEmpty()) {
				tiempoDeltoide = Double.parseDouble(tiempoDelta);
				_ctrl.setDeltaTime(tiempoDeltoide);
			} else {
			    // lanzaria una excepcion supongo
			}
			run_sim(_pasitos);
		});
		
		
		//actionListener stopButton
		_stopButton.setToolTipText("Stop the simulator");
		_stopButton.addActionListener((e)->{
			_stopped = true;
		});
		
		
		//
		_viewerButton.setToolTipText("Stop the simulator");
		//Viewer ActionListener
		_pater = new JFrame();
		_viewerButton.addActionListener((e) ->{
			ViewerWindow viewer = new ViewerWindow(_pater, _ctrl);
			viewer.setVisible(true);
			viewer.setLocationRelativeTo(null);
		});
		
		
		
		// Quit Button
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> Utils.quit(this));
		_toolaBar.add(_quitButton);
	
	}
	
	// TODO el resto de métodos van aquí…
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		
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
	
	//codigo copiado de la plantilla
	private void run_sim(int n) {
				
		if (n > 0 && !_stopped) { 
				try {
					_ctrl.run(1); 
			} catch (Exception e) { 
					// TODO llamar a Utils.showErrorMsg con el mensaje de error que //      corresponda // TODO activar todos los botones
					_stopped = true;
					//de aqui
					Utils.showErrorMsg("ERROR");
					_fileChooser.setEnabled(true);
					_steps.setEnabled(true);
					_deltaTime.setEnabled(true);
					_simulatorButton.setEnabled(true);
					_viewerButton.setEnabled(true);
					// a aqui hecho por nosotros
					return; 
				} 
				SwingUtilities.invokeLater(() -> run_sim(n- 1));
			}else { 
				// TODO hecho por nosotros tambien
				_fileChooser.setEnabled(true);
				_steps.setEnabled(true);
				_deltaTime.setEnabled(true);
				_simulatorButton.setEnabled(true);
				_viewerButton.setEnabled(true);
			}
					
	}
}