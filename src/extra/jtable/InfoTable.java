package extra.jtable;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String _title;
	TableModel _tableModel;
	
	
	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
}
	
	
	private void initGUI() {
		// TODO change the layout of the panel to BorderLayout()
		setLayout(new BorderLayout());
		
		// TODO add a titled border to the JPanel, with the text _title
		TitledBorder titleBorder = BorderFactory.createTitledBorder(_title);
        setBorder(titleBorder);
        
		// TODO add a JTable (with vertical scroll bar) that uses _tableModel
        JTable table = new JTable(_tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
	}
}