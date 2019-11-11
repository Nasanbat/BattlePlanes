package hackathon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TestData {
	public static void main(String[] args) throws IOException {
		LoadFiles();
	}

	private static String LoadFiles() throws IOException {
		int x = 0, y = 0;

		String logPath = "D:\\hackathon\\battlePlanes";
		File LogRoot = new File(logPath);
		List<String> fileNames = new ArrayList<>();
		File[] logfiles = LogRoot.listFiles();
		for (File f : logfiles) {
			StringBuilder data = readFiles(f);

			String[] games = data.toString().split("@");
			for (String game : games) {
				if (game.length() > 0) {

					game = game.replace("^^", "#");
					String[] temp = game.split("#");
					if (temp.length > 2) {
						String[] teams = temp[0].split("T");

						int[][] state = new int[10][10];
						String t1 = teams[1];
						String t2 = teams[2];
						String[] team1 = t1.split("[|]");
						for (int i = 0; i < team1.length; i++) {
							if (team1[i].equals("U")) {
								// Дээшээ
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team1 = > " + team1[i] + " = " + x + "," + y);
							} else if (team1[i].equals("R")) {
								// Баруун
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team1 = > " + team1[i] + " = " + x + "," + y);
							} else if (team1[i].equals("D")) {
								// Доошоо
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team1 = > " + team1[i] + " = " + x + "," + y);
							} else if (team1[i].equals("L")) {
								// Зүүн
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team1 = > " + team1[i] + " = " + x + "," + y);
							}
						}
						String[] team2 = t2.split("[|]");
						for (int i = 0; i < team2.length; i++) {
							if (team2[i].equals("U")) {
								// Дээшээ
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team2 = > " + team2[i] + " = " + x + "," + y);
							} else if (team2[i].equals("R")) {
								// Баруун
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team2 = > " + team2[i] + " = " + x + "," + y);
							} else if (team2[i].equals("D")) {
								// Доошоо
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team2 = > " + team2[i] + " = " + x + "," + y);
							} else if (team2[i].equals("L")) {
								// Зүүн
								y = i % 11 - 1;
								x = i / 11;
								System.out.println(" team2 = > " + team2[i] + " = " + x + "," + y);
							}
						}

						String tmp = temp[1].replace(" ", "").replace("(", "").replace(")", ",");
						String[] shots = tmp.split(",");

						int d = (shots.length % 4 > 0) ? 1 : 0;
						int[][] p1 = new int[shots.length / 4 + d][2];
						int[][] p2 = new int[shots.length / 4 + d][2];

						for (int i = 0; i < shots.length; i = i + 4) {
							p1[i / 4][0] = Integer.parseInt(shots[i]);
							p1[i / 4][1] = Integer.parseInt(shots[i + 1]);
							if (shots.length > i + 2) {
								p2[i / 4][0] = Integer.parseInt(shots[i + 2]);
								p2[i / 4][1] = Integer.parseInt(shots[i + 3]);
							}
						}

						System.out.print(" team1 = > ");
						for (int i = 0; i < p1.length; i++) {
							System.out.print("[" + p1[i][0] + "," + p1[i][1] + "]");
						}

						System.out.println();
						System.out.print(" team2 = > ");
						for (int i = 0; i < p2.length; i++) {
							System.out.print("[" + p2[i][0] + "," + p2[i][1] + "]");
						}
						System.out.println();
					}

				}
			}
		}
		return "";
	}

	static StringBuilder readFiles(File f) throws IOException {
		StringBuilder s = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

			String ln;
			try {
				while ((ln = br.readLine()) != null) {
					s.append(ln.trim() + " ");
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
		return s;
	}
}
