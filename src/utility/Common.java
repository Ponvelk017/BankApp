package utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Common {

	public static long dateToMilli(String date) {
		DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyyy");
		long milliSecond = 0l;
		try {
			Date dateMilli = dateFormat.parse(date);
			milliSecond = dateMilli.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return milliSecond;
	}

	public static long currentDate() {
		LocalDate currentDate = LocalDate.now();
        long currentTimeMillis = currentDate.toEpochDay() * 24 * 60 * 60 * 1000;
        return currentTimeMillis;
	}
	
	public static long berforNDate(int days) {
		LocalDate currentDate = LocalDate.now();
        LocalDate dateBefore30Days = currentDate.minusDays(days);
        long dateBefore = dateBefore30Days.toEpochDay() * 24 * 60 * 60 * 1000;
        return dateBefore;
	}
}
