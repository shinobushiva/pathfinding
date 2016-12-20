package jp.ac.sojou.cis.izumi.maharano;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Maharano {
	private double[] maharano;

	public void process(String[][] valStrs, int startRow, int lastCol,
			int[] cols, int[] ignoreThs) {

		double[] sumArray;
		double[] aveArray;
		int[] hitCounts;

		double[] roArray;

		sumArray = new double[lastCol];
		aveArray = new double[lastCol];
		hitCounts = new int[lastCol];

		for (int row = 0; row < valStrs.length; row++) {

			for (int i = 0; i < cols.length; i++) {
				double d = d(valStrs[row][cols[i]]);
				if (d > ignoreThs[i]) {
					sumArray[cols[i]] += d;
					hitCounts[cols[i]]++;
				}
			}
		}

		for (int i = 0; i < cols.length; i++) {
			int idx = cols[i];
			aveArray[idx] = sumArray[idx] / hitCounts[idx];
		}

		roArray = new double[sumArray.length];
		for (int col = 0; col < cols.length; col++) {
			for (int lNum = 0; lNum < valStrs.length; lNum++) {

				double d = d(valStrs[lNum][cols[col]]);
				if (d > ignoreThs[col]) {
					double dd = d - aveArray[cols[col]];
					roArray[cols[col]] += (dd * dd);
				}
			}
			roArray[cols[col]] /= (hitCounts[cols[col]] - 1);
			roArray[cols[col]] = Math.sqrt(roArray[cols[col]]);
		}

		System.out.println("before satandardise");
		for (int i = 0; i < cols.length; i++) {
			System.out.println("col=" + cols[i] + ":ave=" + aveArray[cols[i]]
					+ ":ro=" + roArray[cols[i]] + ":sum=" + sumArray[cols[i]]
					+ ":hit=" + hitCounts[cols[i]]);
		}
		System.out.println();

		// 標準化
		double[][] dVals = new double[valStrs.length][lastCol];
		for (int i = 0; i < valStrs.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				double v = d(valStrs[i][cols[j]]);
				if (v > ignoreThs[j]) {
					dVals[i][cols[j]] = (v - aveArray[cols[j]])
							/ roArray[cols[j]];
				} else {
					dVals[i][cols[j]] = Double.NaN;
				}
			}
		}
		System.out.println("standard values");
		print(dVals);

		sumArray = new double[lastCol];
		aveArray = new double[lastCol];
		hitCounts = new int[lastCol];

		for (int row = 0; row < dVals.length; row++) {
			for (int i = 0; i < cols.length; i++) {
				double d = dVals[row][cols[i]];
				if (!Double.isNaN(d)) {
					sumArray[cols[i]] += d;
					hitCounts[cols[i]]++;
				}
			}
		}

		for (int i = 0; i < cols.length; i++) {
			int idx = cols[i];
			aveArray[idx] = sumArray[idx] / hitCounts[idx];
		}

		roArray = new double[sumArray.length];
		for (int col = 0; col < cols.length; col++) {
			for (int lNum = 0; lNum < valStrs.length; lNum++) {

				double d = dVals[lNum][cols[col]];
				if (!Double.isNaN(d)) {
					double dd = d - aveArray[cols[col]];
					roArray[cols[col]] += (dd * dd);
				}
			}
			roArray[cols[col]] /= (hitCounts[cols[col]] - 1);
			roArray[cols[col]] = Math.sqrt(roArray[cols[col]]);
		}

		System.out.println("after satandardise");
		for (int i = 0; i < cols.length; i++) {
			System.out.println("col=" + cols[i] + ":ave=" + aveArray[cols[i]]
					+ ":ro=" + roArray[cols[i]] + ":sum=" + sumArray[cols[i]]
					+ ":hit=" + hitCounts[cols[i]]);
		}
		System.out.println();

		double[][] covMat = new double[cols.length][cols.length];
		for (int i = 0; i < cols.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				int hitCount = 0;
				for (int k = 0; k < dVals.length; k++) {
					double x = dVals[k][cols[i]];
					double y = dVals[k][cols[j]];

					if (!Double.isNaN(x) && !Double.isNaN(y)) {
						covMat[i][j] += (x - aveArray[cols[i]])
								* (y - aveArray[cols[j]]);
						hitCount++;
					}
				}
				covMat[i][j] /= hitCount;
				for (int k = 0; k < covMat.length; k++) {
					covMat[k][k] = 1;
				}
			}
		}
		System.out.println("cov mat");
		print(covMat);
		double[][] invCovMat = gyaku(covMat, covMat.length);
		System.out.println("inv cov mat");
		print(invCovMat);

		double[] res = new double[dVals.length];

		for (int i = 0; i < dVals.length; i++) {

			double[] m1 = new double[cols.length];

			for (int j = 0; j < cols.length; j++) {
				double v = 0;
				for (int k = 0; k < cols.length; k++) {
					v += dVals[i][cols[k]] * invCovMat[k][j];
				}
				m1[j] = v;
			}

			double result = 0;
			for (int j = 0; j < cols.length; j++) {
				result += m1[j] * dVals[i][cols[j]];
			}
			res[i] = result;
		}

		print(res);

		maharano = res;

	}

	private double d(String t) {
		try {
			return Double.parseDouble(t);

		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		}
	}

	private void print(double[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				System.out.printf("%20.10f ", mat[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	private void print(double[] mat) {
		for (int i = 0; i < mat.length; i++) {
			System.out.printf("%20.10f ", mat[i]);
		}
		System.out.println();
		System.out.println();
	}

	@SuppressWarnings("unused")
	private void printV(double[] mat) {
		for (int i = 0; i < mat.length; i++) {
			System.out.printf("%f\n", mat[i]);
		}
		System.out.println();
		System.out.println();
	}

	public static void main(String[] args) {

		int startRow = 2;
		int lastCol = 52;
		int[] cols = new int[] { 2, 3, 5, 6, 41, 42, 43, 44, 45, 46, 47, 48, 51 };
		int[] ignoreThs = new int[] { 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		String file = "resources/links.csv";
		ArrayList<String[]> valList = new ArrayList<>();

		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(file))))) {
			String line = null;

			String[] lastOnes = null;
			int lineCount = 0;
			while ((line = r.readLine()) != null) {

				String[] vals = line.split(",");

				if (vals.length < lastCol) {
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

		new Maharano().process(valStrs, startRow, lastCol, cols, ignoreThs);

		// テストデータ
		// valStrs = new String[][] { { "40", "80" }, { "80", "90" },
		// { "90", "100" } };
		// new Test1().process(valStrs, 0, 2, new int[] { 0, 1 },
		// new int[] { 0, 0 });

	}

	// pivotは、消去演算を行う前に、対象となる行を基準とし、それ以降の
	// 行の中から枢軸要素の絶対値が最大となる行を見つけ出し、対象の行と
	// その行とを入れ替えることを行う関数である。
	void pivot(int k, double a[][], int N) {
		double max, copy;
		// ipは絶対値最大となるk列の要素の存在する行を示す変数で、
		// とりあえずk行とする
		int ip = k;
		// k列の要素のうち絶対値最大のものを示す変数maxの値をとりあえず
		// max=|a[k][k]|とする
		max = Math.abs(a[k][k]);
		// k+1行以降、最後の行まで、|a[i][k]|の最大値とそれが存在する行を
		// 調べる
		for (int i = k + 1; i < N; i++) {
			if (max < Math.abs(a[i][k])) {
				ip = i;
				max = Math.abs(a[i][k]);
			}
		}
		if (ip != k) {
			for (int j = 0; j < 2 * N; j++) {
				// 入れ替え作業
				copy = a[ip][j];
				a[ip][j] = a[k][j];
				a[k][j] = copy;
			}
		}
	}

	// ガウス・ジョルダン法により、消去演算を行う
	void sweep(int k, double a[][], int N) {
		double piv, mmm;
		// 枢軸要素をpivとおく
		piv = a[k][k];
		// k行の要素をすべてpivで割る a[k][k]=1となる
		for (int j = 0; j < 2 * N; j++)
			a[k][j] = a[k][j] / piv;
		//
		for (int i = 0; i < N; i++) {
			mmm = a[i][k];
			// a[k][k]=1で、それ以外のk列要素は0となる
			// k行以外
			if (i != k) {
				// i行において、k列から2N-1列まで行う
				for (int j = k; j < 2 * N; j++)
					a[i][j] = a[i][j] - mmm * a[k][j];
			}
		}
	}

	// 逆行列を求める演算
	double[][] gyaku(double a[][], int N) {

		double b[][] = new double[N][N * 2];
		double c[][] = new double[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				b[i][j] = a[i][j];
			}
			b[i][N + i] = 1;
		}

		// print(b);
		for (int k = 0; k < N; k++) {
			pivot(k, b, N);
			sweep(k, b, N);
		}
		// print(b);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				c[i][j] = b[i][j + N];
			}
		}
		return c;
	}

	public double[] getMaharano() {
		return maharano;
	}

	public void setMaharano(double[] maharano) {
		this.maharano = maharano;
	}
}
