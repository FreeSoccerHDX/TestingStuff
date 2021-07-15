package freesoccerhdx.survivalplus.haupt;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeData {


	public static int getDaysOfMonth(int month){

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
		
	}
	
	public static int getDayInteger() {
		
		Date date = new Date();
		SimpleDateFormat time = new SimpleDateFormat("d", Locale.GERMAN);	
		
		return Integer.parseInt(time.format(date));
	}
	
	public static Integer getMonth(String month) {
	    SimpleDateFormat monthParse = new SimpleDateFormat("MMMM", Locale.GERMAN);
	    SimpleDateFormat monthDisplay = new SimpleDateFormat("M", Locale.GERMAN);
	    try{
			return Integer.parseInt(monthDisplay.format(monthParse.parse(month)));
		}catch(Exception e){
			e.printStackTrace();
		}
	    return 0;
	}
	
	public static Integer getMonthInteger(){
		
		Date date = new Date();
		SimpleDateFormat time = new SimpleDateFormat("M", Locale.GERMAN);	
		
		return Integer.parseInt(time.format(date));
	}
	public static String getMonthString(int month){
		
		DateFormatSymbols time = new DateFormatSymbols(Locale.GERMAN);
		return time.getMonths()[month-1];
	}
	public static Integer getYearInteger(){
		Date date = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyy", Locale.GERMAN);	
		
		return Integer.parseInt(time.format(date));
	}
	public static String getDatumTime(long TimeInMillis){
		Calendar c =  Calendar.getInstance();
		c.setTimeInMillis(TimeInMillis);
		int minu = c.get(Calendar.MINUTE);
		
		String month = "§d"+c.get(Calendar.DAY_OF_MONTH)+"."+(c.get(Calendar.MONTH)+1)+"."+c.get(Calendar.YEAR);
		String uhrzeit = "§5@"+c.get(Calendar.HOUR_OF_DAY)+":"+ (minu < 10 ? "0" + minu : minu);

		return month + " " + uhrzeit;
	}
	
	
	public static Integer[] getTime(long millis){ //Year, Month, Day_of_Month, Hour_of_Day, Minute, seconds, milliseconds
		Calendar c =  Calendar.getInstance();
		c.setTimeInMillis(millis);
		
		Integer[] timedata = new Integer[7];
		timedata[0] = c.get(Calendar.YEAR)-1970;
		timedata[1] = c.get(Calendar.MONTH)-1;
		timedata[2] = c.get(Calendar.DAY_OF_MONTH)-1;
		timedata[3] = c.get(Calendar.HOUR_OF_DAY)-1;
		timedata[4] = c.get(Calendar.MINUTE);
		timedata[5] = c.get(Calendar.SECOND);
		timedata[6] = c.get(Calendar.MILLISECOND);
		return timedata;
	}

	public static boolean sameDay(Long time1, Long time2) {
		Calendar cal1 =  Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		Calendar cal2 =  Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		
		if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
			if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
				return true;
			}
		}
		
		
		
		
		return false;
	}
	
	
}
