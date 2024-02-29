package customIO;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import customLogics.UserFunctions;
import dbLogics.CustomerOperations;
import logger.ColoredLogger;
import utility.InvalidInputException;

public class Bankapp {
//	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
	public static Logger logger = Logger.getLogger(Bankapp.class.getName());

	public static void main(String[] args) {

		logger.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new ColoredLogger());
		logger.addHandler(handler);

		FileHandler fileHandler;

		UserFunctions userFunction = new UserFunctions();
		CustomerIO customerIo = new CustomerIO();
		EmployeeIO employeeIo = new EmployeeIO();
		ManagerIO managerIO = new ManagerIO();

		Scanner scanner = new Scanner(System.in);
		try {
			fileHandler = new FileHandler("Bankapp.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);

			logger.info(String.format("%50s", " Welcome to Bank of CBE "));
			boolean breakCondition = true;
			while (breakCondition) {
				logger.info("-" + "-".repeat(40) + "-");
				logger.info("1.Log in\n2.Exit");
				String loginOption = scanner.next();
				switch (loginOption) {
				case "1": {
					int userId;
					String password;
					logger.info("-" + "-".repeat(40) + "-");
					logger.info("Enter the login credentials");
					logger.info(String.format("%10s", "Enter UserId :  "));
					userId = scanner.nextInt();
					scanner.nextLine();
					if (userFunction.isUser(userId) == -1) {
						logger.warning("Invalid userId :( ");
						continue;
					}
					logger.info(String.format("%10s", "Enter Password :"));
					password = scanner.nextLine();
					logger.info("-" + "-".repeat(40) + "-");

					Map<String, Object> record = userFunction.blockedDetails(userId);
					int invaildAttempts = (int) record.get("InvalidAttempts");
					if ((int) record.get("InvalidAttempts") > 2) {
						Instant currentTime = Instant.now();
						Instant blockedTime = Instant.ofEpochMilli((long) record.get("BlockedTime"));
						long timeDifference = Duration.between(currentTime, blockedTime).toMillis();
						if (timeDifference <= (24 * 60 * 60 * 1000L)) {
							userFunction.deleteBlockedUser(userId);
						}
						if (invaildAttempts == 2) {
							logger.warning("Your Account has been locked.You cannot login now :(");
							continue;
						}
					}
					boolean isAuth = userFunction.login(userId, password);
					if (isAuth) {
						logger.info("Logged in successfully");
						String isUser = userFunction.getDesignation(userId);
						switch (isUser) {
						case "Customer": {
							customerIo.customerAction(userId);
						}
							break;
						case "Employee": {
							employeeIo.employeeActions(userId);
						}
							break;
						case "Manager": {
							managerIO.managerActions();
						}
							break;
						}
					} else {
						if (invaildAttempts < 2) {
							logger.warning("you have only " + (2 - invaildAttempts) + " Attempts Left :(");
						}
						if (invaildAttempts >= 2) {
							logger.warning("Your Account has been locked.You cannot login now :(");
						} else {
							userFunction.addBlockedUser(userId, invaildAttempts + 1, Instant.now().toEpochMilli());
							logger.info("You Entered a invalid credentials");
						}
					}
				}
					break;
				case "2": {
					breakCondition = false;
				}
					break;
				default: {
					logger.warning("Invalid Input");
				}
				}

			}
		} catch (IOException e) {
			logger.log(Level.INFO, "An Exception occured ! Sorry for the Inconvenience", e);
		} catch (InvalidInputException e) {
			logger.log(Level.INFO, "An Exception occured ! Sorry for the Inconvenience", e);
		}
		scanner.close();
	}
}
