package atm;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NetworkDataConverter {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Map<Date, Holiday> hollidays = new LinkedHashMap<Date, Holiday>();
	
	private static Map<Date, Weather> weathers = new HashMap<Date, Weather>();

	private static List<Payment> payments = new ArrayList<Payment>();

	public static void loadHolidayData(String fileName) {
		try {
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\" + fileName), Charset.forName("UTF-8"));
			
			for (int i = 0; i < lines.size(); i++) {
				String[] hol = lines.get(i).replaceAll("\"", "").split(",");
				Holiday holiday = new Holiday();
				holiday.setDate(sdf.parse(hol[0]));
				holiday.setImportance(Integer.parseInt(hol[1]));
				StringBuilder sb = new StringBuilder();
				for (int j = 2; j < hol.length; j++) {
					sb.append(hol[j] + ",");
				}
				holiday.setDescription(hol[2]);
				if (hollidays.containsKey(holiday.getDate())) {
					System.out.println(holiday.getDate());
				}
				hollidays.put(holiday.getDate(), holiday);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("loaded " + hollidays.size() + " holidays;");
	}
	
	public static void loadWeatherData(String fileName) {
		try {
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\" + fileName), Charset.forName("UTF-8"));
			
			for (int i = 0; i < lines.size(); i++) {
				String[] weat = lines.get(i).replaceAll("\"", "").split(",");
				Weather weather = new Weather();
				weather.setDate(sdf.parse(weat[0]));
				weather.setMaxTemperature(weat[1].equals("NULL") ? null : Double.parseDouble(weat[1]));
				weather.setMinTemperature(weat[2].equals("NULL") ? null : Double.parseDouble(weat[2]));
				weather.setMaxWindSpeed(weat[3].equals("NULL") ? null : Double.parseDouble(weat[3]));
				weather.setMaxWindGust(weat[4].equals("NULL") ? null : Double.parseDouble(weat[4]));
				weather.setPrecipitation(weat[5].equals("NULL") ? null : Double.parseDouble(weat[5]));
				weather.setSnowDepth(weat[6].equals("NULL") ? null : Double.parseDouble(weat[6]));
				StringBuilder sb = new StringBuilder();
				for (int j = 2; j < weat.length; j++) {
					sb.append(weat[j] + ",");
				}
				weather.setWeatherEvent(sb.length() == 0 ? null : sb.toString().substring(0, sb.length()-1));
				weathers.put(weather.getDate(), weather);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("loaded " + weathers.size() + " weathers;");
	}
	
	private static void loadPaymentData(String fileName) {
		try {
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\" + fileName), Charset.forName("UTF-8"));
			
			for (int i = 0; i < lines.size(); i++) {
				String[] pay = lines.get(i).replaceAll("\"", "").split(",");
				Payment payment = new Payment();
				payment.setTime(sdf.parse(pay[0]));
				payment.setAmount(Integer.parseInt(pay[1]));
				payment.setPayAmount(Integer.parseInt(pay[2]));
				payment.setCommission(Integer.parseInt(pay[3]));
				payments.add(payment);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("loaded " + payments.size() + " payments;");
	}
	
	private static void saveNetworkData(String fileName) {
		FileOutputStream fos;
		StringBuilder sb = new StringBuilder();
		for (Payment payment : payments) {
			sb.append("\"");
			sb.append(sdf.format(payment.getTime()));
			sb.append("\"");
			sb.append(",");
			sb.append("\"");
			sb.append(payment.getAmount());
			sb.append("\"");
			sb.append(",");
			sb.append("\"");
			if (hollidays.containsKey(payment.getTime()) && hollidays.get(payment.getTime()).getImportance() > 2) {
				sb.append(hollidays.get(payment.getTime()).getImportance());
			} else {
				sb.append("0");
			}
			sb.append("\"");
			sb.append(",");
			sb.append("\"");
			if (weathers.containsKey(payment.getTime()) && weathers.get(payment.getTime()).getMinTemperature() != null) {
				sb.append(weathers.get(payment.getTime()).getMinTemperature());
			} else {
				sb.append("100");
			}
			sb.append("\"");
			sb.append(",");
			sb.append("\"");
			if (weathers.containsKey(payment.getTime()) && weathers.get(payment.getTime()).getWeatherEvent() != null && weathers.get(payment.getTime()).getWeatherEvent().contains("Snow")) {
				sb.append("10");
			} else {
				sb.append("0");
			}
			sb.append("\"");
			sb.append("\n");
		}
		try {
			fos = new FileOutputStream("c:\\dev\\neuro\\runtime\\" + fileName);
			fos.write(sb.toString().getBytes("utf-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		System.out.println("saved " + payments.size() + " samples;");
	}

	public static void main(String[] args) {
		loadHolidayData("holiday.csv");
		loadWeatherData("weather.csv");
		loadPaymentData("payment.csv");
		saveNetworkData("samples.csv");		
	}


}
