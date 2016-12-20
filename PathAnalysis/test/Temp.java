public class Temp {

	public static void main(String[] args) {

		double p = 1.0;
		double d = 0.96 * 0.33 + p * 0.933 * 0.33 + 0.96 * 0.33;

		System.out.println(Math.abs(10.0 - 4.0) / 4.0);

		double a1 = Math.abs(30.0 / 90.0 - 30.0 / 100.0);
		System.out.println(a1);
		System.out.println(1 - a1);

		System.out.println(0.9666666666666667 * 0.33);

		System.out.println(d);

		System.out.println(1 - Math.abs(90.0 - 100.0) / 90.0);
	}

}
