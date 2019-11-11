package hackathon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * @author jason.nasanbat
 *
 */
public class BattlePlanes {
	public static void main(String[] args) throws IOException {
		plane[] t = place_ships();
		for (plane a : t)
			System.out.print(a.toString());

		int[][] state = new int[][] { { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, -1, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, -1, 0, 0, 0, 0 }, { 0, 0, 0, -1, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, -1, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, -1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		int[][] shoots = new int[][] { { 3, 3 }, { 2, 5 }, { 6, 4 }, { 5, 2 }, { 0, 0 } };
		int[] a = shoot(state, shoots);
	}

	/**
	 * # 2. Буудах
	 * 
	 * @param state:  Эсрэг талын төлөв. 0-Буудах боломжтой, -1-Буудаад оноогүй,
	 *                8-Шарх (Оногдсон бие), 9-Сөнөсөн (Оногдсон толгой)
	 * @param shoots: Таны буудсан координатууд (Буудсан дарааллаар)
	 * @return
	 */
	public static int[] shoot(int[][] state, int[][] shoots) {
		int[] a = new int[2];
		state = fullState(state, shoots);
		if (find == 1) {
			// 1 Шарх олчихлоо
			a = shotLevel1(state, sharkh[0][0], sharkh[0][1]);
			return a;
		} else if (find == 2) {
			// 2 Шарх олчихлоо
			a = shotLevel2(state, sharkh);
			return a;
		} else if (find == 3) {
			// 3 Шарх олчихлоо

			return a;
		} else {
			// Шарх олоогүй учраас
			return findPlane();
		}
	}

	public static int[] shotLevel1(int[][] state, int x, int y) {
		int[] shot = new int[2];
		int[] s1 = new int[2], s2 = new int[2], s3 = new int[2], s4 = new int[2];
		// Дээд зүүн
		if (x - 1 >= 0 && x - 1 < 9 && y - 1 >= 0 && y - 1 < 9) {
			if (state[x - 1][y - 1] == 0) {
				s1 = new int[] { x - 1, y - 1 };
			}
		}
		// дээд баруун
		if (x - 1 > 0 && x - 1 < 9 && y + 1 > 0 && y + 1 <= 9) {
			if (state[x - 1][y + 1] == 0) {
				s2 = new int[] { x - 1, y + 1 };
			}
		}
		// Доод баруун
		if (x + 1 > 0 && x + 1 <= 9 && y + 1 > 0 && y + 1 <= 9) {
			if (state[x + 1][y + 1] == 0) {
				s3 = new int[] { x + 1, y + 1 };
			}
		}
		// Доод зүүн
		if (x + 1 > 0 && x + 1 <= 9 && y - 1 >= 0 && y - 1 < 9) {
			if (state[x + 1][y - 1] == 0) {
				s4 = new int[] { x + 1, y - 1 };
			}
		}
		for (int sh = 0; sh < 4; sh++) {
			int x1 = 0, y1 = 0;
			if (sh == 0 && s1[0] != 0 && s1[1] != 0) {
				x1 = s1[0];
				y1 = s1[1];
			}
			if (sh == 1 && s2[0] != 0 && s2[1] != 0) {
				x1 = s2[0];
				y1 = s2[1];
			}
			if (sh == 2 && s3[0] != 0 && s3[1] != 0) {
				x1 = s3[0];
				y1 = s3[1];
			}
			if (sh == 3 && s4[0] != 0 && s4[1] != 0) {
				x1 = s4[0];
				y1 = s4[1];
			}
			if (x1 != 0 && y1 != 0 && drawPlane1(state, x1, y1, sharkh)) {
				shot = new int[] { x1, y1 };
				break;
			}
		}

		return shot;
	}

	private static int[] randomShoot(int[][] arr) {
		Random r = new Random();
		int i = r.nextInt(arr.length);
		return arr[i];
	}

	public static int[] shotLevel2(int[][] state, int[][] sh) {

		int x1 = sh[0][0];
		int y1 = sh[0][1];

		int x2 = sh[1][0];
		int y2 = sh[1][1];

		int[][] avialablePoint = new int[0][2];

		if (y1 < y2) {
			// Дээд талын шарх y1
			if (x1 > x2) {
				/* Баруун тийш диагнал */

				// Level1 4 цэг

				// Дээд шархны зүүн дээд булан
				if (y1 > 0 && x2 > 0 && y2 < 9 && state[x1 - 1][y1 - 1] == 0 && drawPlane1(state, x1 - 1, y1 - 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x1 - 1, y1 - 1);
				}
				// Дээд шархны баруун доод булан
				if (x1 < 9 && x2 > 0 && y2 < 9 && state[x1 + 1][y1 + 1] == 0 && drawPlane1(state, x1 + 1, y1 + 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x1 + 1, y1 + 1);
				}
				// Доод шархны зүүн дээд булан
				if (y1 > 0 && x2 > 0 && x1 < 9 && state[x2 - 1][y2 - 1] == 0 && drawPlane1(state, x2 - 1, y2 - 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x2 - 1, y2 - 1);
				}
				// Доод шархны баруун доод булан
				if (y1 > 0 && x1 < 9 && y2 < 9 && state[x2 + 1][y2 + 1] == 0 && drawPlane1(state, x2 + 1, y2 + 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x2 + 1, y2 + 1);
				}

				if (avialablePoint.length > 0) {
					return randomShoot(avialablePoint);
				} else {
					// Level2 4 цэг 1 нүдний зайтай цэгүүд

					// Дээд шархны дээд тал
					if (y1 > 1 && x1 < 9 && state[x1][y1 - 2] == 0 && drawPlane1(state, x1, y1 - 2, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x1, y1 - 2);
					}
					// Дээд шархны баруун тал
					if (y1 > 0 && x1 < 9 && state[x1 + 2][y1] == 0 && drawPlane1(state, x1 + 2, y1, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x1 + 2, y1);
					}
					// Доод шархны доод тал
					if (x2 > 0 && y2 < 8 && state[x2][y2 + 2] == 0 && drawPlane1(state, x2, y2 + 2, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x2, y2 + 2);
					}
					// Доод шархны зүүн тал
					if (x2 > 1 && y2 < 9 && state[x2 - 2][y2] == 0 && drawPlane1(state, x2 - 2, y2, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x2 - 2, y2);
					}
					if (avialablePoint.length > 0) {
						return randomShoot(avialablePoint);
					}
				}

			} else {
				/* Зүүн тийш диагнал */

				// Level1 4 цэг

				// Дээд шархны баруун дээд булан
				if (y2 > 0 && y1 < 9 && x1 < 9 && state[x2 + 1][y2 + 1] == 0 && drawPlane1(state, x2 + 1, y2 + 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x2 + 1, y2 + 1);
				}
				// Дээд шархны зүүн доод булан
				if (x1 < 9 && y1 < 9 && x2 > 0 && state[x2 - 1][y2 - 1] == 0 && drawPlane1(state, x2 - 1, y2 - 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x2 - 1, y2 - 1);
				}
				// Доод шархны баруун дээд булан
				if (y2 > 0 && x1 < 9 && x2 > 0 && state[x1 + 1][y1 + 1] == 0 && drawPlane1(state, x1 + 1, y1 + 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x1 + 1, y1 + 1);
				}
				// Доод шархны зүүн доод булан
				if (y2 > 0 && x2 > 0 && y1 < 9 && state[x1 - 1][y1 - 1] == 0 && drawPlane1(state, x1 - 1, y1 - 1, sh)) {
					// 0-Буудах боломжтой
					avialablePoint = add(avialablePoint, x1 - 1, y1 - 1);
				}

				if (avialablePoint.length > 0) {
					return randomShoot(avialablePoint);
				} else {
					// Level2 4 цэг 1 нүдний зайтай цэгүүд

					// Дээд шархны дээд тал
					if (y2 > 1 && x2 > 0 && state[x2][y2 - 2] == 0 && drawPlane1(state, x2, y2 - 2, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x2, y2 - 2);
					}
					// Дээд шархны зүүн тал
					if (y2 > 0 && x2 > 0 && state[x2 - 2][y2] == 0 && drawPlane1(state, x2 - 2, y2, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x2 - 2, y2);
					}
					// Доод шархны доод тал
					if (x1 < 9 && y2 < 8 && state[x1][y1 + 2] == 0 && drawPlane1(state, x1, y1 + 2, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x1, y1 + 2);
					}
					// Доод шархны баруун тал
					if (y1 < 9 && x1 < 9 && state[x1 + 2][y1] == 0 && drawPlane1(state, x1 + 2, y1, sh)) {
						// 0-Буудах боломжтой
						avialablePoint = add(avialablePoint, x1 + 2, y1);
					}
					if (avialablePoint.length > 0) {
						return randomShoot(avialablePoint);
					}
				}

			}
		}
		return new int[] { 0, 0 };
	}

	/**
	 * Онгоц зурна.
	 * 
	 * @return
	 */
	private static boolean drawPlane1(int[][] state, int x, int y, int[][] sh) {
		Random r = new Random();
		boolean bool = true;
		// **********
		// **********
		// **********
		// **********
		// **********
		// ****1*****
		// ***234****
		// ****5*****
		// ***678****
		// **********

		bool = checkHead(state, x, y, sh);

		return bool;
	}

	private static boolean checkHead(int[][] state, int x, int y, int[][] sh) {
		boolean bool = false;
		int[][] p1 = null;
		int b = 1, c = 0;
		int xx = x;
		int yy = y;
		System.out.println("x=" + x + ", y= " + y);
		for (b = 1; b <= 8; b++) {
			System.out.println("Онгоцны аль хэсэг гэж шалгаж байна. b = " + b);
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					System.out.print(state[i][j] + ", ");
				}
				System.out.println();
			}
			for (c = 0; c < 4; c++) {
				x = xx;
				y = yy;
				System.out.println("Онгоц хаашаа харж байна c= " + c);

				if (b == 1) {

				} else if (b == 2) {
					if (c == 0) {
						x = x - 1;
						y = y + 1;
					} else if (c == 1) {
						x = x + 1;
						y = y + 1;
					} else if (c == 2) {
						x = x + 1;
						y = y - 1;
					} else if (c == 3) {
						x = x - 1;
						y = y - 1;
					}
				} else if (b == 3) {
					if (c == 0) {
						x = x - 1;
						y = y;
					} else if (c == 1) {
						x = x;
						y = y + 1;
					} else if (c == 2) {
						x = x + 1;
						y = y;
					} else if (c == 3) {
						x = x;
						y = y - 1;
					}
				} else if (b == 4) {
					if (c == 0) {
						x = x - 1;
						y = y - 1;
					} else if (c == 1) {
						x = x - 1;
						y = y + 1;
					} else if (c == 2) {
						x = x + 1;
						y = y + 1;
					} else if (c == 3) {
						x = x + 1;
						y = y - 1;
					}
				} else if (b == 5) {
					if (c == 0) {
						x = x - 2;
						y = y;
					} else if (c == 1) {
						x = x;
						y = y + 2;
					} else if (c == 2) {
						x = x + 2;
						y = y;
					} else if (c == 3) {
						x = x;
						y = y + 2;
					}
				} else if (b == 6) {
					if (c == 0) {
						x = x - 3;
						y = y + 1;
					} else if (c == 1) {
						x = x + 1;
						y = y + 3;
					} else if (c == 2) {
						x = x + 3;
						y = y - 1;
					} else if (c == 3) {
						x = x - 1;
						y = y - 3;
					}
				} else if (b == 7) {
					if (c == 0) {
						x = x - 3;
						y = y;
					} else if (c == 1) {
						x = x;
						y = y + 3;
					} else if (c == 2) {
						x = x + 3;
						y = y;
					} else if (c == 3) {
						x = x;
						y = y - 3;
					}
				} else if (b == 8) {
					if (c == 0) {
						x = x - 3;
						y = y - 1;
					} else if (c == 1) {
						x = x - 1;
						y = y + 3;
					} else if (c == 2) {
						x = x + 3;
						y = y + 1;
					} else if (c == 3) {
						x = x + 1;
						y = y - 3;
					}
				}
				if (c == 0) {
					// Дээшээ
					p1 = new int[][] { { x, y }, { x + 1, y - 1 }, { x + 1, y }, { x + 1, y + 1 }, { x + 2, y },
							{ x + 3, y - 1 }, { x + 3, y }, { x + 3, y + 1 } };
				} else if (c == 2) {
					// доошоо
					p1 = new int[][] { { x, y }, { x - 1, y + 1 }, { x - 1, y }, { x - 1, y - 1 }, { x - 2, y },
							{ x - 3, y + 1 }, { x - 3, y }, { x - 3, y - 1 } };
				} else if (c == 1) {
					// Баруун
					p1 = new int[][] { { x, y }, { x - 1, y - 1 }, { x, y - 1 }, { x + 1, y - 1 }, { x, y - 2 },
							{ x - 1, y - 3 }, { x, y - 3 }, { x + 1, y - 3 } };
				} else if (c == 3) {
					// Зүүн
					p1 = new int[][] { { x, y }, { x + 1, y + 1 }, { x, y + 1 }, { x - 1, y + 1 }, { x, y + 2 },
							{ x + 1, y + 3 }, { x, y + 3 }, { x - 1, y + 3 } };
				}
				int step = 0;
				for (int l = 0; l < p1.length; l++) {
					if (p1[l][0] >= 0 && p1[l][0] <= 9 && p1[l][1] >= 0 && p1[l][1] <= 9) {
						step = 1;
					} else {
						// ийм онгоц байх боломжгүй.
						step = 0;
						break;
					}
				}
				if (step == 1) {
					// Шархууд энэ онгоц дээр байгаа эсэх
					int k = 0;
					for (int i = 0; i < sh.length; i++) {
						System.out.println("Шарх:" + i + " => " + sh[i][0] + "," + sh[i][1]);
						for (int l = 1; l < p1.length; l++) {
							if (sh[i][0] == p1[l][0] && sh[i][1] == p1[l][1]) {
								k++;
								// Энэ онгоц дээр байна.
								l = p1.length;
							}
						}
					}
					for (int j = 0; j < p1.length; j++) {
						System.out.print("[" + p1[j][0] + "," + p1[j][1] + "],");
					}
					System.out.println();

					if (k == sh.length) {
						step++;
					}
					// Шархууд энэ онгоц дээр байгаа бол Step = 1 байна
					if (step == 2) {
						// Үндсэн board дээр тавиж үзэх
						k = 0;
						for (int l = 0; l < p1.length; l++) {
							int x1 = p1[l][0];
							int y1 = p1[l][1];
							if (state[x1][y1] == 0) {
								// буудаагүй цэг
								k++;
							} else if (state[x1][y1] == 8) {
								for (int i = 0; i < sh.length; i++) {
									if (x1 == sh[i][0] && y1 == sh[i][1]) {
										// энэ онгоцны шарх байна.
										k++;
										break;
									}
								}
							}
						}
						if (k == p1.length) {
							step++;
							// Онгоц байх боломжтой.
							bool = true;
							for (int i = 0; i < 10; i++) {
								for (int j = 0; j < 10; j++) {
									int d = state[i][j];
									for (int l = 0; l < p1.length; l++) {
										if (p1[l][0] == i && p1[l][1] == j)
											d = 5;
									}
									System.out.print(d + ", ");
								}
								System.out.println();
							}
							return bool;
						}
					}
				}
			} // Хаашаа харсанг шалгаж байгаа for
		} // онгоцны аль хэсэг гэдгийг шалгаж байгаа for

		return bool;
	}

