import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class POMChanger {

	static String plungin = "		<plugins>\r\n			<plugin>\r\n				<groupId>pl.project13.maven</groupId>\r\n"
			+ "				<artifactId>git-commit-id-plugin</artifactId>\r\n"
			+ "				<version>2.2.2</version>\r\n				<executions>\r\n"
			+ "					<execution>\r\n						<phase>validate</phase>\r\n"
			+ "						<goals>\r\n							<goal>revision</goal>\r\n"
			+ "						</goals>\r\n					</execution>\r\n"
			+ "				</executions>\r\n				<configuration>\r\n"
			+ "					<dateFormat>yyyyMMddHHmmss</dateFormat>\r\n				</configuration>\r\n"
			+ "			</plugin>\r\n			<plugin>\r\n"
			+ "				<artifactId>maven-compiler-plugin</artifactId>\r\n"
			+ "				<version>3.1</version>\r\n				<configuration>\r\n"
			+ "					<source>1.8</source>\r\n					<target>1.8</target>\r\n"
			+ "				</configuration>\r\n			</plugin>\r\n			<plugin>\r\n"
			+ "				<artifactId>maven-ejb-plugin</artifactId>\r\n				<version>2.3</version>\r\n"
			+ "				<configuration>\r\n					<ejbVersion>3.1</ejbVersion>\r\n"
			+ "				</configuration>\r\n			</plugin>\r\n		</plugins>";
	static String resource = "		<resources>\r\n			<resource>\r\n"
			+ "				<directory>ejbModule</directory>\r\n				<excludes>\r\n"
			+ "					<exclude>**/*.java</exclude>\r\n				</excludes>\r\n"
			+ "			</resource>\r\n			<resource>\r\n"
			+ "				<directory>ejbModule</directory>\r\n				<filtering>true</filtering>\r\n"
			+ "				<includes>\r\n					<include>**/git.properties</include>\r\n"
			+ "				</includes>\r\n			</resource>\r\n			<resource>\r\n"
			+ "				<directory>appClientModule</directory>\r\n				<filtering>true</filtering>\r\n"
			+ "				<includes>\r\n					<include>**/git.properties</include>\r\n"
			+ "				</includes>\r\n			</resource>\r\n			<resource>\r\n"
			+ "				<directory>ejbModule</directory>\r\n				<filtering>true</filtering>\r\n"
			+ "				<includes>\r\n					<include>**/persistence.xml</include>\r\n"
			+ "				</includes>\r\n			</resource>\r\n		</resources>";
	static String profiles = "	<profiles>\r\n		<profile>\r\n			<id>weblogic</id>\r\n"
			+ "			<properties>\r\n				<datasource.prefix>jdbc</datasource.prefix>\r\n"
			+ "			</properties>\r\n			<distributionManagement>\r\n"
			+ "				<repository>\r\n					<id>wl</id>\r\n"
			+ "					<name>Weblogic Releases</name>\r\n"
			+ "					<url>${repo.url.weblogic}</url>\r\n				</repository>\r\n"
			+ "				<snapshotRepository>\r\n					<id>dev</id>\r\n"
			+ "					<name>GCM Snapshots</name>\r\n"
			+ "					<url>${repo.url.snapshots}</url>\r\n				</snapshotRepository>\r\n"
			+ "			</distributionManagement>\r\n		</profile>\r\n		<profile>\r\n"
			+ "			<id>default</id>\r\n			<properties>\r\n"
			+ "				<datasource.prefix>java:</datasource.prefix>\r\n			</properties>\r\n"
			+ "			<distributionManagement>\r\n				<repository>\r\n"
			+ "					<id>gcm</id>\r\n					<name>GCM Releases</name>\r\n"
			+ "					<url>${repo.url.releases}</url>\r\n				</repository>\r\n"
			+ "				<snapshotRepository>\r\n					<id>dev</id>\r\n"
			+ "					<name>GCM Snapshots</name>\r\n"
			+ "					<url>${repo.url.snapshots}</url>\r\n				</snapshotRepository>\r\n"
			+ "			</distributionManagement>\r\n		</profile>\r\n	</profiles>";
	static String profilesCleint = "	<profiles>\r\n		<profile>\r\n			<id>weblogic</id>\r\n"
			+ "			<distributionManagement>\r\n				<repository>\r\n"
			+ "					<id>wl</id>\r\n					<name>Weblogic Releases</name>\r\n"
			+ "					<url>${repo.url.weblogic}</url>\r\n				</repository>\r\n"
			+ "				<snapshotRepository>\r\n					<id>dev</id>\r\n"
			+ "					<name>GCM Snapshots</name>\r\n"
			+ "					<url>${repo.url.snapshots}</url>\r\n				</snapshotRepository>\r\n"
			+ "			</distributionManagement>\r\n		</profile>\r\n		<profile>\r\n"
			+ "			<id>default</id>\r\n			<distributionManagement>\r\n"
			+ "				<repository>\r\n					<id>gcm</id>\r\n"
			+ "					<name>GCM Releases</name>\r\n					<url>${repo.url.releases}</url>\r\n"
			+ "				</repository>\r\n				<snapshotRepository>\r\n"
			+ "					<id>dev</id>\r\n					<name>GCM Snapshots</name>\r\n"
			+ "					<url>${repo.url.snapshots}</url>\r\n				</snapshotRepository>\r\n"
			+ "			</distributionManagement>\r\n		</profile>\r\n	</profiles>";
	static String profilesMain = "  	<profiles>\r\n  		<profile>\r\n            <id>weblogic</id>\r\n"
			+ "			<properties>\r\n				<datasource.prefix>jdbc</datasource.prefix>\r\n"
			+ "			</properties>\r\n            <distributionManagement>\r\n"
			+ "                <repository>\r\n                    <id>wl</id>\r\n"
			+ "                    <name>Weblogic Releases</name>\r\n"
			+ "                    <url>${repo.url.weblogic}</url>\r\n                </repository>\r\n"
			+ "                <snapshotRepository>\r\n                    <id>dev</id>\r\n"
			+ "                    <name>GCM Snapshots</name>\r\n"
			+ "                    <url>${repo.url.snapshots}</url>\r\n                </snapshotRepository>\r\n"
			+ "            </distributionManagement>\r\n        </profile>\r\n        <profile>\r\n"
			+ "            <id>default</id>\r\n			<properties>\r\n"
			+ "				<datasource.prefix>java:</datasource.prefix>\r\n			</properties>\r\n"
			+ "            <distributionManagement>\r\n                <repository>\r\n"
			+ "                    <id>gcm</id>\r\n                    <name>GCM Releases</name>\r\n"
			+ "                    <url>${repo.url.releases}</url>\r\n                </repository>\r\n"
			+ "                <snapshotRepository>\r\n                    <id>dev</id>\r\n"
			+ "                    <name>GCM Snapshots</name>\r\n"
			+ "                    <url>${repo.url.snapshots}</url>\r\n                </snapshotRepository>\r\n"
			+ "            </distributionManagement>\r\n       </profile>\r\n  	</profiles>";

	public static void main2(String[] args) throws IOException, SAXException, ParserConfigurationException {
		File root = new File("D:\\PRODUCTS\\NES\\tbt.i");
		readPlungin(root);
		readResource(root);
		readProfiles(root);
		readPersistence(root);

	}

	private static void readPlungin(File root) throws IOException {
		String ejb = ".EJB";
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				readPlungin(f);
			} else if (f.getName().equals("pom.xml") && f.getPath().toLowerCase().contains(ejb.toLowerCase())) {
				String pthFileName = f.getPath();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				boolean isPlungin = false;
				while ((ln = br.readLine()) != null) {
					if (!isPlungin) {
						if (sb.length() > 0)
							sb.append("\n");
						if (ln.trim().equals("<plugins>")) {
							isPlungin = true;
							continue;
						}
						sb.append(ln);
					}
					if (ln.trim().equals("</plugins>")) {
						sb.append(POMChanger.plungin);
						isPlungin = false;
					}
				}

				br.close();

				String pom = sb.toString();
				System.out.println(pthFileName);

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f.getPath()), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			}
		}
	}

	private static void readResource(File root) throws IOException {
		String ejb = ".EJB";
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				readResource(f);
			} else if (f.getName().equals("pom.xml") && f.getPath().toLowerCase().contains(ejb.toLowerCase())) {
				String pthFileName = f.getPath();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				boolean isResource = false;
				while ((ln = br.readLine()) != null) {
					if (!isResource) {
						if (sb.length() > 0)
							sb.append("\n");
						if (ln.trim().equals("<resources>")) {
							isResource = true;
							continue;
						}
						sb.append(ln);
					}
					if (ln.trim().equals("</resources>")) {
						sb.append(POMChanger.resource);
						isResource = false;
					}
				}

				br.close();

				String pom = sb.toString();
				System.out.println(pthFileName);

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f.getPath()), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			}
		}
	}

	private static void readPersistence(File root) throws IOException {
		String ejb = ".EJB";
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				readPersistence(f);
			} else if (f.getName().equals("persistence.xml") && f.getPath().toLowerCase().contains(ejb.toLowerCase())) {
				String pthFileName = f.getPath();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				boolean isResource = false;
				while ((ln = br.readLine()) != null) {
					if (sb.length() > 0)
						sb.append("\n");
					if (ln.trim().equals("<jta-data-source>java:/nes</jta-data-source>")) {
						ln = "		<jta-data-source>${datasource.prefix}/nes</jta-data-source>";
						sb.append(ln);
					} else
						sb.append(ln);
				}

				br.close();

				String pom = sb.toString();
				System.out.println(pthFileName);

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f.getPath()), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			}
		}
	}

	private static void readProfiles(File root) throws IOException, SAXException, ParserConfigurationException {
		File[] files = root.listFiles();
		String ejb = ".EJB";
		String clnt = ".Client";
		String notSring1 = "target";
		String notSring2 = "bin";
		for (File f : files) {
			if (f.isDirectory()) {
				readProfiles(f);
			} else if (f.getName().equals("pom.xml") && f.getPath().toLowerCase().contains(ejb.toLowerCase())
					&& !f.getPath().toLowerCase().contains(notSring1.toLowerCase())
					&& !f.getPath().toLowerCase().contains(notSring2.toLowerCase())) {
				System.out.println(f.getPath());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				String modName = "";
				String strNames[] = f.getPath().trim().toLowerCase().split("\\\\");
				for (int i = 0; i < strNames.length; i++) {
					System.out.println(strNames[i]);
					if (strNames[i].contains(".ejb")) {
						String s[] = strNames[i].trim().toLowerCase().split("\\.");
						modName = s[0] + "." + s[1];
						System.out.println(modName);
					}
				}

				boolean hasProfiles = false, isDistributed = false;
				boolean dependencies = false, dependency = false, artifactId = false;
				while ((ln = br.readLine()) != null) {
					if (!dependencies) {
						if (ln.trim().contains("<dependencies>") || ln.trim().contains("<plugins>")) {
							dependencies = true;
						}
					} else if (ln.trim().contains("</dependencies>") || ln.trim().contains("</plugins>")) {
						dependencies = false;
					}
					if (!dependency) {
						if (ln.trim().contains("<dependency>")) {
							dependency = true;
						}
					} else if (ln.trim().contains("</dependency>")) {
						dependency = false;
						artifactId = false;
					}
					if (dependency) {
						if (ln.trim().contains("<artifactId>") && ln.trim().contains(modName)) {
							artifactId = true;
						}

					}

					if (dependency && artifactId && ln.trim().contains("<version>".trim())) {
						System.out.println(ln + dependencies);
						sb.append("\n");
						if (ln.toLowerCase().contains("[".toLowerCase()) || ln.toLowerCase().contains(")".toLowerCase())
								|| ln.toLowerCase().contains("LATEST".toLowerCase())
								|| ln.toLowerCase().contains("SNAPSHOT".toLowerCase()))
							sb.append(ln);
						else {
							String strVer = ln.trim().toLowerCase().split("<version>")[1].toString()
									.split("</version>")[0];
							String strVerLast[] = strVer.trim().toLowerCase().split("\\.");
							if (strVerLast != null) {
								System.out.println(strVerLast[0]);
								Long versionLast = Long.parseLong(strVerLast[2]);
								ln = "			<version>" + strVerLast[0] + "." + strVerLast[1] + "." + (++versionLast)
										+ "</version>";
								sb.append(ln);
							}
						}

					} else if (!dependencies && ln.toLowerCase().contains("<version>".toLowerCase())) {
						sb.append("\n");
						if (ln.toLowerCase().contains("maven.build.timestamp".toLowerCase())
								|| ln.toLowerCase().contains("SNAPSHOT".toLowerCase()))
							sb.append(ln);
						else {
							String strVer = ln.trim().toLowerCase().split("<version>")[1].toString()
									.split("</version>")[0];
							String strVerLast[] = strVer.trim().toLowerCase().split("\\.");
							if (strVerLast != null) {
								System.out.println(strVerLast[0]);
								Long versionLast = Long.parseLong(strVerLast[2]);
								ln = "	<version>" + strVerLast[0] + "." + strVerLast[1] + "." + (++versionLast)
										+ "</version>";
								sb.append(ln);
							}
						}

					} else {
						if (!hasProfiles) {
							if (sb.length() > 0)
								sb.append("\n");
							if (ln.trim().equals("<profiles>")) {
								hasProfiles = true;
								continue;
							} else if (ln.trim().equals("<distributionManagement>") && !isDistributed) {
								hasProfiles = true;
								isDistributed = true;
								continue;
							}
							sb.append(ln);
						}
						if (ln.trim().equals("</profiles>")) {
							sb.append(POMChanger.profiles);
							hasProfiles = false;
						}
						if (ln.trim().equals("</distributionManagement>") && isDistributed) {
							sb.append(POMChanger.profiles);
							hasProfiles = false;
						}
					}
				}
				br.close();

				String pom = sb.toString();
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f.getPath()), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			} else if (f.getName().equals("pom.xml") && f.getPath().toLowerCase().contains(clnt.toLowerCase())
					&& !f.getPath().toLowerCase().contains(notSring1.toLowerCase())
					&& !f.getPath().toLowerCase().contains(notSring2.toLowerCase())) {
				System.out.println(f.getPath());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				boolean hasProfiles = false, isDistributed = false, dependencies = false;
				while ((ln = br.readLine()) != null) {
					if (!dependencies) {
						if (ln.trim().contains("<dependencies>") || ln.trim().contains("<plugins>")) {
							dependencies = true;
						}
					} else if (ln.trim().contains("</dependencies>") || ln.trim().contains("</plugins>")) {
						dependencies = false;
					}
					if (!dependencies && ln.toLowerCase().contains("<version>".toLowerCase())) {
						sb.append("\n");
						if (ln.toLowerCase().contains("maven.build.timestamp".toLowerCase())
								|| ln.toLowerCase().contains("SNAPSHOT".toLowerCase()))
							sb.append(ln);
						else {
							String strVer = ln.trim().toLowerCase().split("<version>")[1].toString()
									.split("</version>")[0];
							String strVerLast[] = strVer.trim().toLowerCase().split("\\.");
							if (strVerLast != null) {
								System.out.println(strVerLast[0]);
								Long versionLast = Long.parseLong(strVerLast[2]);
								ln = "	<version>" + strVerLast[0] + "." + strVerLast[1] + "." + (++versionLast)
										+ "</version>";
								sb.append(ln);
							}
						}

					} else {
						if (!hasProfiles) {
							if (sb.length() > 0)
								sb.append("\n");
							if (ln.trim().equals("<profiles>")) {
								hasProfiles = true;
								continue;
							} else if (ln.trim().equals("<distributionManagement>") && !isDistributed) {
								hasProfiles = true;
								isDistributed = true;
								continue;
							}
							sb.append(ln);
						}
						if (ln.trim().equals("</profiles>")) {
							sb.append(POMChanger.profilesCleint);
							hasProfiles = false;
						}
						if (ln.trim().equals("</distributionManagement>") && isDistributed) {
							sb.append(POMChanger.profilesCleint);
							hasProfiles = false;
						}
					}
				}
				br.close();

				String pom = sb.toString();

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f.getPath()), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			} else if (f.getName().equals("pom.xml") && !f.getPath().toLowerCase().contains(clnt.toLowerCase())
					&& !f.getPath().toLowerCase().contains(clnt.toLowerCase())
					&& !f.getPath().toLowerCase().contains(notSring1.toLowerCase())
					&& !f.getPath().toLowerCase().contains(notSring2.toLowerCase())) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				boolean hasProfiles = false, isDistributed = false;
				while ((ln = br.readLine()) != null) {
					if (!hasProfiles) {
						if (sb.length() > 0)
							sb.append("\n");
						if (ln.trim().equals("<profiles>")) {
							hasProfiles = true;
							continue;
						} else if (ln.trim().equals("<distributionManagement>") && !isDistributed) {
							hasProfiles = true;
							isDistributed = true;
							continue;
						}
						sb.append(ln);
					}
					if (ln.trim().equals("</profiles>")) {
						sb.append(POMChanger.profilesMain);
						hasProfiles = false;
					}
					if (ln.trim().equals("</distributionManagement>") && isDistributed) {
						sb.append(POMChanger.profilesMain);
						hasProfiles = false;
					}
				}
				br.close();

				String pom = sb.toString();

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(f.getPath()), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			}
		}
	}

	private static void read(File root) throws IOException, SAXException, ParserConfigurationException {
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				read(f);
			} else if (f.getName().equals("pom.xml")) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String ln;
				boolean isResource = false, isDistributed = false;
				boolean hasProfiles = false, prevLineDepFinish = false;
				while ((ln = br.readLine()) != null) {
					if (!isResource) {
						if (sb.length() > 0)
							sb.append("\n");
						if (ln.trim().equals("<resources>")) {
							isResource = true;
							continue;
						}
						sb.append(ln);

					} else if (hasProfiles) {
						if (sb.length() > 0)
							sb.append("\n");
						if (ln.trim().equals("<profiles>")) {
							hasProfiles = true;
							continue;
						}
						sb.append(ln);
					} else {
						if (ln.trim().equals("</resources>")) {
							sb.append(POMChanger.resource);
							isResource = false;
						}

					}
				}

				br.close();

				String pom = sb.toString();

				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("D:\\temp\\bb.xml"), "UTF-8"));
				bw.write(pom);
				bw.flush();
				bw.close();
			}
		}
	}
}
