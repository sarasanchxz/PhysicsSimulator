package extra.jtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class GroupsTableModel extends AbstractTableModel implements SimulatorObserver{

	/**
	 * 
	 */
	Controller _ctrl;
	String[] _header = { "Id", "Force Laws", "Bodies" };
	List<BodiesGroup> _groups;
	
	
	GroupsTableModel(Controller ctrl) {
		_groups = new ArrayList<>();
		_ctrl = ctrl;
		ctrl.addObserver(this);
	// TODO registrar this como observador;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int getRowCount() {
		// epico
		if(_groups == null) {
			return 0;
		}
		else {
			return _groups.size();
		}
	}

	@Override
	public int getColumnCount() {
		return _header.length;
		//return 0;
	}
	
	//este lo creamos nosotros pa q aparzca el nombre en cada columna
	public String getColumnName(int index) {
		return _header[index];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// coges el bodiesgroup de la lista en la posicion q te pidan
		BodiesGroup list = _groups.get(rowIndex);
	
		//y esto pa coger las cositas
		switch (columnIndex) {
			case 0:
				return list.getId();
			case 1:
				return list.getForceLawsInfo();
			case 2:
				StringBuilder stringB = new StringBuilder();
				for(Body b: list.getBodies()) {
					stringB.append(b.getId()).append(", ");
				}
				return stringB.substring(0, stringB.length() - 2);
			default: 
				return null;
		}
		   
		
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		//fireTableStructureChanged();
		
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_groups.clear();
		fireTableStructureChanged();
		//_groups.clear();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		fireTableStructureChanged();
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		_groups.add(g);
		fireTableStructureChanged();
		
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		
		fireTableStructureChanged();
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		for(BodiesGroup i : _groups) {
			if(i.getId() == g.getId()) {
				i = g;
			}
		}
		fireTableStructureChanged();
	}

}
