package logger;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ColoredLogger extends Formatter {

	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_PURPLE = "\u001B[35m";

	@Override
	public String format(LogRecord record) {
		StringBuilder coloredMessage = new StringBuilder();
		if (record.getLevel() == Level.INFO) {
			coloredMessage.append(ANSI_GREEN);
		}
		else if(record.getLevel() == Level.WARNING) {
			coloredMessage.append(ANSI_RED);
		}
		else {
			coloredMessage.append(ANSI_PURPLE);
		}
		coloredMessage.append(record.getMessage()).append("\n");
		
		return coloredMessage.toString();
	}
}
