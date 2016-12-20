package jp.ac.sojou.cis.izumi.maharano;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class MapPanel extends JPanel {

	enum ShowMode {
		SB, BS, BOTH,
	}

	private ShowMode showMode = ShowMode.BOTH;

	public MapPanel() {
		super();
		setFocusable(true);

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					yOff += 1;
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					yOff -= 1;
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					xOff -= 1;
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					xOff += 1;
					repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_C) {
					xOff = 0;
					yOff = 0;
					repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_1) {
					scale = scale * 1.1;
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_2) {
					scale = scale / 1.1;
					repaint();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_M) {

					System.out.println(e);
					switch (showMode) {

					case SB:
						showMode = ShowMode.BS;
						break;
					case BS:
						showMode = ShowMode.BOTH;
						break;
					case BOTH:
						showMode = ShowMode.SB;
						break;
					}
					repaint();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {

				if (KeyEvent.getKeyText(KeyEvent.VK_0).equals(
						"" + e.getKeyChar())) {
					scale = 1;
					repaint();
				}

			}
		});
	}

	private String data[][];
	private Map<String, Point2D> nodeMap;
	private Map<String, Line2D> linkMap;
	private Map<String, String[]> linkDataMap;
	private Map<String, Color> linkColorMap = new HashMap<>();
	private double[] bounds;

	private double[] maharano;

	public double[] getMaharano() {
		return maharano;
	}

	public void setMaharano(double[] maharano) {
		this.maharano = maharano;

		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;

		for (double d : maharano) {
			if (Double.isNaN(d))
				continue;

			min = Math.min(d, min);
			max = Math.max(d, max);
		}
		System.out.println("min:" + min);
		System.out.println("max:" + max);

		linkColorMap.clear();

		for (int i = 0; i < maharano.length; i++) {
			if (Double.isNaN(maharano[i])) {
				linkColorMap.put("" + (i), Color.GRAY);
				continue;
			}

			double d = (maharano[i] - min) / (max - min);
			// System.out.println(d);

			int pivot = 0;
			for (int j = 0; j < getPivots().length; j++) {
				if (getPivots()[j] >= d * 100) {
					pivot = j;
					break;
				}
			}
			linkColorMap.put("" + (i), getColors()[pivot]);
		}

		repaint();
	}

	private double scale = 1;
	private double xOff = 0;
	private double yOff = 0;

	public void setScale(double mag) {
		if (mag > 0)
			this.scale = mag;
	}

	public void setOffset(double x, double y) {
		xOff = x;
		yOff = y;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawString(showMode.toString(), 20, 20);

		Rectangle rect = g2d.getClipBounds();
		double clipSize = Math.min(rect.width, rect.height);
		double mapSize = Math.max(bounds[2] - bounds[0], bounds[3] - bounds[1]);
		// System.out.println(clipSize);
		// System.out.println(mapSize);

		double mag = clipSize / mapSize * scale;
		double xOffset = bounds[0] - 100000 / 2 + xOff * 10000;
		double yOffset = bounds[1] - 100000 / 2 + yOff * 10000;
		// System.out.println(xOffset);
		// System.out.println(yOffset);
		// System.out.println(mag);

		// AffineTransform af = AffineTransform.getScaleInstance(1, -1);
		// af.translate(0, -rect.height);
		// g2d.setTransform(af);

		BasicStroke bs = new BasicStroke(2);
		g2d.setStroke(bs);
		g2d.setColor(Color.GRAY);
		for (String key : linkMap.keySet()) {

			String[] vals = linkDataMap.get(key)[1].split("-");
			boolean flag = Integer.parseInt(vals[0]) < Integer
					.parseInt(vals[1]);

			Line2D line = linkMap.get(key);
			Point2D p1 = line.getP1();
			Point2D p2 = line.getP2();

			if (maharano != null) {
				g2d.setColor(linkColorMap.get(key));
			}
			if (flag) {
				if (showMode != ShowMode.BS) {
					Line2D dl = new Line2D.Double(
							((p1.getX() - xOffset) * mag),
							((p1.getY() - yOffset) * mag) * -1 + rect.height,
							((p2.getX() - xOffset) * mag),
							((p2.getY() - yOffset) * mag) * -1 + rect.height);
					g2d.draw(dl);
				}
			} else {
				if (showMode != ShowMode.SB) {
					double dd = 2;
					Line2D dl = new Line2D.Double(((p1.getX() - xOffset) * mag)
							+ dd, ((p1.getY() - yOffset) * mag) * -1
							+ rect.height - dd, ((p2.getX() - xOffset) * mag)
							+ dd, ((p2.getY() - yOffset) * mag) * -1
							+ rect.height - dd);
					g2d.draw(dl);
				}
			}
		}

		g2d.setColor(Color.GRAY);
		for (String key : nodeMap.keySet()) {
			Point2D p = nodeMap.get(key);
			g2d.drawString(key, (int) ((p.getX() - xOffset) * mag),
					(int) ((p.getY() - yOffset) * mag * -1 + rect.height));
		}
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] nodeData, String[][] linkData) {
		this.data = nodeData;

		bounds = new double[] { Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
				Double.NEGATIVE_INFINITY };

		nodeMap = new HashMap<String, Point2D>();
		for (int i = 0; i < nodeData.length; i++) {
			String id = nodeData[i][0];
			Double x = Double.parseDouble(nodeData[i][1]);
			Double y = Double.parseDouble(nodeData[i][2]);
			nodeMap.put(id, new Point2D.Double(x, y));

			bounds[0] = Math.min(bounds[0], x);
			bounds[1] = Math.min(bounds[1], y);
			bounds[2] = Math.max(bounds[2], x);
			bounds[3] = Math.max(bounds[3], y);
		}

		linkMap = new HashMap<>();
		linkDataMap = new HashMap<>();

		for (int i = 2; i < linkData.length; i++) {
			String[] ns = linkData[i][1].split("-");
			linkMap.put(linkData[i][0], new Line2D.Double(nodeMap.get(ns[0]),
					nodeMap.get(ns[1])));
			linkDataMap.put(linkData[i][0], linkData[i]);
		}
		repaint();
	}

	public double[] getPivots() {
		return pivots;
	}

	public void setPivots(double[] pivots) {
		this.pivots = pivots;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1194792489806779461L;
	private double[] pivots;
	private Color[] colors;

}