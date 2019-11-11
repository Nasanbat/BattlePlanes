import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import com.sun.org.apache.xerces.internal.jaxp.datatype.*;

public class CopyFiles {
	public static String hashSHA1(String value) {
		String sha1 = "";
		try {

			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(value.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static <T> boolean isXMLGregorianCalendar(Class<T> cl) {
		String clName = cl.getName();
		if (clName.contains("XMLGregorianCalendar"))
			return true;
		else
			return false;
	}

	public static <T> boolean isXMLGregorianCalendar2(Class<T> cl) {
		return cl == com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl.class;
	}

	public static XMLGregorianCalendar toISODateTime(Date date, String fieldType) {
		if (date == null)
			return null;

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		XMLGregorianCalendar calendar = null;

		Calendar ca = Calendar.getInstance();
		TimeZone tz = ca.getTimeZone();
		try {
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c.get(Calendar.YEAR),
					c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
					c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND),
					tz.getOffset(new Date().getTime()) / 1000 / 60);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fieldType.equals("ISONormalisedDateTime"))
			calendar.setTimezone(0);

		return calendar;
	}

	public static void main(String[] args) throws IOException {
		// String.format("%s-%s-%s-%s-%s-%s-%s", this.envId, this.custCode, this.isProd,
		// this.isRealProd, this.ownerId, this.host, this.port);
		/*
		 * XMLGregorianCalendar date = toISODateTime(new Date(),
		 * "ISONormalisedDateTime"); if (isXMLGregorianCalendar(date.getClass())) {
		 * System.out.println("OK" + date); } else { System.out.println("Failed" +
		 * date); }
		 * 
		 * com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl date2 =
		 * null; date2 = new DatatypeFactoryImpl();
		 * 
		 * if (isXMLGregorianCalendar2(date2.getClass())) { System.out.println("2  OK" +
		 * date); } else { System.out.println("2 Failed" + date); }
		 */
		 
		//envId, custCode, isProd,isRealProd, ownerId, host, port
		System.out.println(hashSHA1("258-DBM-1-1-1003-172.16.116.32-2044"));
		System.out.println(hashSHA1("259-BOGD-1-1-1008-172.16.116.32-2045"));
		System.out.println(hashSHA1("260-CREDITBANK-1-1-1003-172.16.116.32-2046"));
		System.out.println(hashSHA1("273-NIB-1-1-1057-172.16.116.33-3056"));
		System.out.println(hashSHA1("262-NOMIN-1-1-1003-172.16.116.33-3054"));
		System.out.println(hashSHA1("239-CKB-1-1-1003-172.16.116.34-4001"));
		System.out.println(hashSHA1("25-TDB-1-1-1029-172.16.116.32-2008"));
		System.out.println(hashSHA1("257-TRANS-1-1-1016-172.16.116.32-2043"));
		System.out.println(hashSHA1("287-STATE-1-1-1053-172.16.116.32-2048"));
		System.out.println(hashSHA1("289-CAPITRON-1-1-1057-172.16.116.33-3060"));
		System.out.println(hashSHA1("229-UBCB-1-1-1057-172.16.116.33-3059"));
		

		System.out.println(hashSHA1("229-GCM-0-0-16-172.16.116.33-3052"));
	}

	public static void main1(String[] args) throws IOException {
		// String.format("%s-%s-%s-%s-%s-%s-%s", this.envId, this.custCode, this.isProd,
		// this.isRealProd, this.ownerId, this.host, this.port);
		// System.out.println(hashSHA1("283-CKB-1-0-1053-172.16.116.34-4003"));
		String envId = "1057";
		if (args != null && args.length >= 1) {
			envId = args[0].trim();
		} else {
			byte[] b = new byte[100];
			System.in.read(b);
			envId = (new String(b)).trim();
		}

		String folder = "\\\\mngc-lab" + envId.substring(0, 1) + "\\NES\\";
		File mainFolder = new File(folder);
		File[] folders = mainFolder.listFiles();
		for (File f0 : folders) {
			if (f0.isDirectory() && f0.getName().startsWith(envId)) {
				folder = f0.getPath();
				break;
			}
		}

		String dbFolder = folder + "\\bin\\db";
		String dbFolderNotFound = "D:\\" + envId + "_" + (new Date()).hashCode() + "\\NotFound";
		String logFile = "3_info_DbChanges.log";
		String logPath = "D:\\log";

		// logPath = "\\\\mngc-lab3\\NES\\3057_WJB_CREDITBANK_DEV_ACH\\bin\\nes_log";

		File LogRoot = new File(logPath);
		List<String> dbFileNames = new ArrayList<>();
		File[] logfiles = LogRoot.listFiles();
		for (File f : logfiles) {
			File[] logfiles2 = f.listFiles();
			for (File f1 : logfiles2) {
				if (f1.getName().equals(logFile)) {
					List<String> tmps = readFiles(f1);

					System.out.println(" " + f1.getPath());
					if (tmps != null) {
						System.out.println("log is Not Found files count: " + tmps.size());
						dbFileNames.addAll(tmps);
					} else {
						System.out.println("log is Not Found files count 0 ");
					}
				}
			}
		}

		File root = new File(dbFolder);
		File[] files = root.listFiles();
		System.out.println("this log is Not Found files count: " + dbFileNames.size());
		for (String dbfileName : dbFileNames) {
			boolean isCopy = false;
			for (File f : files) {
				if (f.getName().toUpperCase().equals(dbfileName.toUpperCase())) {

					File f3 = new File(dbFolderNotFound);
					if (f3.mkdirs()) {
						System.out.println("Directory created: ");
					}
					Files.copy(f.toPath(), Paths.get(dbFolderNotFound, dbfileName),
							StandardCopyOption.REPLACE_EXISTING);
					isCopy = true;
				}
			}
			if (!isCopy)
				System.out.println("file Not Found, " + dbfileName);
		}

	}

	static List<String> readFiles(File f) throws IOException {

		List<String> dbFileNames = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			String ln;
			try {
				while ((ln = br.readLine()) != null) {

					String line = ln.trim();
					if (line.contains("Not found")) {
						String[] slip = line.replace("|", "").split("Not found");
						if (slip.length >= 2)
							dbFileNames.add(slip[1].trim());
					}

				}
			} catch (Exception e) {
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbFileNames;
	}

}
