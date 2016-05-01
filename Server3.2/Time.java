package lock;
import java.util.Calendar;

/**
 * Class that handles date and time.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class Time {	
	public static Calendar cal;
	public static int second, minute, hour, dayOfMonth, year;
	public static String day, month;

	/**
	 * A constructor that recieves the current date and time.
	 */
	public static String getTime() {
		cal = Calendar.getInstance();
		second = cal.get( Calendar.SECOND );
		minute = cal.get( Calendar.MINUTE );
		hour = cal.get( Calendar.HOUR_OF_DAY );
		day = dayOfWeek( cal.get( Calendar.DAY_OF_WEEK ) );
		month = monthOfYear( cal.get( Calendar.MONTH ) );
		dayOfMonth = cal.get( Calendar.DAY_OF_MONTH );
		year = cal.get( Calendar.YEAR );
		return day + " " + dayOfMonth + " " + month + " " + year + " " + String.format( "%02d",hour ) +
				":" + String.format( "%02d",minute ) + ":" + String.format( "%02d",second );
	}
	
	/**
	 * A function that translates an int to the day of the week.
	 * 
	 * @param day An int that represents the day of the week.
	 */
	public static String dayOfWeek( int day ) {
		String dayOfWeek = ""; 
		if( day == 1 ) {
			dayOfWeek = "Sunday";
		}
		if( day == 2 ) {
			dayOfWeek = "Monday";
		}
		if( day == 3 ) {
			dayOfWeek = "Tuesday";
		}
		if( day == 4 ) {
			dayOfWeek = "Wednesday";
		}
		if( day == 5 ) {
			dayOfWeek = "Thursday";
		}
		if( day == 6 ) {
			dayOfWeek = "Friday";
		}
		if( day == 7 ) {
			dayOfWeek = "Saturday";
		} 
		return dayOfWeek;
	}
	
	/**
	 * A function that translates an int to the month of the year.
	 * 
	 * @param day An int that represents the month of the year.
	 */
	public static String monthOfYear( int month ) {
		String monthOfYear = ""; 
		if( month == 0 ) {
			monthOfYear = "January";
		}
		if( month == 1 ) {
			monthOfYear = "February";
		}
		if( month == 2 ) {
			monthOfYear = "March";
		}
		if( month == 3 ) {
			monthOfYear = "April";
		}
		if( month == 4 ) {
			monthOfYear = "May";
		}
		if( month == 5 ) {
			monthOfYear = "June";
		}
		if( month == 6 ) {
			monthOfYear = "Juli";
		} 
		if( month == 7 ) {
			monthOfYear = "August";
		}
		if( month == 8 ) {
			monthOfYear = "September";
		}
		if( month == 9 ) {
			monthOfYear = "October";
		}
		if( month == 10 ) {
			monthOfYear = "November";
		}
		if( month == 11 ) {
			monthOfYear = "December";
		}
		return monthOfYear;
	}
}
