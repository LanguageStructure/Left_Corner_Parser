package Unger;

public class Integral {
	public static void main(String[] args) {
		System.out.println(integralrek(0, 1));
	}

	public static double integralrek(double a, double b) {
		System.out.println(a+" "+b);
		if ((b - a) <= 0.01d) {
			System.out.println("Jumpe in berechnung");
			return (b - a) * (F_x_2(a) + F_x_2(b)) / 2d;
		} else {
			System.out.println("Rufe auf mit " + a + " " + (a + b / 2d));
			return integralrek(a, (a + b / 2d)) + integralrek((a + b / 2d), b);
		}
	}

	public static double F_x_2(double a) {
		return a * a;
	}
}