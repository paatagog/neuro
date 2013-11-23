package atm;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

public class WeatherDataConverter {

	private static final SimpleDateFormat sdfOld = new SimpleDateFormat("d/m/yyyy"); 
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd"); 

	public static void convert(String fileName) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("c:\\dev\\neuro\\runtime\\weather-" + fileName + ".sql");
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\weather-" + fileName + ".txt"), Charset.forName("UTF-8"));
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).contains(fileName)) {
					String dat = lines.get(i).substring(2, lines.get(i).indexOf(fileName) + 4);
					System.out.println(dat);
					lines.set(i,lines.get(i).replace(dat, sdf.format(sdfOld.parse(dat))));
				}
				sb.append(lines.get(i));
			}
			
			System.out.print("lines count is " + lines.size());

			fos.write(sb.toString().getBytes("utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		convert("2010");
//		convert("2011");
//		convert("2012");
	}
	
}
