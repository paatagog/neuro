package atm;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmSampleDataConverter {
	
	private static final double CONFIDENTIAL_PARAMETER = 0.015;
	
	private static final double SCALE_FACTOR = 100;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private List<AtmDailyDataSample> samples = new ArrayList<AtmDailyDataSample> ();

	private int monthStart;

	private int monthEnd;
	
	private Map<Date, Double> maxAmountsMap = new HashMap<Date, Double>();

	private void readAtmSampleData(String fileName) {
		try {
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\" + fileName), Charset.forName("UTF-8"));
			
			for (int i = 0; i < lines.size(); i++) {
				String[] pay = lines.get(i).replaceAll("\"", "").split(",");
				AtmDailyDataSample sample = new AtmDailyDataSample();
				sample.setDate(sdf.parse(pay[0]));
				sample.setAmount(Integer.parseInt(pay[1]));
				sample.setHolliday(Integer.parseInt(pay[2]));
				sample.setTemperature(Double.parseDouble(pay[3]));
				sample.setWeatherEvent(Integer.parseInt(pay[4]));
				samples.add(sample);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void adjustPaymentData() {
		for (int i = 0; i < samples.size(); i++) {
			AtmDailyDataSample sample = samples.get(i);
			sample.setAmount(sample.getAmount() * CONFIDENTIAL_PARAMETER);
			if (i == 0) {
				startMonth(i);
			} else if (sample.getMonth() != samples.get(i - 1).getMonth()) {
				endMonth(i - 1);
				scaleMonth();
				startMonth(i);
			} else if (i == samples.size() - 1) {
				endMonth(i);
				scaleMonth();
			}
		}
	}

	private void startMonth(int i) {
		monthStart = i;
	}
	
	private void endMonth(int i) {
		monthEnd = i;
	}
	
	private void scaleMonth() {
		if ( monthStart > monthEnd) {
			return;
		}
		
		AtmDailyDataSample maxElement = Collections.max(samples.subList(monthStart, monthEnd + 1), new Comparator<AtmDailyDataSample>() {

			@Override
			public int compare(AtmDailyDataSample o1, AtmDailyDataSample o2) {
				return (int) Math.signum(o1.getAmount() - o2.getAmount());
			}
		});
		
		maxElement.setMaxElement(true);
		double maxAmont = maxElement.getAmount();
		
		maxAmountsMap.put(samples.get(monthStart).getDate(), maxAmont);
		
		for (int i = monthStart; i <= monthEnd; i++) {
			samples.get(i).setAdjustedAmount(adjust(samples.get(i).getAmount(), maxAmont, SCALE_FACTOR));
			samples.get(i).setMonthMaxAmount(maxAmont);
		}
		
	}

	private void adjustWeekOfMonth() {
		for (AtmDailyDataSample sample : samples) {
			sample.setAdjustedWeekOfMonth((int)adjust(sample.getWeekOfMonth(), 6.5, SCALE_FACTOR));
		}		
	}

	private void adjustWeekDayData() {
		for (AtmDailyDataSample sample : samples) {
			sample.setAdjustedWeekDay((int)adjust(sample.getWeekDay(), 7.5, SCALE_FACTOR));
		}		
	}

	private void adjustHolidayData() {
		for (AtmDailyDataSample sample : samples) {
			sample.setAdjustedHolliday((int)adjust(sample.getHolliday(), 10.5, SCALE_FACTOR));
		}		
	}
	
	private double adjust(double value, double initialRange, double finalRange) {
		return (value - initialRange / 2) * 2 * finalRange / initialRange;
	}
	
	private void writeAdjustedExcelData(String fileName) {
		try {
			FileOutputStream fos;
			StringBuilder sb = new StringBuilder();
			
			for (AtmDailyDataSample sample : samples) {
				
				addCsvCell(sdf.format(sample.getDate()), sb, true);
				addCsvCell(sample.getAmount(), sb, true);
				addCsvCell(sample.getHolliday(), sb, true);
				addCsvCell(sample.getTemperature(), sb, true);
				addCsvCell(sample.getWeatherEvent(), sb, true);
				addCsvCell("    ", sb, true);
				addCsvCell(sample.getAdjustedWeekOfMonth(), sb, true);
				addCsvCell(sample.getAdjustedWeekDay(), sb, true);
				addCsvCell(sample.getAdjustedAmount(), sb, true);
				addCsvCell(sample.getAdjustedHolliday(), sb, true);
				addCsvCell("    ", sb, true);
				addCsvCell(sample.getWeekOfMonth(), sb, true);
				addCsvCell(sample.getWeekDay(), sb, true);
				addCsvCell("    ", sb, true);
				addCsvCell(sample.getMonthMaxAmount(), sb, true);
				
				sb.append("\n");
			}
			System.out.print(samples.size() + "\n");
	
			fos = new FileOutputStream("c:\\dev\\neuro\\runtime\\" + fileName);
			fos.write(sb.toString().getBytes("utf-8"));
			fos.close();			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void addCsvCell(Object value, StringBuilder sb, boolean comma) {
		sb.append("\"");
		sb.append(value);
		sb.append("\"");
		if (comma) {
			sb.append(",");
		}
	}
	
	private void writeAdjustedNetworkInputData(String fileName) {
		try {
			FileOutputStream fos;
			StringBuilder sb = new StringBuilder();
			
			for (AtmDailyDataSample sample : samples) {
				sb.append(sample.getAdjustedWeekOfMonth());
				sb.append(",");
				sb.append(sample.getAdjustedWeekDay());
				sb.append(",");
				sb.append(sample.getAdjustedHolliday());
				sb.append(";");
				sb.append(sample.getAdjustedAmount());
				sb.append("\n");
			}
			System.out.print(samples.size() + "\n");
	
			fos = new FileOutputStream("c:\\dev\\neuro\\runtime\\" + fileName);
			fos.write(sb.toString().getBytes("utf-8"));
			fos.close();			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AtmSampleDataConverter converter = new AtmSampleDataConverter();
		converter.readAtmSampleData("inputData.csv");
		converter.adjustPaymentData();
		converter.adjustHolidayData();
		converter.adjustWeekDayData();
		converter.adjustWeekOfMonth();
		converter.writeAdjustedExcelData("adjustedInputDataExcel.csv");
		converter.writeAdjustedNetworkInputData("adjustedNetworkInputData.txt");
	}


}
