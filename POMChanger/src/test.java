import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public class test {

	public static void main(String[] args) throws IOException {
		/*Random rand = new Random(); 
		int value = rand.nextInt(1000); 
		System.out.println( (new Date()).getTime() +" " + value);
		
		getMessage();
		*/
		String value = "229-GCM-0-0-1075-172.16.1.33-3052";
		MessageDigest crypt = null;
		try {
			crypt = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		crypt.reset();
		crypt.update(value.getBytes("UTF-8"));
		String sha1 = byteToHex(crypt.digest());
		//b906c2426ed7bdd39aa206fc4ec7ecaf996bd322
		System.out.println( sha1);
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

	public static RTPMessage getMessage() throws IOException {
		URL getURL = new URL("http://192.168.81.58:5555");
		HttpURLConnection innerConnection = ((HttpURLConnection) getURL.openConnection());
		innerConnection.setRequestMethod("GET");

		int responseCode = innerConnection.getResponseCode();
		if (200 == responseCode) {
			String readSeq = innerConnection.getHeaderField("X-MONTRAN-RTP-ReqSts");
			if ("EMPTY".equals(readSeq)) {
				return null;
			}
			String strSeq = innerConnection.getHeaderField("X-MONTRAN-RTP-MessageSeq");
			String type = innerConnection.getHeaderField("X-MONTRAN-RTP-MessageType");
			String msgContent = ""; // readReply(innerConnection);
			BufferedReader reader = new BufferedReader(new InputStreamReader(innerConnection.getInputStream()));
			try {
				CharBuffer buffer = ByteBuffer.allocate(4096).asCharBuffer();

				while (reader.read(buffer) >= 0) {
				}
				buffer.flip();
				msgContent = buffer.toString();
				System.out.println(msgContent);
			} finally {
				reader.close();
			}
			if (innerConnection.getHeaderField("X-MONTRAN-RTP-PossibleDuplicate") != null) {

			}
			long sequence = Long.parseLong(strSeq);
			RTPMessage message = new RTPMessage(type, msgContent, sequence);
			return message;
		}
		throw new IOException("Error HTTP Request: " + responseCode);
	}

	protected static String readReply(HttpURLConnection innerConnection) throws IOException {
		/* 152 */ BufferedReader reader = new BufferedReader(new InputStreamReader(innerConnection.getInputStream()));
		try {
			/* 154 */ CharBuffer buffer = ByteBuffer.allocate(4096).asCharBuffer();

			/* 156 */ while (reader.read(buffer) >= 0) {
			}
			/* 157 */ buffer.flip();
			/* 158 */ String c = buffer.toString();
			/* 159 */ if (c.length() == 0) {
				/* 160 */ throw new IOException("Empty content");
			}
			/* 162 */ return c;
		} finally {
			/* 164 */ reader.close();
		}
	}

	public static class RTPMessage {
		private String type;

		private long sequence;

		protected String content;

		private String reportedStatus;

		private int errorCode;

		private boolean isCleared;

		private long processingDuration;

		public RTPMessage(String content) {
			/* 25 */ this.content = content;
		}

		public RTPMessage(String type, String content, long sequence) {
			/* 29 */ this(content);

			/* 31 */ this.type = type;
			/* 32 */ this.sequence = sequence;
		}

		public String getType() {
			/* 36 */ return this.type;
		}

		public void setType(String type) {
			/* 40 */ this.type = type;
		}

		public long getSequence() {
			/* 44 */ return this.sequence;
		}

		public void setSequence(long sequence) {
			/* 48 */ this.sequence = sequence;
		}

		public String getContent() {
			/* 52 */ return this.content;
		}

		public void setContent(String content) {
			/* 56 */ this.content = content;
		}

		public String getReportedStatus() {
			/* 60 */ return this.reportedStatus;
		}

		public void setReportedStatus(String reportedStatus) {
			/* 64 */ this.reportedStatus = reportedStatus;
			/* 65 */ if (("ACSP".equals(reportedStatus)) || ("ACCP".equals(reportedStatus))) {
				/* 66 */ this.isCleared = true;
				/* 67 */ } else if (reportedStatus.startsWith("RJCT")) {
				/* 68 */ int pos = reportedStatus.indexOf('/');
				/* 69 */ if (pos >= 0) {
					try {
						/* 71 */ this.errorCode = Integer.parseInt(reportedStatus.substring(pos + 1));
					} catch (NumberFormatException localNumberFormatException) {
					}
				}
				/* 74 */ if (1011 == this.errorCode) {
					/* 76 */ String sts = getXmlItemContent(this.content, "GrpSts", 0);
					/* 77 */ if (sts != null) {
						/* 78 */ this.isCleared = "ACCP".equals(sts);
					} else {
						/* 80 */ sts = getXmlItemContent(this.content, "TxSts", 0);
						/* 81 */ this.isCleared = (("ACSP".equals(reportedStatus)) || ("ACCP".equals(reportedStatus)));
					}
				}
			}
		}

		public String getXmlItemContent(String message, String tagName, int startPos) {
			/* 96 */ int end = message.indexOf("</" + tagName + ">", startPos);

			/* 98 */ if (end != -1) {
				/* 99 */ message = message.substring(0, end);
				/* 100 */ int start = message.indexOf("<" + tagName + ">", startPos);
				/* 101 */ if (start != -1) {
					/* 102 */ return message.substring(start + 1, end);
				}
			}

			/* 107 */ return null;
		}

		public long getProcessingDuration() {
			/* 111 */ return this.processingDuration;
		}

		public void setProcessingDuration(long processingTime) {
			/* 115 */ this.processingDuration = processingTime;
		}

		public boolean isCleared() {
			/* 119 */ return this.isCleared;
		}

		public int getErrorCode() {
			/* 126 */ return this.errorCode;
		}

		public String toString() {
			/* 131 */ return "RTPMessage [type=" + this.type + ", sequence=" + this.sequence + "]";
		}
	}

	@SuppressWarnings("unused")
	public static void main1(String[] args) {

		Root root = null;
		Record record = null;
		try {
			root = new Root();
			record = Record.class.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (record != null) {
			record.setAccount("");
			record.setAddress(null);
			record.setAddress("");

		}
		if (root != null && record != null)
			root.getRecord().add(record);
		try {

			JAXBContext jaxb = JAXBContext.newInstance(Root.class);
			Marshaller jaxbMarshaller = jaxb.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(root, sw);
			String xmlString = sw.toString();

			File f = new File("a.xml");

			OutputStream os = new FileOutputStream("a.xml", false);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			if (f.exists() && false)
				bw.append(xmlString);
			else
				bw.write(xmlString);

			bw.flush();
			bw.close();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "Root")
	public static final class Root implements Serializable {

		public Root() {

		}

		private final static long serialVersionUID = 1L;

		@XmlElement(name = "Record", required = true)
		protected List<Record> record;

		public List<Record> getRecord() {
			if (record == null) {
				record = new ArrayList<Record>();
			}
			return this.record;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "Record")
	public static final class Record implements Serializable {
		private final static long serialVersionUID = 1L;

		public Record() {

		}

		@XmlElement(name = "FIO", required = true)
		protected String fio = null;
		@XmlElement(name = "Sex", required = true)
		protected String sex = null;
		@XmlElement(name = "Title", required = true)
		protected String title = null;
		@XmlElement(name = "BirthDay", required = true)
		protected String birthDay = null;
		@XmlElement(name = "PasNom", required = true)
		protected String pasNom = null;
		@XmlElement(name = "ExtID", required = true)
		protected String extId = null;
		@XmlElement(name = "BRPart", required = true)
		protected String brPart = null;
		@XmlElement(name = "LATFIO", required = true)
		protected String latFio = null;
		@XmlElement(name = "BIRTHFIO", required = true)
		protected String birthFio = null;
		@XmlElement(name = "INN", required = true)
		protected String inn = null;
		@XmlElement(name = "Address", required = true)
		protected String address = null;
		@XmlElement(name = "EMAIL", required = true)
		protected String email = null;
		@XmlElement(name = "PHONE", required = true)
		protected String phone = null;
		@XmlElement(name = "FAX", required = true)
		protected String fax = null;
		@XmlElement(name = "CellPhone", required = true)
		protected String cellPhone;
		@XmlElement(name = "COUNTRYRES", required = true)
		protected String countryRes = null;
		@XmlElement(name = "RESADDRESS", required = true)
		protected String resAddress;
		@XmlElement(name = "ZIPREG")
		protected String zipReg = null;
		@XmlElement(name = "CNTRYREG")
		protected String cntryReg = null;
		@XmlElement(name = "REGIONREG", required = true)
		protected String regionReg = null;
		@XmlElement(name = "CITYREG", required = true)
		protected String cityReg;
		@XmlElement(name = "STREETREG")
		protected String streetReg = null;
		@XmlElement(name = "HOUSEREG", required = true)
		protected String houseReg = null;
		@XmlElement(name = "BUILDREG", required = true)
		protected String buildReg = null;
		@XmlElement(name = "FRAMEREG", required = true)
		protected String frameReg = null;
		@XmlElement(name = "FLATREG")
		protected String flatReg = null;
		@XmlElement(name = "COMPANY")
		protected String company = null;
		@XmlElement(name = "ACCOUNT", required = true)
		protected String account = null;

		public void setRegionReg(String regionReg) {
			this.regionReg = regionReg;
		}

		public void setFio(String fio) {
			this.fio = fio;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setBirthDay(String birthDay) {
			this.birthDay = birthDay;
		}

		public void setPasNom(String pasNom) {
			this.pasNom = pasNom;
		}

		public void setExtId(String extId) {
			this.extId = extId;
		}

		public void setBrPart(String brPart) {
			this.brPart = brPart;
		}

		public void setLatFio(String latFio) {
			this.latFio = latFio;
		}

		public void setBirthFio(String birthFio) {
			this.birthFio = birthFio;
		}

		public void setInn(String inn) {
			this.inn = inn;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public void setPhone(String phone) {
			if (phone == null) {
				this.cellPhone = null;
			}
			this.phone = phone;
		}

		public void setFax(String fax) {
			if (fax == "") {
				this.fax = "";
			}
			this.fax = fax;
		}

		public void setCellPhone(String cellPhone) {
			this.cellPhone = cellPhone;
		}

		public void setCountryRes(String countryRes) {
			this.countryRes = countryRes;
		}

		public void setResAddress(String resAddress) {
			this.resAddress = resAddress;
		}

		public void setZipReg(String zipReg) {
			this.zipReg = zipReg;
		}

		public void setCntryReg(String cntryReg) {
			if (cntryReg == "") {
				this.cntryReg = "";
			}
			this.cntryReg = cntryReg;
		}

		public void setStreetReg(String streetReg) {
			if (streetReg == "") {
				this.streetReg = "";
			}
			this.streetReg = streetReg;
		}

		public void setHouseReg(String houseReg) {
			if (houseReg == "") {
				this.houseReg = "";
			}
			this.houseReg = houseReg;
		}

		public void setBuildReg(String buildReg) {
			this.buildReg = buildReg;
		}

		public void setFrameReg(String frameReg) {
			this.frameReg = frameReg;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public void setAccount(String account) {
			this.account = account;
		}
	}
}