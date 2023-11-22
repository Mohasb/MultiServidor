package util;



public class PrintWithColor {

	static String green = "\u001B[32m";
	static String reset = "\u001B[0m";
	static String red = "\u001B[31;47m";
	static String yellow = "\u001B[33m";
	static String blue = "\u001B[34m";
	static String black = "\u001B[30;47m";
	static String bold = "\u001b[1m";
	//static LocalDateTime currentDateTime = LocalDateTime.now();

	public static void print(String message, String color) {
		//String formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


			switch (color) {
				case "red" -> color = red;
				case "green" -> color = green;
				case "blue" -> color = blue;
				case "yellow" -> color = yellow;
				case "black" -> color = black;
				default -> {
					color = reset;
					bold = "";
					//formattedDateTime = "";
				}

		}

		System.out.println(color + bold + /*formattedDateTime +*/ message + reset);
	}

}
