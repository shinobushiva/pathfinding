package jp.ac.sojou.cis.izumi.maharano;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class HeaderRenderer implements TableCellRenderer {

	public class HeaderComponent {
		public JCheckBox checkBox = new JCheckBox();
		public JTextField textField = new JTextField();

		public JPanel component;

		public HeaderComponent() {
			component = new JPanel();
			component.setLayout(new GridLayout(1, 1));
			// component.add(textField);
			component.add(checkBox);
			component.setFocusable(true);
			component.setRequestFocusEnabled(true);
		}
	}

	/**
	 * 
	 */
	private final MaharanoTool maharanoTool;
	Map<Integer, HeaderComponent> compMap = new HashMap<>();
	Map<Integer, Boolean> map = new HashMap<>();

	public void renewMap(int[] csks) {
		getMap().clear();
		compMap.clear();

		for (int column = 0; column < colNum; column++) {
			if (Arrays.binarySearch(csks, column) >= 0) {
				getMap().put(column, true);
			} else {
				getMap().put(column, false);
			}

			HeaderComponent hc = new HeaderComponent();

			hc.checkBox.setSelected(getMap().get(column));
			compMap.put(column, hc);
			Font f = hc.checkBox.getFont();
			Font f2 = new Font(f.getName(), f.getStyle(),
					(int) (f.getSize() / 2));
			hc.checkBox.setFont(f2);

			MyTableModel model = (MyTableModel) table.getModel();
			double[] weights = model.getWeights();
			if (weights.length > column) {
				hc.textField.setText("" + weights[column]);
			}
		}
		maharanoTool.repaint();
	}

	private int colNum;
	private JTable table;

	public HeaderRenderer(MaharanoTool maharanoTool, JTableHeader header,
			int colNum) {
		this.maharanoTool = maharanoTool;
		this.colNum = colNum;

		for (int column = 0; column < colNum; column++) {
			if (!getMap().containsKey(column)) {
				getMap().put(column, true);

				HeaderComponent hc = new HeaderComponent();
				hc.checkBox.setSelected(true);
				compMap.put(column, hc);
			}
		}

		table = header.getTable();

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader header = (JTableHeader) e.getComponent();
				JTable table = header.getTable();
				TableColumnModel columnModel = table.getColumnModel();
				int column = columnModel.getColumnIndexAtX(e.getX());

				HeaderComponent hc = compMap.get(column);

				System.out.println(e.getPoint());
				int min = hc.textField.getBounds().y;
				int max = hc.textField.getBounds().y
						+ hc.textField.getBounds().height;
				if (min <= e.getPoint().y && e.getPoint().y <= max) {
					System.out.println(true);
					hc.textField.requestFocus();
					System.out.println(hc.textField.isVisible());
					String txt = hc.textField.getText();
					String in = JOptionPane.showInputDialog("Put Weight", txt);
					try {
						double num = Double.parseDouble(in);
						hc.textField.setText("" + num);
					} catch (Exception ex) {
					}

					header.repaint();
				} else {

					getMap().put(column, !getMap().get(column));
					hc.checkBox.setSelected(getMap().get(column));
					HeaderRenderer.this.maharanoTool.saveProperties();

					header.repaint();
				}

			}
		});
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		HeaderComponent hc = compMap.get(column);
		hc.checkBox.setText("" + value);
		hc.checkBox.setToolTipText(value + " [Th="
				+ this.maharanoTool.ignoreThs[column] + "]");

		return hc.component;
	}

	public Map<Integer, Boolean> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Boolean> map) {
		this.map = map;
	}

}