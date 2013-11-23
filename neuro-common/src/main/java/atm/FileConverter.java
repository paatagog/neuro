package atm;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileConverter {
	
	public static void main(String[] args) {
//		processFile("2011-feb");
//		processFile("2011-jan");
//		processFile("2011-jul");
//		processFile("2011-jun");
//		processFile("2011-mar");
//		processFile("2011-may");
//		processFile("2011-nov");
//		processFile("2011-oct");
//		processFile("2011-sep");

//		processFile("2012-jan");
//		processFile("2012-feb");
//		processFile("2012-mar");
//		processFile("2012-apr");
//		processFile("2012-may");
//		processFile("2012-jun");
//		processFile("2012-jul");
//		processFile("2012-aug");
//		processFile("2012-sep");
//		processFile("2012-oct");
		processFile("2012-nov");
		System.out.println("Success!");
		
	}

	private static void processFile(String fileName) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("c:\\dev\\neuro\\runtime\\" + fileName + ".sql");
			List<String> lines=Files.readAllLines(Paths.get("c:\\dev\\neuro\\runtime\\" + fileName + ".csv"), Charset.forName("UTF-8"));
			StringBuilder sb = new StringBuilder();
			
			sb.append("INSERT INTO payment VALUES ");
			appendRow(sb, lines.get(1));
			
			for (int i = 2; i < lines.size() - 1; i++) {
//				System.out.println(i + "; ");
				if (i % 1000 == 0) {
					sb.append("; \n");
					sb.append("INSERT INTO payment VALUES ");
				} else {
					sb.append(",");
				}
				appendRow(sb, lines.get(i));
			}

			sb.append(",");
			appendRow(sb, lines.get(lines.size() - 1));
			sb.append("; \n");

			fos.write(sb.toString().getBytes("utf-8"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void appendRow(StringBuilder sb, String line) {
		String[] rows = line.replaceAll("\"", "").split(",");

		sb.append("(");
		sb.append("'").append(rows[1].replace(" ", "T")).append("Z").append("'").append(",");
		sb.append(rows[2]).append(",");
		sb.append(rows[3]).append(",");
		sb.append(rows[4]).append(",");
		sb.append(rows[5]).append(",");
		if (rows.length < 7) {
			sb.append("'GEL'");
		} else {
			sb.append("'").append(rows[6]).append("'");
		}
		sb.append(")");
	}

}
