package jp.ac.sojou.cis.izumi.maharano;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AlternativePathCalculator2 extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 237557506650691361L;

	private JTextField jtfRoute1;
	private JTextField jtfRoute2;

	private JTextArea jtaAlternatives;

	private JLabel jlResult;

	private JButton jbCalc;

	private MaharanoTool tool;

	public void setMaharano(double[] maharano) {
		this.maharano = maharano;
	}

	private double[] maharano;

	public AlternativePathCalculator2(MaharanoTool tool) {
		this.tool = tool;

		// Sample
		// 48-29,29-26,26-22,22-9,9-5
		// 48-29,29-28,28-12,12-10,10-6,6-5
		//
		// 29-26,26-22/12-10,10-6,6-5
		// 22-9,9-5/29-28,28-12

		jtfRoute1 = new JTextField();
		jtfRoute1.setText("48-29,29-26,26-22,22-9,9-5");
		jtfRoute2 = new JTextField();
		jtfRoute2.setText("48-29,29-28,28-12,12-10,10-6,6-5");

		jtaAlternatives = new JTextArea();
		jtaAlternatives.setText("29-26,26-22/12-10,10-6,6-5\n"
				+ "22-9,9-5/29-28,28-12\n");

		jbCalc = new JButton("Calc");
		jbCalc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calc();
			}
		});

		jlResult = new JLabel();

		JPanel north = new JPanel(new GridLayout(3, 1));
		north.add(jtfRoute1);
		north.add(jtfRoute2);
		north.add(jlResult);

		setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		add(new JScrollPane(jtaAlternatives), BorderLayout.CENTER);

		add(jbCalc, BorderLayout.SOUTH);

	}

	private void calc() {
		maharano = tool.getModel().getMaharano();

		double maharanoMin = Double.POSITIVE_INFINITY;
		double maharanoMax = Double.NEGATIVE_INFINITY;

		for (double d : maharano) {
			if (Double.isNaN(d))
				continue;

			maharanoMin = Math.min(d, maharanoMin);
			maharanoMax = Math.max(d, maharanoMax);
		}

		System.out.println("maharanoMin:" + maharanoMin);
		System.out.println("maharanoMax:" + maharanoMax);

		String[][] data = tool.getModel().getData();

		Map<String, Integer> linknameIndexMap = new HashMap<String, Integer>();
		for (int i = 1; i < data.length; i++) {
			linknameIndexMap.put(data[i][1], i);
		}

		System.out.println(linknameIndexMap);

		String route1 = jtfRoute1.getText();
		String route2 = jtfRoute2.getText();

		double totalDist1 = 0;
		double totalDist2 = 0;

		{
			String[] split = route1.split(",");
			for (String str : split) {
				if (linknameIndexMap.containsKey(str)) {
					totalDist1 += Double.parseDouble(data[linknameIndexMap
							.get(str.trim())][2]);
				} else {
					System.out.println(str);
				}
			}
		}

		{
			String[] split = route2.split(",");
			for (String str : split) {
				if (linknameIndexMap.containsKey(str)) {
					totalDist2 += Double.parseDouble(data[linknameIndexMap
							.get(str.trim())][2]);
				} else {
					System.out.println(str);
				}
			}
		}

		double mDist1 = 0;
		double mDist2 = 0;

		System.out.println("total dist1=" + totalDist1);
		System.out.println("total dist2=" + totalDist2);

		String[] lines = jtaAlternatives.getText().split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty())
				continue;

			String[] alts = line.split("/");
			double d1 = 0;
			double dist1 = 0;
			{
				int count = 0;
				String[] paths = alts[0].split(",");
				for (String path : paths) {
					double m = maharano[linknameIndexMap.get(path.trim())];
					if (Double.isNaN(m)) {
						continue;
					}
					count++;
					d1 += m;
					dist1 += Double.parseDouble(data[linknameIndexMap.get(path
							.trim())][2]);
					route1 = route1.replace(path, "");
				}
				d1 /= count;
			}
			double d2 = 0;
			double dist2 = 0;
			{
				int count = 0;
				String[] paths = alts[1].split(",");
				for (String path : paths) {
//					System.err.println(path.trim());
					double m = maharano[linknameIndexMap.get(path.trim())];
					if (Double.isNaN(m)) {
						continue;
					}
					count++;
					d2 += m;
					dist2 += Double.parseDouble(data[linknameIndexMap.get(path
							.trim())][2]);
					route2 = route1.replace(path, "");
				}
				d2 /= count;
			}

			System.out.println(line + ":" + d1 + ":" + d2);

			double d1n = (d1 - maharanoMin) / (maharanoMax - maharanoMin);
			double d2n = (d2 - maharanoMin) / (maharanoMax - maharanoMin);
			double pp = Math.abs(d2n - d1n) / d1n;
			System.out.println("pp:" + (1 - pp));

			mDist1 += dist1 / totalDist1;
			mDist2 += dist2 / totalDist2 * (1 - pp);

		}

		// System.out.println("result:" + result);

		System.out.println("route1:" + route1);
		System.out.println("route2:" + route2);

		String[] paths = route1.split(",");
		for (String path : paths) {
			path = path.trim();
			if (path.isEmpty())
				continue;

			double dist = Double
					.parseDouble(data[linknameIndexMap.get(path)][2]);

			mDist1 += dist / totalDist1;
			mDist2 += dist / totalDist2;
			// mCount++;

		}

		System.out.println(mDist1);
		System.out.println(mDist2);
		double result = (1 - Math.abs(mDist1 - mDist2) / mDist1) * 100;
		System.out.println("result:" + result);

		jlResult.setText("" + result);
	}

}
