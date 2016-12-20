package jp.ac.sojou.cis.izumi.maharano;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class MaharanoTool extends JFrame {
	private static final int START_ROW = 1;
	private final int COLNUM = 52;

	private static final String SETTINGS_PROPERTIES = "settings.properties";

	int[] cols;
	int[] ignoreThs = new int[] { 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1,
			1, -1, -1, 1 };

	private JTable table;
	private MyTableModel model;

	private Maharano maharano;

	private AlternativePathCalculator alternativePathCalculator;

	public MaharanoTool() {
		nodeData = loadNodeData(5, 1);
		linkData = loadLinkData();

		setModel(new MyTableModel());
		maharano = new Maharano();
		alternativePathCalculator = new AlternativePathCalculator(this);

		mapPanel = new MapPanel();
		JDialog mapDialog = new JDialog();
		mapDialog.setSize(640, 480);
		mapDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mapDialog.setVisible(true);
		mapDialog.add(mapPanel);
		mapPanel.setData(nodeData, linkData);

		getModel().setData(linkData);
		getModel().setColCount(getModel().getData()[0].length);

		double[] weights = new double[getModel().getData()[0].length];
		Arrays.fill(weights, 1);

		table = new JTable();
		table.setModel(getModel());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.revalidate();
		headerRenderer = new HeaderRenderer(this, table.getTableHeader(),
				COLNUM);
		table.getTableHeader().setDefaultRenderer(headerRenderer);
		table.setColumnSelectionAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);

		setLayout(new BorderLayout());
		// add(new JLabel("Test"), BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);

		JButton button = new JButton("Update Maharano Distance");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Map<Integer, Boolean> map = headerRenderer.getMap();
				List<Integer> colList = new ArrayList<>();

				map.keySet().stream().forEachOrdered((i) -> {
					if (map.get(i))
						colList.add(i);
				});
				System.out.println(colList);
				Integer[] array = colList.toArray(new Integer[0]);
				int[] cols = new int[array.length];
				int[] igth = new int[array.length];
				for (int i = 0; i < cols.length; i++) {
					cols[i] = array[i];
					igth[i] = ignoreThs[array[i]];
				}

				maharano.process(linkData, 1, COLNUM, cols, igth);
				double[] result = maharano.getMaharano();
				getModel().setMaharano(result);

				setPivotColors();
				mapPanel.setMaharano(result);

				alternativePathCalculator.setMaharano(result);

				table.revalidate();
				table.repaint();

			}
		});

		JButton exportButton = new JButton("Export CSV");
		exportButton.addActionListener(new ActionListener() {
			private String q(String s) {
				return "\"" + s + "\"";
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				double[] mh = getModel().getMaharano();
				String[][] data = getModel().getData();

				StringBuilder sb = new StringBuilder();

				sb.append(q("マハラノビス距離")).append(",");
				for (int j = 0; j < data[0].length; j++) {
					if (headerRenderer.map.get(j)) {
						sb.append("o:");
					} else {
						sb.append("x:");
					}
					sb.append(q(data[0][j])).append(",");
				}
				sb.append("\n");

				for (int i = 1; i < mh.length; i++) {
					sb.append(q("" + mh[i])).append(",");
					for (int j = 0; j < data[i].length; j++) {
						sb.append(q(data[i][j])).append(",");
					}
					sb.append("\n");
				}

				JFileChooser jfc = new JFileChooser();
				int st = jfc.showSaveDialog(MaharanoTool.this);
				if (st == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					try (BufferedWriter w = new BufferedWriter(new FileWriter(
							file))) {
						w.write(sb.toString());
						w.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(MaharanoTool.this,
							"CSV Exported at " + file.getAbsolutePath());
				}
			}
		});

		JButton calcButton = new JButton("Alternative Calculator");
		calcButton.addActionListener(new ActionListener() {

			JDialog d;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (d == null) {
					d = new JDialog();
					d.add(alternativePathCalculator);
					d.setLocationRelativeTo(MaharanoTool.this);
					d.setVisible(true);
					d.setSize(MaharanoTool.this.getSize());
					d.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
			};
		});

		jtfPivots = new JTextField();
		jtfPivots.setText("25, 50, 75, 100");
		jtfColors = new JTextField();
		jtfColors.setText("0000ff, 00ffff, ffff00, ff0000");

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(1, 2));
		fields.add(jtfPivots);
		fields.add(jtfColors);
		add(fields, BorderLayout.NORTH);

		JPanel buttons = new JPanel();
		buttons.add(button);
		buttons.add(exportButton);
		buttons.add(calcButton);
		add(buttons, BorderLayout.SOUTH);

		loadProperties();
	}

	private void setPivotColors() {

		mapPanel.setPivots(new double[] { 0.25, 0.5, 0.75, 1 });
		mapPanel.setColors(new Color[] { Color.BLUE, Color.CYAN, Color.PINK,
				Color.RED });

		String[] sps = jtfPivots.getText().split(",");
		String[] scs = jtfColors.getText().split(",");

		try {
			double[] pivots = new double[sps.length];
			for (int i = 0; i < sps.length; i++) {
				pivots[i] = Double.parseDouble(sps[i].trim());
			}

			Color[] colors = new Color[scs.length];
			for (int i = 0; i < sps.length; i++) {
				colors[i] = new Color(p(scs[i].trim().substring(0, 2)),
						p(scs[i].trim().substring(2, 4)), p(scs[i].trim()
								.substring(4, 6)));
				System.out.println(colors[i]);
			}
			mapPanel.setPivots(pivots);
			mapPanel.setColors(colors);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int p(String str) {
		try {
			return Integer.parseInt(str.toUpperCase().trim(), 16);

		} catch (Exception e) {
			System.err.println(str);
			e.printStackTrace();
			return 0;
		}
	}

	void saveProperties() {
		Properties prop = new Properties();

		StringBuilder buf = new StringBuilder();
		for (int i : headerRenderer.map.keySet()) {
			if (headerRenderer.map.get(i))
				buf.append("" + i + ",");
		}
		String vals = buf.substring(0, buf.length() - 1);
		prop.put("checks", vals);
		prop.put("pivots", jtfPivots.getText());
		prop.put("colors", jtfColors.getText());
		try (FileOutputStream fos = new FileOutputStream(new File(
				SETTINGS_PROPERTIES))) {
			prop.store(fos, "Saved @ " + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadProperties() {

		File file = new File(SETTINGS_PROPERTIES);
		if (!file.exists())
			return;
		try (FileInputStream fis = new FileInputStream(file)) {

			Properties props = new Properties();
			props.load(fis); // 読み込み
			fis.close();
			String checks = (String) props.getProperty("checks");
			String[] css = checks.split(",");
			int[] cis = new int[css.length];
			for (int i = 0; i < css.length; i++) {
				cis[i] = Integer.parseInt(css[i]);
			}

			jtfPivots.setText((String) props.getProperty("pivots",
					"25, 50, 75, 100"));
			jtfColors.setText((String) props.getProperty("colors",
					"0000ff, 00ffff, ffff00, ff0000"));

			headerRenderer.renewMap(cis);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[][] loadNodeData(int column, int startRow) {

		String file = "resources/nodes.csv";
		ArrayList<String[]> valList = new ArrayList<>();

		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(file))))) {
			String line = null;

			String[] lastOnes = null;
			int lineCount = 0;
			while ((line = r.readLine()) != null) {

				String[] vals = line.split(",");

				if (vals.length < column) {
					lineCount++;
					continue;
				}

				if (lineCount < startRow) {
					lineCount++;
					continue;
				}

				if (lastOnes != null) {
					for (int j = 0; j < vals.length; j++) {
						if (vals[j].isEmpty())
							vals[j] = lastOnes[j];
					}
				}
				lastOnes = vals;
				valList.add(vals);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		String[][] valStrs = valList.toArray(new String[0][0]);
		return valStrs;
	}

	public String[][] loadLinkData() {

		String file = "resources/links.csv";
		ArrayList<String[]> valList = new ArrayList<>();

		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(file))))) {
			String line = null;

			String[] lastOnes = null;
			int lineCount = 0;
			while ((line = r.readLine()) != null) {

				String[] vals = line.split(",");

				if (vals.length < COLNUM) {
					lineCount++;
					continue;
				}

				if (lineCount < START_ROW) {
					lineCount++;
					continue;
				}

				if (lastOnes != null) {
					for (int j = 0; j < vals.length; j++) {
						if (vals[j].isEmpty())
							vals[j] = lastOnes[j];
					}
				}
				lastOnes = vals;

				valList.add(vals);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		String[][] valStrs = valList.toArray(new String[0][0]);
		return valStrs;
	}

	public static void main(String[] args) {
		MaharanoTool mt = new MaharanoTool();
		mt.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mt.setBounds(100, 100, 640, 480);
		mt.setVisible(true);

	}

	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);

		table.revalidate();
		table.repaint();
		table.getTableHeader().repaint();
	}

	public MyTableModel getModel() {
		return model;
	}

	public void setModel(MyTableModel model) {
		this.model = model;
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4874278079358376708L;
	private String[][] linkData;
	private HeaderRenderer headerRenderer;
	private String[][] nodeData;
	private MapPanel mapPanel;
	private JTextField jtfPivots;
	private JTextField jtfColors;

}
