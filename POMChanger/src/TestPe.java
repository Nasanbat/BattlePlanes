import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestPe {

	public TestPe() {
	}

	public static class MyThread extends Thread {

		int port;

		public MyThread(int port) {
			this.port = port;
		}

		public void run() {
			Capitalizer.listBank = new ArrayList<>();
			Capitalizer.bank t = new Capitalizer.bank();
			t.setBankCode("CHKHMNUB");
			Capitalizer.listBank.add(t);
			Capitalizer.bank t1 = new Capitalizer.bank();
			t1.setBankCode("TDBMMNUB");
			Capitalizer.listBank.add(t1);

			int count = 0;

			try (ServerSocket listener = new ServerSocket(port)) {
				System.out.println("The BOM server is running " + port + " ...");
				ExecutorService pool = Executors.newFixedThreadPool(200);
				while (true) {
					Socket socket = listener.accept();
					pool.execute(new Capitalizer(socket));
					System.out.println(" >> Accepted: " + (++count) + " [" + new Date() + "]");
					try {
						Thread.sleep(10);
					} catch (Exception e) {
					}

				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// conn();

		int port = args == null ? 8443 : args.length > 0 ? Integer.parseInt(args[0]) : 8443;

		MyThread myThread = new MyThread(port);
		myThread.start();

		while (true) {
			Scanner sc = new Scanner(System.in);
			String name = sc.nextLine();
			if ("exit".equals(name.toLowerCase())) {
				System.exit(0);
			} else if ("print".equals(name.toLowerCase())) {

			}
		}
	}

	private static class Capitalizer implements Runnable {
		private Socket socket;

		class CResult {
			public CResult() {
				this.retType = 1;
			}

			public CResult(int retType) {
				this.retType = retType;
			}

			public int retType;
			public String retDesc;
			public String status;
			public String errInfo;
			public String infoStr;
			public Object retObj;

		}

		static class bank {

			String bankCode;

			static Long key = 0L;

			static Long recievePacs008count = 0L;

			static Long returnPacs002count = 0L;

			static Long recievePacs002count = 0L;

			static Long replayPacs002count = 0L;

			static Long sendPacs008count = 0L;

			public Long getKey() {
				return key++;
			}

			static Long keyFifo = 0L;

			public Long getKeyFifo() {
				return keyFifo++;
			}

			ConcurrentHashMap<String, String> msgList;

			ConcurrentHashMap<String, String> msgReplayList;

			public String getBankCode() {
				return bankCode;
			}

			public void setBankCode(String bankCode) {
				this.bankCode = bankCode;
			}

			public ConcurrentHashMap<String, String> getMsgList() {
				return msgList;
			}

			public void setMsgList(ConcurrentHashMap<String, String> msgList) {
				this.msgList = msgList;
			}

			public ConcurrentHashMap<String, String> getMsgReplayList() {
				return msgReplayList;
			}

			public void setMsgReplayList(ConcurrentHashMap<String, String> msgReplayList) {
				this.msgReplayList = msgReplayList;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				bank other = (bank) obj;
				if (bankCode == null) {
					if (other.bankCode != null)
						return false;
				} else if (!bankCode.equals(other.bankCode))
					return false;
				return true;
			}
		}

		public static List<bank> listBank;

		Capitalizer(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			CResult res = new CResult();
			StringBuilder logtrace = new StringBuilder();
			String reqType = "";
			String bankCode = "";
			String msg = "";
			String strmsg = "";
			int contentLength = 0;
			final String contentHeader = "Content-Length: ";
			final String MONTRANRTP = "X-MONTRAN-RTP-Channel: ";
			logtrace.append((new Date()) + "Connected: " + socket);
			try {
				BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line;

				// logtrace.append((new Date()) + "Connected: " + socket);
				line = in2.readLine();
				StringBuilder raw = new StringBuilder();
				raw.append("" + line);
				boolean isPost = line != null ? line.startsWith("POST") : false;
				// logtrace.append("isPost: " + isPost);
				boolean MessageAck = line != null ? line.startsWith("POST /MessageAck") : false;
				// logtrace.append("MessageAck: " + MessageAck);

				reqType = isPost ? "POST" : "GET";
				// logtrace.append("reqType: " + reqType);
				while (!(line = in2.readLine()).equals("")) {
					// logtrace.append('\n' + line);
					raw.append('\n' + line);

					if (isPost) {
						if (line.startsWith(contentHeader)) {
							contentLength = Integer.parseInt(line.substring(contentHeader.length()));
							// logtrace.append("\n contentLength" + contentLength);
						}
					}
					if (line.startsWith(contentHeader)) {
						contentLength = Integer.parseInt(line.substring(contentHeader.length()));
						// logtrace.append("\n contentLength" + contentLength);
					}
					if (line != null) {
						if (line.startsWith(MONTRANRTP)) {
							bankCode = line.substring(MONTRANRTP.length());
							// logtrace.append("\n bankCode" + bankCode);
						}

					}
				}
				// logtrace.append("HeaderDone");
				StringBuilder body = new StringBuilder();
				if (isPost) {
					int c = 0;
					for (int i = 0; i < contentLength; i++) {
						c = in2.read();
						body.append((char) c);
					}
				}
				// logtrace.append("body: " + body);
				raw.append(body.toString());
				String messageType = "pacs.002";
				if (reqType.equals("POST")) {
					// logtrace.append(" POST raw: " + raw.toString());
				}
				if (reqType.equals("POST") && !MessageAck && contentLength > 0) {

					if (body.toString().indexOf("pacs.002.001.07") > 0) {
						// System.out.println((new Date()) + "" + socket.hashCode() + ",replaypacs2 : "
						// + body.toString());
						logtrace.append((new Date()) + " pacs002");
						System.out.println((new Date()) + "" + socket + ", post pacs002");
						res = replayMsgPacs002(bankCode, body.toString(), true);
						if (res.retType == 0) {
							// logtrace.append(res.infoStr);
							System.out.println((new Date()) + "" + socket + ", post result OK " + res.retDesc);
						} else {
							System.out.println(
									(new Date()) + "" + socket + ", post result " + res.retType + " " + res.retDesc);
							logtrace.append("ERR=>" + res.infoStr);
						}
						strmsg = res.retDesc;
						// System.out.println((new Date()) + "" + socket + ", replayPacs002 " + strmsg);

						// logtrace.append((new Date()) + "" + socket + ", replayPacs2: " + strmsg);
						messageType = "pacs.002";
					} else {
						logtrace.append((new Date()) + "pacs008");

						// Илгээсэн гүйлгээ хадгална

						CResult res1 = setMsgPacs008(bankCode, body.toString());
						logtrace.append((new Date()) + "setMsgPacs008" + bankCode + " => pacs.008 = " + res1.infoStr);
						// Хүлээн авсан амжилттай хариу өгнө
						res = replayMsgPacs002(bankCode, res1.retDesc, false);
						if (res.retType == 0) {
							// logtrace.append(res.infoStr);
						} else {
							logtrace.append("ERR=>" + res.infoStr);
						}
						strmsg = res.retDesc;
						// logtrace.append((new Date()) + "" + socket + ", replayPacs002: " + strmsg);
						messageType = "pacs.002";
					}
				} else if (reqType.equals("POST") && MessageAck) {
					strmsg = "";
					// logtrace.append((new Date()) + " " + socket.hashCode() + ", : " + strmsg);
					messageType = "pacs.002";
					logtrace.append("\n POST MessageAck pacs.002 strmsg: " + strmsg);
				} else if (reqType.equals("GET")) {
					logtrace.append("GET");
					Random rand = new Random();
					int n = rand.nextInt(1000);
					if (n == 10000L) {
						messageType = "rcon.001";
						logtrace.append(messageType);
						strmsg = getMsgRcon001(bankCode);

						logtrace.append((new Date()) + " " + socket + " bankCode = " + bankCode + ", raw: "
								+ raw.toString() + ", strmsg: " + strmsg);

						logtrace.append("\n strmsg: " + strmsg);
					} else {
						messageType = "pacs.008";
						// logtrace.append(messageType);
						strmsg = getMsgPacs008(bankCode);

						if (strmsg != null && !strmsg.equals("")) {
							// logtrace.append((new Date()) + " strmsg: " + strmsg);
						}
					}
				}
				res.status = (res.status == null || res.status.equals("") ? "ACSP" : res.status);
				// logtrace.append("\n\rWriter out start ");
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.print("HTTP/1.1 200 \r\n"); // Version & status code
				out.print("Content-Type: text/plain\r\n"); // The type of data
				out.print("Connection: Keep-Alive\r\n"); // The type of data
				out.print(contentHeader + (strmsg == null ? 0 : strmsg.length()) + "\r\n"); // The type of data
				out.print("X-MONTRAN-RTP-ReqSts: " + res.status + "\r\n");
				out.print("X-MONTRAN-RTP-ProcessingTime: 123\r\n");
				out.print("X-MONTRAN-RTP-MessageSeq: " + (new Date()).getTime() + "\r\n"); // The type of data
				out.print("X-MONTRAN-RTP-MessageType: " + messageType + "\r\n\r\n"); // The type of data

				// logtrace.append("\n\rWriter out header done");
				if (strmsg != null && !strmsg.equals("")) {
					out.println(strmsg);
					// logtrace.append("\n\rWriter out body done");
				} else {
					out.println("");
					// logtrace.append("\n\rWriter out null body done");
				}

				// logtrace.append("\n Done ");
				in2.close();
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println((new Date()) + "Error:" + logtrace.toString() + e.toString());
				e.printStackTrace();
			} finally {
				System.out.println((new Date()) + "info:" + logtrace.toString());
				try {
					socket.close();
				} catch (IOException e) {
				}
				// System.out.println("Closed: " + socket);
			}
		}

		public CResult replayMsgPacs002(String bankCode, String strmsg, boolean replay) {

			CResult res = new CResult();
			// <BizMsgIdr>201905131931330169649</BizMsgIdr>
			int bi = strmsg.indexOf("<BizMsgIdr>") + 11;
			int ei = strmsg.indexOf("</BizMsgIdr>");
			String orgnlMsgId = strmsg.substring(bi, ei);
			bi = strmsg.indexOf("<InstrId>") + 9;
			ei = strmsg.indexOf("</InstrId>");

			// <InstrId>TX3000201905130000156310</InstrId>
			String orgnlInstrId = "";
			orgnlInstrId = (bi < ei) ? strmsg.substring(bi, ei) : "";
			if (orgnlInstrId.equals("")) {
				bi = strmsg.indexOf("<OrgnlInstrId>") + 14;
				ei = strmsg.indexOf("</OrgnlInstrId>");

				orgnlInstrId = (bi < ei) ? strmsg.substring(bi, ei) : "";
			}
			String repMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<hdr:Message\r\n"
					+ "	xmlns:hdr=\"urn:montran:message.01\">\r\n	<hdr:AppHdr\r\n"
					+ "		xmlns=\"urn:iso:std:iso:20022:tech:xsd:head.001.001.01\">\r\n		<Fr>\r\n"
					+ "			<FIId>\r\n				<FinInstnId>\r\n"
					+ "					<BICFI>ZYAKROBU</BICFI>\r\n				</FinInstnId>\r\n"
					+ "			</FIId>\r\n		</Fr>\r\n		<To>\r\n			<FIId>\r\n"
					+ "				<FinInstnId>\r\n					<BICFI>[FROMBANKCODE]</BICFI>\r\n"
					+ "				</FinInstnId>\r\n			</FIId>\r\n		</To>\r\n"
					+ "		<BizMsgIdr>[BizMsgIdr]</BizMsgIdr>\r\n		<MsgDefIdr>pacs.002.001.07</MsgDefIdr>\r\n"
					+ "		<CreDt>2019-05-13T11:31:51Z</CreDt>\r\n		<Sgntr>\r\n"
					+ "			<Signature\r\n				xmlns=\"http://www.w3.org/2000/09/xmldsig#\">\r\n"
					+ "				<SignedInfo>\r\n"
					+ "					<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\r\n"
					+ "					<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/>\r\n"
					+ "					<Reference URI=\"\">\r\n						<Transforms>\r\n"
					+ "							<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\r\n"
					+ "							<Transform Algorithm=\"http://www.w3.org/2006/12/xml-c14n11\"/>\r\n"
					+ "						</Transforms>\r\n"
					+ "						<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>\r\n"
					+ "						<DigestValue>wR3Ws4tf2UaaMDMTyJXkAgUqlHuPuVumfh8v45XqvqM=</DigestValue>\r\n"
					+ "					</Reference>\r\n				</SignedInfo>\r\n"
					+ "				<SignatureValue>igiyDKv9Jizw64MQCY7l4rfpoPgfyas7oXrbG0Y7btVK67z/FXasp0DdymN/r2752EXP+qXtRtRb\r\n"
					+ "u4we2HiRWRSTURFWqc6E8PcXFWXiTI8Isp+XyfAtqitcDYIet5pusEFW3a3Ffv6tX1uAWlT8Ef8J\r\n"
					+ "sWfpw5qU0XUSnzHtixdzul1JiVYLZm0CTREcZ4alip48gHMeXvW86yN/2X9lgFt24WrBgYzbc7SL\r\n"
					+ "Nb15tsq6T6BWEwarkg45TMqKLyW+21JL86YbszXFkGEuxGQ8oW6BqVcp28JvjycMdlb9BaxBl+ur\r\n"
					+ "1k4Qyg46IknYkLtWNbkKtFY7bqsYe3WIFUDl+A==</SignatureValue>\r\n				<KeyInfo>\r\n"
					+ "					<X509Data>\r\n"
					+ "						<X509SubjectName>CN=server1</X509SubjectName>\r\n"
					+ "						<X509IssuerSerial>\r\n"
					+ "							<X509IssuerName>CN=IPSBOMCA</X509IssuerName>\r\n"
					+ "							<X509SerialNumber>586602844064344833688776566906968724</X509SerialNumber>\r\n"
					+ "						</X509IssuerSerial>\r\n					</X509Data>\r\n"
					+ "				</KeyInfo>\r\n			</Signature>\r\n		</Sgntr>\r\n"
					+ "	</hdr:AppHdr>\r\n	<hdr:FIToFIPmtStsRpt\r\n"
					+ "		xmlns=\"urn:iso:std:iso:20022:tech:xsd:pacs.002.001.07\">\r\n		<GrpHdr>\r\n"
					+ "			<MsgId>[BizMsgIdr]</MsgId>\r\n"
					+ "			<CreDtTm>2019-05-13T19:31:51+08:00</CreDtTm>\r\n			<InstdAgt>\r\n"
					+ "				<FinInstnId>\r\n					<BICFI>[FROMBANKCODE]</BICFI>\r\n"
					+ "				</FinInstnId>\r\n			</InstdAgt>\r\n		</GrpHdr>\r\n"
					+ "		<OrgnlGrpInfAndSts>\r\n			<OrgnlMsgId>[OrgnlMsgId]</OrgnlMsgId>\r\n"
					+ "			<OrgnlMsgNmId>pacs.008.001.07</OrgnlMsgNmId>\r\n"
					+ "			<GrpSts>ACCP</GrpSts>\r\n		</OrgnlGrpInfAndSts>\r\n"
					+ "		<TxInfAndSts>\r\n			<StsId>[BizMsgIdr]</StsId>\r\n"
					+ "			<OrgnlInstrId>[OrgnlInstrId]</OrgnlInstrId>\r\n"
					+ "			<OrgnlEndToEndId>[OrgnlInstrId]</OrgnlEndToEndId>\r\n"
					+ "			<OrgnlTxId>[OrgnlInstrId]</OrgnlTxId>\r\n"
					+ "			<StsRsnInf><Rsn><Cd>[REASON]</Cd></Rsn></StsRsnInf>"
					+ "			<AccptncDtTm>2019-05-13T19:31:50+08:00</AccptncDtTm>\r\n"
					+ "			<OrgnlTxRef>\r\n				<PmtTpInf>\r\n					<SvcLvl>\r\n"
					+ "						<Prtry>SENT</Prtry>\r\n					</SvcLvl>\r\n"
					+ "					<LclInstrm>\r\n						<Cd>INST</Cd>\r\n"
					+ "					</LclInstrm>\r\n				</PmtTpInf>\r\n"
					+ "				<DbtrAgt>\r\n					<FinInstnId>\r\n"
					+ "						<BICFI>[FROMBANKCODE]</BICFI>\r\n					</FinInstnId>\r\n"
					+ "				</DbtrAgt>\r\n			</OrgnlTxRef>\r\n		</TxInfAndSts>\r\n"
					+ "	</hdr:FIToFIPmtStsRpt>\r\n</hdr:Message>";

			repMsg = repMsg.replace("[FROMBANKCODE]", bankCode);
			repMsg = repMsg.replace("[BizMsgIdr]", "PS20190513001301" + Math.random());
			repMsg = repMsg.replace("[OrgnlMsgId]", orgnlMsgId);
			repMsg = repMsg.replace("[OrgnlInstrId]", orgnlInstrId);
			String reason = "MS02";
			if (replay) {
				// Хариу амжилттай гэж буцаана
				String repMsg1 = repMsg;
				// Нөгөө банкны хариуг төлөвөөс шалгана.
				if (strmsg.indexOf("<GrpSts>RJCT</GrpSts>") > 0) {
					bi = strmsg.indexOf("<Rsn>") + 5;
					ei = strmsg.indexOf("</Rsn>");
					if (bi > 0 && ei > 0 && bi < ei) {
						String tmp = strmsg.substring(bi, ei);
						repMsg = repMsg.replace("<Cd>[REASON]</Cd>", tmp);
					}
					repMsg = repMsg.replace("ACCP", "RJCT");
				}
				repMsg = repMsg.replace("[REASON]", reason);
				System.out.println((new Date()) + "" + socket + ", repMsg1 => " + repMsg);
				res = setMsgPacs002(bankCode, repMsg);
				if (res.retType != 0) {
					// Алдаатай хариу буцаана
					repMsg1 = repMsg1.replace("ACCP", "RJCT");
					repMsg1 = repMsg1.replace("[REASON]", reason);
					res.retDesc = repMsg1;
				} else {
					repMsg1 = repMsg1.replace("[REASON]", reason);
					res.retDesc = repMsg1;
				}
				System.out.println((new Date()) + "" + socket + ", repMsg2 => " + repMsg1);
			} else {
				res.status = "ACSP";
				if (strmsg.indexOf("<GrpSts>RJCT</GrpSts>") > 0) {
					repMsg = repMsg.replace("ACCP", "RJCT");
					res.status = "RJCT";
				}
				res.retType = 0;
				repMsg = repMsg.replace("[REASON]", reason);
				res.retDesc = repMsg;
			}
			return res;
		}

		public CResult setMsgPacs002(String bankCode, String msg) {
			CResult res = new CResult();
			StringBuilder str = new StringBuilder();
			str.append("setMsgPacs002 => " + bankCode);
			if (listBank != null) {
				for (bank b : listBank) {
					if (b.getBankCode().equals(bankCode)) {
						if (msg != null) {
							str.append(" msg= " + msg);
							int bi = msg.indexOf("<OrgnlTxId>") + 11;
							int ei = msg.indexOf("</OrgnlTxId>");
							str.append(" bi= " + bi);
							str.append(" ei= " + ei);
							String OrgnlInstrId = (bi < ei) ? msg.substring(bi, ei) : "";
							str.append("OrgnlInstrId:" + OrgnlInstrId);
							b.recievePacs002count++;
							if (b.getMsgReplayList() != null) {
								b.getMsgReplayList().put(OrgnlInstrId, msg);

								str.append(" Done1 ");
								break;
							} else {
								ConcurrentHashMap<String, String> msgList = new ConcurrentHashMap<>(2000);
								msgList.put(OrgnlInstrId, msg);
								b.setMsgReplayList(msgList);

								str.append(" Done2 ");
								break;
							}
						} else {
							str.append(" msg == null ");
							break;
						}
					}
				}
			}
			res.retType = 0;
			res.infoStr = str.toString();
			return res;
		}

		public String getMsgPacs008(String bankCode) {
			StringBuilder str = new StringBuilder();
			String msg = "";
			if (listBank != null) {
				for (bank b : listBank) {
					if (b.getBankCode().equals(bankCode)) {
						str.append(socket + " bankCode: " + bankCode + " MsgList.size = "
								+ (b.getMsgList() != null ? b.getMsgList().size() : " null "));
						if (b.getMsgList() != null && b.getMsgList().size() > 0) {
							if (b.getMsgList().keys().hasMoreElements()) {
								String instrId = b.getMsgList().keys().nextElement();
								b.sendPacs008count++;
								msg = b.getMsgList().remove(instrId);
								str.append(" MsgList().remove(" + instrId + ") "
										+ (b.getMsgList() != null ? b.getMsgList().size() : " null "));
							}
						}
					}
				}
			}
			System.out.println((new Date()) + str.toString());
			return msg;
		}

		public String getMsgRcon001(String bankCode) {
			String msg = "<recon xmlns=\"urn:montran:rcon.001\">\r\n"
					+ "	<settlement seq=\"201905070000000601\" ccy=\"MNT\" msgId=\"CT0201905070000049401\" instgAgtBIC=\""
					+ bankCode + "\" processTime=\"2019-05-07T21:39:43+08:00\"/>\r\n"
					+ "	<totalSent amount=\"0.00\" count=\"0\"/>\r\n"
					+ "	<totalReceived amount=\"0.00\" count=\"0\"/>\r\n"
					+ "	<total amount=\"0.00\" count=\"0\"/>\r\n" + "</recon>";

			return msg;
		}

		private String getBankCode(String msg) {
			int bi = msg.indexOf("<CdtrAgt>") + 9;
			int ei = msg.indexOf("</CdtrAgt>");
			String tmp = "";
			if (msg.length() > bi && msg.length() > ei)
				tmp = msg.substring(bi, ei);

			tmp = tmp.replace(" ", "").replace("\r", "").replace("\n", "");
			bi = tmp.indexOf("<FinInstnId><BICFI>") + "<FinInstnId><BICFI>".length();
			ei = tmp.indexOf("</BICFI></FinInstnId>");

			String bankCode = (tmp.length() > bi && tmp.length() > ei) ? tmp.substring(bi, ei) : "";
			return bankCode;
		}

		public CResult setMsgPacs008(String fromBankCode, String msg) {
			CResult res = new CResult();
			StringBuilder str = new StringBuilder();
			int bi = msg.indexOf("<InstrId>") + 9;
			int ei = msg.indexOf("</InstrId>");
			String InstrId = "";
			InstrId = (msg.length() > bi && msg.length() > ei) ? msg.substring(bi, ei) : "";
			if (InstrId.length() <= 0)
				str.append("\r\n " + socket + " BankCode: " + fromBankCode + " InstrId: Is Null");
			String bankCode = getBankCode(msg);
			str.append("\r\n bankCode:" + bankCode);
			msg = msg.replaceFirst(fromBankCode, bankCode);
			msg = msg.replaceFirst(fromBankCode, bankCode);

			msg = msg.replace("<Fr>", "<TMPFR>");
			msg = msg.replace("</Fr>", "</TMPFR>");
			msg = msg.replace("<To>", "<Fr>");
			msg = msg.replace("</To>", "</Fr>");
			msg = msg.replace("<TMPFR>", "<To>");
			msg = msg.replace("</TMPFR>", "</To>");

			msg = msg.replace("InstgAgt", "InstdAgt");

			// msg = msg.replace("CdtrAgt", "TMPAGT");
			// msg = msg.replace("<Cdtr>", "<TMP>");
			// msg = msg.replace("</Cdtr>", "</TMP>");
			// msg = msg.replace("CdtrAcct", "TMPACCT");
			// msg = msg.replace("UltmtCdtr", "TMPULTMT");

			// msg = msg.replace("DbtrAgt", "CdtrAgt");
			// msg = msg.replace("<Dbtr>", "<Cdtr>");
			// msg = msg.replace("</Dbtr>", "</Cdtr>");
			// msg = msg.replace("DbtrAcct", "CdtrAcct");
			// msg = msg.replace("UltmtDbtr", "UltmtCdtr");

			// msg = msg.replace("TMPAGT", "DbtrAgt");
			// msg = msg.replace("<TMP>", "<Dbtr>");
			// msg = msg.replace("</TMP>", "</Dbtr>");
			// msg = msg.replace("TMPACCT", "DbtrAcct");
			// msg = msg.replace("TMPULTMT", "UltmtDbtr");

			if (listBank == null) {
				listBank = new ArrayList<>();
				bank b = new bank();
				b.setBankCode(bankCode);
				ConcurrentHashMap<String, String> msgList = new ConcurrentHashMap<>(2000);

				b.recievePacs008count++;
				msgList.put(InstrId, msg);
				b.setMsgList(msgList);
				listBank.add(b);
			} else
				for (bank b : listBank) {
					if (b.getBankCode().equals(bankCode)) {
						if (b.getMsgList() != null) {
							str.append("\r\n MsgList() != null =>BankCode: " + fromBankCode + " InstrId: " + InstrId);
							b.recievePacs008count++;
							b.getMsgList().put(InstrId, msg);
						} else {
							str.append("\r\n MsgList() == null =>BankCode: " + fromBankCode + " InstrId: " + InstrId);
							ConcurrentHashMap<String, String> msgList = new ConcurrentHashMap<>(2000);
							b.recievePacs008count++;
							msgList.put(InstrId, msg);
							b.setMsgList(msgList);
						}
					}
				}
			str.append((new Date()) + " checkGetTxn " + bankCode + " - " + InstrId);
			str.append(checkGetTxn(bankCode, InstrId));
			System.out.println(" checkGetTxn Done " + InstrId);
			res = checkTxnDone(bankCode, InstrId);
			str.append(res.infoStr);
			System.out.println(" checkTxnDone Done " + InstrId);
			res.infoStr = str.toString();
			return res;
		}

		private String checkGetTxn(String bankCode, String instrId) {
			StringBuilder str = new StringBuilder();
			str.append("checkGetTxn[ bankCode = " + (new Date()) + " - " + bankCode + " InstrId=" + instrId);
			int i = 0;
			for (bank b : listBank) {
				if (b.getBankCode().equals(bankCode)) {
					while (true) {
						if (b.getMsgList() == null) {
							str.append("]");
							return str.toString();
						} else {
							str.append("\r\n" + (new Date()) + " => " + i + " MsgList.size = " + b.getMsgList().size());
							if (b.getMsgList().size() > 0 && b.getMsgList().containsKey(instrId))
								try {
									i++;

									System.out.println(" checkGetTxn sleep " + i);
									Thread.sleep(1000);
								} catch (InterruptedException e) {
								}
							else {
								str.append("]");
								return str.toString();
							}
						}
					}
				}
			}
			str.append("]");
			return str.toString();
		}

		private CResult checkTxnDone(String bankCode, String instrId) {
			CResult res = new CResult();
			StringBuilder str = new StringBuilder();
			int i = 0;
			str.append("checkTxnDone[ bankCode = " + (new Date()) + " - " + bankCode + " InstrId=" + instrId);
			for (bank b : listBank) {
				if (b.getBankCode().equals(bankCode)) {
					while (true) {
						if (b.getMsgReplayList() == null) {
							try {
								i++;
								Thread.sleep(1000);
								str.append("\r\n null checkTxnDone sleep " + i);
								System.out.println("InstrId=" + instrId + " checkTxnDone sleep " + i
										+ " MsgReplayList.size = "
										+ (b.getMsgReplayList() != null ? b.getMsgReplayList().size() : "null"));
							} catch (InterruptedException e) {
							}
						} else {
							str.append("\r\n" + (new Date()) + " => " + i + " MsgReplayList.size = "
									+ (b.getMsgReplayList() != null ? b.getMsgReplayList().size() : "null"));
							if (b.getMsgReplayList() != null && b.getMsgReplayList().size() > 0
									&& b.getMsgReplayList().containsKey(instrId)) {
								res.retType = 0;
								res.retDesc = b.getMsgReplayList().get(instrId);

								b.getMsgReplayList().remove(instrId);
								str.append(" removed ]");

								b.returnPacs002count++;
								res.infoStr = str.toString();
								return res;
							} else
								try {
									i++;
									Thread.sleep(1000);
									str.append("\r\n InstrId" + instrId + "checkTxnDone sleep " + i);
									System.out.println("InstrId=" + instrId + "checkTxnDone sleep " + i
											+ " MsgReplayList.size = "
											+ (b.getMsgReplayList() != null ? b.getMsgReplayList().size() : "null"));
								} catch (InterruptedException e) {
								}
						}
					}
				}
			}
			str.append(" not found bank ]");
			res.infoStr = str.toString();
			return res;
		}
	}
}

/*
 * 
 * 
 * // TODO Auto-generated constructor stub Map<String, Queue<Capitalizer.bank>>
 * mp = new HashMap<>(); Queue<Capitalizer.bank> data = new LinkedList<>();
 * Capitalizer.bank b = new Capitalizer.bank(); data.add(b); mp.put("TDB",
 * data);
 * 
 * mp.get("TDB").add(b);
 * 
 * Capitalizer.bankmp.get("TDB").poll();
 */
