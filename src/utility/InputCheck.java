package utility;

public class InputCheck {

	private static String nullStatement = "Provided input is null , Please provide a valid input . :( ";
	private static String negativeIntegerStatement = "Provide a Positive Integer to Process . :(";

	public static void checkNull(Object input) throws InvalidInputException {
		if (input == null) {
			throw new InvalidInputException(nullStatement);
		}
	}

	public static void checkNegativeInteger(int input) throws InvalidInputException {
		if (input < 0) {
			throw new InvalidInputException(negativeIntegerStatement);
		}
	}

	public static void checkNegativeInteger(long input) throws InvalidInputException {
		if (input < 0) {
			throw new InvalidInputException(negativeIntegerStatement);
		}
	}

	public void sendMessage(String message) throws InvalidInputException {
		throw new InvalidInputException(message);
	}
}
