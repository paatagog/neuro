package book;

import java.io.FileOutputStream;

public class GoogleBookDownload {
	
	public static void main(String[] args) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("c:\\dev\\neuro\\runtime\\RosenblattBook.html");
			StringBuilder sb = new StringBuilder();
			
			sb.append("<html><body>");
			for (int i = 0; i < 8; i++) {
				sb.append("<img src=\"http://babel.hathitrust.org/cgi/imgsrv/image?id=mdp.39015039846566;width=1020;height=1020;orient=0;seq=" + i + "\"><br/>");
			}
			
			sb.append("</body></html>");

			fos.write(sb.toString().getBytes("utf-8"));
			
			System.out.print("Success!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