	/**
	 * Аль хэсгээс хайж байгааг илэрхийлнэ. 0,1,2 гэсэн утга авна.
	 */
	private static int ilevel = 0;

	private static int find = 0;

	/**
	 * Хамгийн эхний буудах цэгүүд
	 */
	private static Shotlevel[] L1 = new Shotlevel[] { new Shotlevel(1, 2), new Shotlevel(1, 7), new Shotlevel(2, 1),
			new Shotlevel(2, 8), new Shotlevel(7, 1), new Shotlevel(7, 8), new Shotlevel(8, 2), new Shotlevel(8, 7) };
	/**
	 * 2-р төвшинд буудах цэгүүд
	 */
	private static Shotlevel[] L2 = new Shotlevel[] { new Shotlevel(0, 5), new Shotlevel(2, 4), new Shotlevel(3, 6),
			new Shotlevel(4, 0), new Shotlevel(4, 9), new Shotlevel(5, 2), new Shotlevel(5, 7), new Shotlevel(7, 4),
			new Shotlevel(9, 5) };
	/**
	 * 3-р төвшинд буудах цэгүүд
	 */
	private static Shotlevel[] L3 = new Shotlevel[] { new Shotlevel(3, 3), new Shotlevel(4, 4), new Shotlevel(5, 5),
			new Shotlevel(6, 6), };

