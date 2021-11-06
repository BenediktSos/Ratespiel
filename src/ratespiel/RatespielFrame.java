package ratespiel;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RatespielFrame {

	private final Semaphore semaphore = new Semaphore(0);
	public String readInput;
	JLabel requestLabel;
	JLabel hintLabel;
	JLabel[] history;

	public void initialise() {
		// Create window
		JFrame mainWindow = new JFrame("Ratespiel");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// IconImage
		Image icon = new ImageIcon(getClass().getResource("/ratespiel/TopImage_NyanBenedikt.png")).getImage();
		mainWindow.setIconImage(icon);

		// Content of window
		mainWindow.setLayout(new GridLayout(0, 1));

		requestLabel = new JLabel("Hello World!"); // line1

		hintLabel = new JLabel("How are you today?"); // line2

		JTextField input = new JTextField(5); // line3
		input.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readInput = input.getText();
				input.setText(null);
				semaphore.release();

			}
		});

		int historyLines = 10; // line4+
		history = new JLabel[historyLines];

		mainWindow.add(requestLabel); // adding all lines to window
		mainWindow.add(input);
		mainWindow.add(hintLabel);
		for (int i = 0; i < history.length; i++) {
			history[i] = new JLabel("-");
			mainWindow.add(history[i]);
		}

		// final settings
		mainWindow.pack();
		mainWindow.setSize(250, 300);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);
	}

	public int findInt() {
		// wait for 'Enter'
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// get input from 'input' JTextfield

		Scanner scanner = new Scanner(readInput);

		if (readInput != null) {
			// try not to crash without integer input
			try {
				return scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Unintended Input: \"!int\"");
				System.out.println("\t" + e);
				return Integer.MAX_VALUE;
			}

		} else {
			return Integer.MAX_VALUE;
		}
	}

	public String findString() {
		// wait for 'Enter'
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// get input from 'input' JTextfield
		if (readInput != null) {
			return Character.toString(readInput.toCharArray()[0]);
		} else {
			return "";
		}
	}

	public void setRequestText(String text) {
		requestLabel.setText(text);
	}

	public void setHintText(String text) {
		hintLabel.setText(text);
	}

	public void appendHistory(String apppendString) {
		// 'append' to first place,
		// pushing everything back
		for (int i = (history.length - 1); i >= 0; i--) {
			if (i > 0) {
				history[i].setText(history[i - 1].getText());
			} else {
				history[0].setText(apppendString);
			}
		}
	}
}
