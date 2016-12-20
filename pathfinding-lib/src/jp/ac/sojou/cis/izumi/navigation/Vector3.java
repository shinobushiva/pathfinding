package jp.ac.sojou.cis.izumi.navigation;

public class Vector3 {

	public Vector3(double x2, double y2, double z2) {
		x = x2;
		y = y2;
		z = z2;
	}

	public double x;
	public double y;
	public double z;

	public static Double distance(Vector3 p1, Vector3 p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)
				+ Math.pow(p1.z - p2.z, 2));

	}

}
