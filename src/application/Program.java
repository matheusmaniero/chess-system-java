package application;

import java.util.Locale;
import java.util.Scanner;

import boardgame.Position;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		Position p1 = new Position(2, 5);
		System.out.println(p1);

		sc.close();
	}

}