	/**
	 * Айлын онгоцны толгой байх боломжгүй цэгээд
	 */
	private static Shotlevel[] notHead = new Shotlevel[] { new Shotlevel(0, 0), new Shotlevel(0, 9),
			new Shotlevel(9, 0), new Shotlevel(9, 9), };
	/**
	 * Айлын онгоцны толгой байх боломжгүй цэгээд
	 */
	private static int[][] sharkh = new int[0][2];

	/**
	 * Нэг хэмжээст array дээр add хийх.
	 * 
	 * @return int[]
	 */
	public static int[] add(int arr[], int x) {
		int i;
		int newarr[] = new int[arr.length + 1];

		for (i = 0; i < arr.length; i++)
			newarr[i] = arr[i];

		newarr[arr.length] = x;

		return newarr;
	}

	/**
	 * Хоёр хэмжээст array дээр add хийх.
	 * 
	 * @return int[][]
	 */
	public static int[][] add(int arr[][], int x, int y) {
		int i;
		int newarr[][] = new int[arr.length + 1][2];

		for (i = 0; i < arr.length; i++) {
			newarr[i][0] = arr[i][0];
			newarr[i][1] = arr[i][1];
		}

		newarr[arr.length][0] = x;
		newarr[arr.length][1] = y;

		return newarr;
	}

