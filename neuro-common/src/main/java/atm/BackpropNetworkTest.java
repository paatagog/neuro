package atm;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BackpropNetworkTest {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static List<AtmDailyDataSample> samples = new ArrayList<AtmDailyDataSample> ();  

	private static void test01(String fileName) {
	
		try {
			// ფაილიდან წავიკითხოთ მონაცემები 
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\" + fileName), Charset.forName("UTF-8"));
			for (int i = 0; i < lines.size(); i++) {
				String[] arr = lines.get(i).replaceAll("\"", "").split(",");
				AtmDailyDataSample sample = new AtmDailyDataSample();
				sample.setDate(sdf.parse(arr[0]));
				sample.setAmount(Integer.parseInt(arr[1]));
				sample.setHolliday(Integer.parseInt(arr[2]));
				sample.setTemperature(Double.parseDouble(arr[3]));
				sample.setWeatherEvent(Integer.parseInt(arr[4]));
				samples.add(sample);
			}
			System.out.print("loaded samples count is " + lines.size());
			
			// მოვახდინოთ სკალირება
			scaleSamples();

			// შევქმნათ ნეირონული ქსელი
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void scaleSamples() {
		List<AtmDailyDataSample> scaledSamples = new ArrayList<AtmDailyDataSample> ();
		samples = scaledSamples;		
	}
	
	public static void main(String[] args) {
		test01("samples.csv");		
	}

}
