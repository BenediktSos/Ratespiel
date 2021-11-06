package ratespiel;

import static java.lang.System.exit;

public class RatespielRefactured {

	public static void main(String[] args) {
		// initialise window 'frame'
		RatespielFrame frame = new RatespielFrame();
		frame.initialise();

		// autosolver
		boolean autosolve = askYesNo(frame, "Autosolver aktivieren?(y/N)");
		// upper bound of random number
		int maxBorder = requestInt(frame, "Was soll die Obergrenze sein?");
		// lower bound of random number
		int minBorder = requestInt(frame, "Was soll die Untergrenze sein?");
		// End of set-up

		while (true) {
			// make sure minBorder < maxBorder
			if (maxBorder < minBorder) {
				int temp = maxBorder;
				maxBorder = minBorder;
				minBorder = temp;
			}

			int random = minBorder + (int) (Math.random() * (1 + maxBorder - minBorder));
			// the game itself
			// init
			int versucheZaehler = 0;
			boolean zuHoch = false;
			double eingabe = 0;
			long startZeit = System.currentTimeMillis();

			frame.setRequestText("Bitte eine Zahl eingeben:");

			while (true) { // Abfragen von Zahlen bis zur Loesung
				versucheZaehler++;
				if (autosolve) {
					eingabe = solve(zuHoch, eingabe, versucheZaehler, minBorder, maxBorder);
				} else {
					eingabe = frame.findInt();
				}

				frame.appendHistory(Integer.toString((int) eingabe));

				if ((int) eingabe < random) {
					frame.setHintText("Ihre Zahl ist zu niedrig!");
					zuHoch = false;
				} else if ((int) eingabe > random) {
					frame.setHintText("Ihre Zahl ist zu hoch!");
					zuHoch = true;
				} else {
					frame.setHintText("Gewonnen!");
					// frame.setRequestText("Gewonnen!"); //Instantly overwritten, useless
					break;
				}

			}

			// Game end

			double neededTime = ((System.currentTimeMillis()) - startZeit) / 1000d;
			int neededMinutes = (int) (neededTime / 60);
			double neededSeconds = neededTime - (neededMinutes / 60);

			if (!autosolve) {

				frame.appendHistory(" Sie haben " + versucheZaehler + " Versuche gebraucht.");
				if (neededMinutes > 0) {
					frame.appendHistory(" und " + neededSeconds + " Sekunden gedauert.");
					frame.appendHistory(" Es hat " + neededMinutes + " Minuten");
				} else {
					frame.appendHistory(" Es hat " + neededSeconds + " Sekunden gedauert.");
				}

				if (neededTime > (Math.sqrt((maxBorder - minBorder) * 100) + 5)) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					frame.appendHistory("Ich bin nicht entäuscht,...");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					frame.appendHistory("aber...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					frame.appendHistory("...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					frame.appendHistory("Ich hätte mehr von dir erwartet.");
				}
			} else {
				frame.appendHistory(" um die Lösung zu finden.");
				frame.appendHistory(" " + neededTime + " Sekunden gebraucht ");
				frame.appendHistory(" Es hat " + versucheZaehler + " Versuche und ");
			}

			// Ask for new Round
			// continue to new round?
			if (askYesNo(frame, "Spiel beenden?(y/N)")) {

				// decide to end game or new init
				if (askYesNo(frame, "Zahlengrenzen ändern?(y/N)")) {

					autosolve = askYesNo(frame, "Autosolver aktivieren?(y/N)");
					maxBorder = requestInt(frame, "Was soll die Obergrenze sein?");
					minBorder = requestInt(frame, "Was soll die Untergrenze sein?");

				} else {
					frame.setRequestText("-----------");
					frame.setHintText("Spiel beendet!");
					break;
				}
			}
		}
		exit(0);
	}

	static double solve(boolean zuHoch, double eingabe, int versucheZaehler, int minBorder, int maxBorder) {
		double ausgabe;
		double supplement;
		// initialise eingabewert
		if (versucheZaehler == 1) {
			eingabe = minBorder;
		}
		// calculate best supplement
		supplement = ((maxBorder - minBorder) / Math.pow(2, versucheZaehler));
		if (supplement < 1 && supplement > 0) {
			supplement = 1;
		} else if (supplement > -1 && supplement < 0) {
			supplement = -1;
		}
		// antispeed

		try {
			Thread.sleep(250 * (int) (1 + Math.log10(versucheZaehler) + Math.log10(supplement)));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// ausgabe...
		ausgabe = (zuHoch) ? (eingabe - supplement) : (eingabe + supplement);
		return ausgabe;
	}

	private static String requestString(RatespielFrame frame, String request) {
		frame.setRequestText(request);
		String returnString = "";
		while (returnString.isEmpty()) {
			returnString = frame.findString();
		}
		return returnString;
	}

	/*
	 * private static String hintString(RatespielFrame frame, String request) {
	 * frame.setHintText(request); String returnString = ""; while
	 * (returnString.isEmpty()) { returnString = frame.findString(); } return
	 * returnString; }
	 */

	private static int requestInt(RatespielFrame frame, String request) {
		frame.setRequestText(request);
		int returnInt = Integer.MAX_VALUE;
		while (returnInt == Integer.MAX_VALUE) {
			returnInt = frame.findInt();
		}
		return returnInt;
	}

	/*
	 * private static int hintInt(RatespielFrame frame, String request) {
	 * frame.setHintText(request); int returnInt = Integer.MAX_VALUE; while
	 * (returnInt == Integer.MAX_VALUE) { returnInt = frame.findInt(); } return
	 * returnInt; }
	 */

	private static boolean askYesNo(RatespielFrame frame, String request) {

		String yNString = "";
		while (!(yNString.equalsIgnoreCase("y") || yNString.equalsIgnoreCase("n"))) {
			yNString = requestString(frame, request);
		}

		return yNString.equalsIgnoreCase("y");
	}
}