	/**
	 * Тайлбайд буудсан цэгүүдээ тэмдэглэнэ
	 * 
	 * @param state
	 * @param shoots
	 * @return
	 */
	private static int[][] fullState(int[][] state, int[][] shoots) {
		int[][] tmp = state;
		int head = 0;

		for (int i = 0; i < shoots.length; i++) {

			int x = shoots[i][0];
			int y = shoots[i][1];
			if (state[x][y] == 0) {
				// 0-Буудах боломжтой
			} else if (state[x][y] == 1) {
				// 1-Буудаад оноогүй
			} else if (state[x][y] == 8) {
				// 8-Шарх (Оногдсон бие)
			} else if (state[x][y] == 9) {
				// 9-Сөнөсөн (Оногдсон толгой)
			}
		}
		int _find = 0;
		int x1 = 0, y1 = 0;
		int[][] sharkh0 = new int[0][2];
		int[][] sharkh1 = new int[0][2];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				if (state[i][j] == 0) {
					// 0-Буудах боломжтой

					// 1. notHead -д байвал 7 болгоно.

				} else if (state[i][j] == -1) {
					// 1-Буудаад оноогүй
				} else if (state[i][j] == 8) {
					// 8-Шарх (Оногдсон бие)
					int isMe = 0;
					for (int l = 0; l < shoots.length; l++) {
						if (state[l][0] == i && state[l][1] == j) {
							// Миний өмнө нь буудсан шарх
							_find++;
							sharkh0 = add(sharkh0, i, j);
							isMe = 1;
						}
					}
					if (isMe == 0) {
						sharkh1 = add(sharkh1, i, j);
					}
				} else if (state[i][j] == 9) {
					// 9-Сөнөсөн (Оногдсон толгой)
					head = 1;
					x1 = i;
					y1 = j;
				}
			}
		}
		// Миний буудаагүй шархыг шалгана.
		if (sharkh1.length == 7) {
			// Шууд толгой онсон онгоц
			if (_find > 0) {
				find = _find;
				sharkh = sharkh0;
			}
		} else if (sharkh1.length == 6) {
			// Би 1 шарх аваад толгой онсон онгоц
			// getPlane()
		} else if (sharkh1.length == 5) {
			// Би 2 шарх аваад толгой онсон онгоц
		} else if (sharkh1.length == 4) {
			// Би 3 шарх аваад толгой онсон онгоц
		} else if (sharkh1.length == 3) {
			// Би 4 шарх аваад толгой онсон онгоц
		} else if (sharkh1.length == 2) {
			// Би 5 шарх аваад толгой онсон онгоц
		} else if (sharkh1.length == 1) {
			// Би 6 шарх аваад толгой онсон онгоц
		} else if (sharkh1.length == 0 && head == 1) {
			// Би 7 шарх аваад толгой онсон онгоц
		}

		return tmp;

	}

	/**
	 * Хамгийн цөөн үйлдлээр нийт талбайгаас онгоц олох
	 */
	private static int[] findPlane() {
		int[] a = new int[2];
		Random r = new Random();
		int i = 0;
		while (ilevel == 0 && L1.length > 0) {
			i = r.nextInt(L1.length);
			a[0] = L1[i].x;
			a[1] = L1[i].y;
			return a;
		}
		ilevel++;
		while (ilevel == 1 && L2.length > 0) {
			i = r.nextInt(L2.length);
			a[0] = L2[i].x;
			a[1] = L2[i].y;
			return a;
		}
		ilevel++;
		while (ilevel == 2 && L3.length > 0) {
			i = r.nextInt(L3.length);
			a[0] = L3[i].x;
			a[1] = L3[i].y;
			return a;
		}
		return a;
	}

	/**
	 * # 1. Эхлүүлэх
	 * 
	 * @return Өөрийн онгоцны байрлал
	 */
	public static plane[] place_ships() {
		plane[] a = new plane[2];

		a[0] = createPlane(null);
		System.out.print(a[0].toString());
		a[1] = createPlane(a[0]);
		System.out.print(a[1].toString());

		return a;
	}

	private static plane createPlane(plane firstPlan) {
		plane a = new plane();
		Random r = new Random();
		if (firstPlan == null) {
			// Эхний онгоц
			a.setDir(r.nextInt(4));
			a.generatePlane();
			a.setFirstPlane(a);
		} else {
			// 2 дахь онгоц
			a.setDir(r.nextInt(4));
			a.setFirstPlane(firstPlan);
			a.generatePlane();
		}

		return a;
	}

	public static class plane {

		private int dir;
		private int head[];

		private plane firstPlane;

		public int getDir() {
			return dir;
		}

		@Override
		public String toString() {
			return "plane [dir=" + dir + ", head=" + Arrays.toString(head) + "]";
		}

		public void setDir(int dir) {
			this.dir = dir;
			// Тест дата
			this.dir = 2;
		}

		public int[] getHead() {
			return head;
		}

		public void setHead(int[] head) {
			this.head = head;
		}

		public void setFirstPlane(plane firstPlane) {
			this.firstPlane = firstPlane;
		}

		public void generatePlane() {
			// 10x10 харьцаанд багтах онгоцын толгой зурна.
			int[] head1 = new int[2];
			head1[0] = 9; // Толгойн координат: X
			head1[1] = 3; // Толгойн координат: Y

			if (firstPlane != null) {
				head1[0] = 8; // Толгойн координат: X
				head1[1] = 4; // Толгойн координат: Y
			}
			head = head1;
		}

		private boolean checkPlane(int[] h) {
			boolean r = false;
			if (h[0] >= 0 && h[0] <= 9) {
				if (h[1] >= 0 && h[1] <= 9) {
					// онгоц хаашаа харсан 0-Дээшээ, 1-Баруун, 2-Доошоо, 3-Зүүн
					if (dir == 0) {
						// 0-Дээшээ
						// TODO
						r = true;
					} else if (dir == 1) {
						// 1-Баруун
						// TODO
						r = true;
					} else if (dir == 2) {
						// 2-Доошоо

						// TODO
						r = true;
					} else if (dir == 3) {
						// 3-Зүүн

						// TODO
						r = true;
					}
				}
			}
			return r;
		}
	}

	public static class Shotlevel {
		public int x;
		public int y;

		public Shotlevel(int x1, int y1) {
			x = x1;
			y = y1;
		}
	}
}
