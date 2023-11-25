package classes.util;



public class PrintWithColor {

	static String green = "\u001B[32m";
	static String reset = "\u001B[0m";
	static String red = "\u001B[31m";
	static String yellow = "\u001B[33m";
	static String blue = "\u001B[34m";
	static String black = "\u001B[30;47m";
	static String bold = "\u001b[1m";

	public static void print(String message, String color) {


			switch (color) {
				case "red" -> color = red;
				case "green" -> color = green;
				case "blue" -> color = blue;
				case "yellow" -> color = yellow;
				case "black" -> color = black;
				default -> {
					color = reset;
					bold = "";
				}

		}

		System.out.println(color + bold + message + reset);
	}

	public static void printMessage(String message) {
		System.out.println(blue + bold + message + reset);
	}
	public static void printError(String message) {
		System.out.println(red + bold + message + reset);
	}
	public static void printSucces(String message) {
		System.out.println(green + bold + message + reset);
	}
	public static void printWarning(String message) {
		System.out.println(yellow + bold + message + reset);
	}
}
