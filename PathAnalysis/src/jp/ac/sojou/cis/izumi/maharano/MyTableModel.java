package jp.ac.sojou.cis.izumi.maharano;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2792712587334161179L;

	private String[][] data;
	private int colCount;

	private double[] weights = new double[0];

	private double[] maharano;

	@Override
	public int getRowCount() {
		return data.length - 1;
	}

	@Override
	public int getColumnCount() {
		return getColCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			if (maharano != null) {
				return "" + maharano[rowIndex + 1];
			} else {
				return "" + Double.NaN;
			}
		}
		return data[rowIndex + 1][columnIndex];
	}

	public String[][] getData() {
		return data;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "マハラノビス距離";
		}
		return data[0][column];
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public double[] getMaharano() {
		return maharano;
	}

	public void setMaharano(double[] maharano) {
		this.maharano = maharano;
	}

}