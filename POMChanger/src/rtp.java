import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.montran.pe.client.ClientConfig;
import com.montran.pe.client.ConnectionFactory;
import com.montran.pe.client.EngineConnection;
import com.montran.pe.client.RTPMessage;
import com.montran.pe.client.SignatureValidationException;
import com.montran.pe.client.XMLSignatureUtils;

public class rtp {

	public static String toDateStr(Date date, String format) {
		if (date == null)
			return "01-01-01";
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static String toStr(Object val) {

		if (null == val)
			return "";

		String ret = null;
		Class<?> type = val.getClass();
		if (type == Date.class) {
			Date d = (Date) val;
			ret = toDateStr(d, "yyyy-MM-dd");
		} else if (type == String.class) {
			ret = (String) val;
		} else {
			ret = String.valueOf(val);
		}

		return ret;
	}

	public static Date getNow() {
		return new Date();
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		for (int i = 0; i <= 100; i++) {
			getMessage(args[0]);
		}
	}

	public static void getMessage(String path) throws IOException {

		ClntFteRtpMessage rtpMessageReq = new ClntFteRtpMessage();

		String fileName = path + "/pacs008.xml";

		System.setProperty("com.montran.config.dir", path);

		System.out.println("path:" + path);

		String CONF_BASE_URL = getNesConfig(path + "/client-config.properties", "BASE_URL", "");
		String CONF_MY_DNS = getNesConfig(path + "/client-config.properties", "MY_DNS", "ips.cluster");
		String CONF_IPS_CLUSTER = getNesConfig(path + "/client-config.properties", CONF_MY_DNS, "");
		String CONF_RTP_BIC = getNesConfig(path + "/client-config.properties", "RTP_BIC", "");
		String CONF_PARTICIPANT_BIC = getNesConfig(path + "/client-config.properties", "PARTICIPANT_BIC", "");
		String CONF_SSL_KEY_ALIAS = getNesConfig(path + "/client-config.properties", "SSL_KEY_ALIAS", "");
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		List<String> lstStr = new ArrayList<>();
		lstStr = Arrays.asList(CONF_IPS_CLUSTER.split(";"));
		map.put(CONF_MY_DNS, lstStr);

		ClientConfig.BASE_URL = CONF_BASE_URL;
		ClientConfig.MY_DNS = map;
		ClientConfig.SIGN_OUTGOING = true;
		ClientConfig.RTP_BIC = CONF_RTP_BIC;
		ClientConfig.HTTP_SEND_PAYMENT_TIMEOUT = 60;
		ClientConfig.HTTP_CONNECTION_TIMEOUT = 2;
		ClientConfig.HTTP_REQUEST_TIMEOUT = 10;

		RTPMessage messageReq = null;
		RTPMessage messageRes = null;
		String destMsgId = null;

		System.out.println("CONF_BASE_URL:" + CONF_BASE_URL);
		System.out.println("CONF_MY_DNS:" + CONF_MY_DNS);
		System.out.println("CONF_IPS_CLUSTER:" + CONF_IPS_CLUSTER);
		System.out.println("CONF_RTP_BIC:" + CONF_RTP_BIC);
		System.out.println("CONF_PARTICIPANT_BIC:" + CONF_PARTICIPANT_BIC);
		System.out.println("CONF_SSL_KEY_ALIAS:" + CONF_SSL_KEY_ALIAS);

		ClientConfig.RTP_BIC = CONF_PARTICIPANT_BIC;
		EngineConnection conn = ConnectionFactory.getEngineConnection(CONF_PARTICIPANT_BIC, CONF_SSL_KEY_ALIAS);
		RTPMessage rtpMessage = null;

		rtpMessage = conn.getMessage();

		if (rtpMessage != null)
			System.out.println("rtpMessage not null!");
		if (rtpMessage != null && rtpMessage.getContent() != null) {
			System.out.println("rtpMessage.getType(): " + rtpMessage.getType());
			System.out.println("rtpMessage.getSequence(): " + rtpMessage.getSequence());
			System.out.println("rtpMessage.getContent(): " + rtpMessage.getContent());
			System.out.println("rtpMessage.getReportedStatus(): " + rtpMessage.getReportedStatus());
			System.out.println("rtpMessage.getErrorCode(): " + rtpMessage.getErrorCode());
			System.out.println("rtpMessage.getProcessingDuration(): " + rtpMessage.getProcessingDuration());
		}
		String tmp = "";
		try {
			int s = rtpMessage.getContent().indexOf("seq=\"");
			s += 5;
			System.out.println(s);
			tmp = rtpMessage.getContent().substring(s, s + 18);
			System.out.println(tmp);
			conn.confirmMessage(Long.parseLong(tmp));
		} catch (Exception ex) {
			System.out.println(tmp + " - " + ex.toString());
		}
		System.out.println("confirmMessage = " + rtpMessage.getSequence());
		try {
			conn.confirmMessage(201905080000001801L);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		try {
			conn.confirmMessage(rtpMessage.getSequence());
		} catch (Exception ex) {
			System.out.println(rtpMessage.getSequence() + " " + ex.toString());
		}
		System.out.println("finished");
	}

	@SuppressWarnings("unused")
	public static void main2(String[] args) throws IOException {

		// String path = args[0];
		String path = "D:\\montran\\Out";

		ClntFteRtpMessage rtpMessageReq = new ClntFteRtpMessage();

		String fileName = path + "/pacs008.xml";

		System.setProperty("com.montran.config.dir", path);

		System.out.println("path:" + path);

		String CONF_BASE_URL = getNesConfig(path + "/client-config.properties", "BASE_URL", "");
		String CONF_MY_DNS = getNesConfig(path + "/client-config.properties", "MY_DNS", "ips.cluster");
		String CONF_IPS_CLUSTER = getNesConfig(path + "/client-config.properties", CONF_MY_DNS, "");
		String CONF_RTP_BIC = getNesConfig(path + "/client-config.properties", "RTP_BIC", "");
		String CONF_PARTICIPANT_BIC = getNesConfig(path + "/client-config.properties", "PARTICIPANT_BIC", "");
		String CONF_SSL_KEY_ALIAS = getNesConfig(path + "/client-config.properties", "SSL_KEY_ALIAS", "");
		// String CONF_SSL_KEY_ALIAS = getNesConfig(path + "/client-config.properties",
		// "SSL_KEY_ALIAS", "");

		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		List<String> lstStr = new ArrayList<>();
		lstStr = Arrays.asList(CONF_IPS_CLUSTER.split(";"));
		map.put(CONF_MY_DNS, lstStr);

		ClientConfig.BASE_URL = CONF_BASE_URL;
		ClientConfig.MY_DNS = map;
		ClientConfig.SIGN_OUTGOING = true;
		ClientConfig.RTP_BIC = CONF_RTP_BIC;
		ClientConfig.HTTP_SEND_PAYMENT_TIMEOUT = 60;
		ClientConfig.HTTP_CONNECTION_TIMEOUT = 2;
		ClientConfig.HTTP_REQUEST_TIMEOUT = 10;

		RTPMessage messageReq = null;
		RTPMessage messageRes = null;
		String destMsgId = null;

		System.out.println("CONF_BASE_URL:" + CONF_BASE_URL);
		System.out.println("CONF_MY_DNS:" + CONF_MY_DNS);
		System.out.println("CONF_IPS_CLUSTER:" + CONF_IPS_CLUSTER);
		System.out.println("CONF_RTP_BIC:" + CONF_RTP_BIC);
		System.out.println("CONF_PARTICIPANT_BIC:" + CONF_PARTICIPANT_BIC);
		System.out.println("CONF_SSL_KEY_ALIAS:" + CONF_SSL_KEY_ALIAS);

		XMLSignatureUtils xmlSign = new XMLSignatureUtils();
		// 2. Rtp System-рүү илгээх
		EngineConnection conn = ConnectionFactory.getEngineConnection(CONF_PARTICIPANT_BIC, CONF_SSL_KEY_ALIAS);
		// 3. Дамжуулах мэссэжийн биеийг sign-жуулах
		rtpMessageReq = new ClntFteRtpMessage();
		rtpMessageReq.setIo("I");
		rtpMessageReq.setStatus("READY");
		rtpMessageReq.setSentCount(0L);
		rtpMessageReq.setCreDtTm(getNow());

		rtpMessageReq.setSenderBicCode("");
		rtpMessageReq.setReceiverBicCode("");
		rtpMessageReq.setMsgId("123456");
		rtpMessageReq.setContent(readFile(fileName));

		String signedMessage = "";
		System.out.println("message:" + rtpMessageReq.getContent());
		try {
			signedMessage = xmlSign.generateSignature(rtpMessageReq.getContent());
		} catch (SignatureException e1) {
			System.out.println("SignatureException:" + e1.toString());
			e1.printStackTrace();
		}
		System.out.println("signedMessage:" + signedMessage);

		rtpMessageReq.setContent(signedMessage);

		StringBuilder seq = new StringBuilder();
		seq.append(toDateStr(getNow(), "yyyyMMddHHmmssSSS"));

		rtpMessageReq.setSequence(seq.toString());
		rtpMessageReq.setType(destMsgId);

		Long seq1 = Long.parseLong(rtpMessageReq.getSequence());

		messageReq = new RTPMessage(rtpMessageReq.getMsgId(), signedMessage, seq1);

		// conn.closeConnection();
		RTPMessage str = null;
		// str = conn.getMessage();
		if (str != null) {
			System.out.println("getMessage sent ");
			System.out.println("getMessage msg :" + str);
			System.out.println("getMessage getSequence :" + str.getSequence() != null ? str.getSequence() : null);
			System.out.println(
					"getMessage getReportedStatus :" + str.getReportedStatus() != null ? str.getReportedStatus()
							: null);
			System.out.println("getMessage getErrorCode :" + str.getErrorCode() != null ? str.getErrorCode() : null);
			System.out.println("getMessage getContent :" + str.getContent() != null ? str.getContent() : null);
		}
		// 5. Монгол банкируу мэссэж илгээх

		System.out.println("msg:" + messageReq);
		System.out.println("Sending msg");

		try {
			messageRes = conn.sendNewMessage(messageReq);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.toString());
		}

		System.out.println("Sent");
		System.out.println("response msg :" + messageRes);
		System.out.println("response getSequence :" + messageRes.getSequence());
		System.out.println("response getReportedStatus :" + messageRes.getReportedStatus());
		System.out.println("response getErrorCode :" + messageRes.getErrorCode());
		System.out.println("response getContent :" + messageRes.getContent());

		ClntFteRtpMessage rtpMessageRes = new ClntFteRtpMessage();
		rtpMessageRes.setIo("RCV");
		rtpMessageRes.setStatus("RECEIVED_SUCCESS");
		rtpMessageRes.setContent(messageRes.getContent());
		rtpMessageRes.setType(messageRes.getType());
		rtpMessageRes.setSequence(toStr(messageRes.getSequence()));
		rtpMessageRes.setGrpSts(messageRes.getReportedStatus());
		rtpMessageRes.setErrorCode(toStr(messageRes.getErrorCode()));
		rtpMessageRes.setProcessingDuration(toStr(messageRes.getProcessingDuration()));
		rtpMessageRes.setSentCount(0L);

		rtpMessageRes.setOrigMsgId(rtpMessageReq.getMsgId());
		rtpMessageRes.setOrigType(rtpMessageReq.getType());
		// Long reqRecNo = proc.saveMessages(messageRes, RtpConst.RCV);

		if (messageRes != null) {

		} else {

		}
		// validate DS
		try {
			Document xmlDoc = readXmlToDocument(messageRes.getContent());
			if (xmlDoc == null) {

			}
			xmlSign.validateSignature(xmlDoc);
		} catch (SignatureValidationException sve) {

			System.out.println(sve.toString());
			sve.printStackTrace();
		} catch (Exception e) {

			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public static Document readXmlToDocument(String xmlBody) throws Exception {
		Document doc = null;
		try {
			doc = loadXMLFrom(xmlBody);
		} catch (Exception e) {
		}
		return doc;
	}

	public rtp() {
		// TODO Auto-generated constructor stub
	}

	private static String readFile(String pthFileName) {
		StringBuilder strData = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(pthFileName))) {

			String strLine;
			while ((strLine = br.readLine()) != null) {
				strData.append(strLine);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strData.toString();
	}

	public static Document loadXMLFrom(String xml) throws SAXException, IOException {

		xml = xml.replaceAll("&", "&amp;");
		return loadXMLFrom(new ByteArrayInputStream(xml.getBytes()));
	}

	public static String getNesConfig(String file, String pKey, String pDefault) {

		String path = file;
		if (!((new File(path)).exists()))
			return pDefault;

		InputStreamReader inputStream = null;
		try {
			inputStream = new InputStreamReader(new FileInputStream(path), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == inputStream)
			return pDefault;

		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e1) {
			e1.printStackTrace();
			return pDefault;
		}
		String ppp = p.getProperty(pKey, pDefault);
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ppp;
	}

	public static Document loadXMLFrom(InputStream is) throws SAXException, IOException {
		try {

			if (is == null)
				return null;

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
			}

			Document doc = builder.parse(is);
			is.close();
			is = null;
			return doc;
		} catch (Exception e) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e1) {

				}
			}
		}
	}

	public static class ClntFteRtpMessage implements Serializable {
		private static final long serialVersionUID = 1L;

		private Long recNo;

		private String io;

		private String sequence;

		private String senderBicCode;

		private String receiverBicCode;

		private String status;

		private String statusDesc;

		private String content;

		private Date creDtTm;

		private String msgId;

		private String type;

		private String origMsgId;

		private String origType;

		private String grpSts;

		private String errorCode;

		private String processingDuration;

		private Long sentCount;

		private Date createdDatetime;

		public ClntFteRtpMessage() {
		}

		public Long getRecNo() {
			return this.recNo;
		}

		public void setRecNo(Long recNo) {
			this.recNo = recNo;
		}

		public String getIo() {
			return this.io;
		}

		public void setIo(String io) {
			this.io = io;
		}

		public String getSequence() {
			return this.sequence;
		}

		public void setSequence(String sequence) {
			this.sequence = sequence;
		}

		public String getSenderBicCode() {
			return this.senderBicCode;
		}

		public void setSenderBicCode(String senderBicCode) {
			this.senderBicCode = senderBicCode;
		}

		public String getReceiverBicCode() {
			return this.receiverBicCode;
		}

		public void setReceiverBicCode(String receiverBicCode) {
			this.receiverBicCode = receiverBicCode;
		}

		public String getStatus() {
			return this.status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatusDesc() {
			return statusDesc;
		}

		public void setStatusDesc(String statusDesc) {
			this.statusDesc = statusDesc;
		}

		public String getContent() {
			return this.content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Date getCreDtTm() {
			return this.creDtTm;
		}

		public void setCreDtTm(Date creDtTm) {
			this.creDtTm = creDtTm;
		}

		public String getMsgId() {
			return this.msgId;
		}

		public void setMsgId(String msgId) {
			this.msgId = msgId;
		}

		public String getType() {
			return this.type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getOrigMsgId() {
			return this.origMsgId;
		}

		public void setOrigMsgId(String origMsgId) {
			this.origMsgId = origMsgId;
		}

		public String getOrigType() {
			return this.origType;
		}

		public void setOrigType(String origType) {
			this.origType = origType;
		}

		public String getGrpSts() {
			return this.grpSts;
		}

		public void setGrpSts(String grpSts) {
			this.grpSts = grpSts;
		}

		public String getErrorCode() {
			return this.errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getProcessingDuration() {
			return this.processingDuration;
		}

		public void setProcessingDuration(String processingDuration) {
			this.processingDuration = processingDuration;
		}

		public Long getSentCount() {
			return this.sentCount;
		}

		public void setSentCount(Long sentCount) {
			this.sentCount = sentCount;
		}

		public Date getCreatedDatetime() {
			return this.createdDatetime;
		}

		public void setCreatedDatetime(Date createdDatetime) {
			this.createdDatetime = createdDatetime;
		}

	}

}