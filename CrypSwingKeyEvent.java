package crypSwing;

import javax.swing.*;
import java.awt.event.*;
import java.awt.KeyboardFocusManager;

/**
 * This class has the code for when a user hits a key in the main CrypSwing window.
 * @author Bitman
 * @version 1.0 07/21/19
 */
public class CrypSwingKeyEvent implements KeyListener {

	CrypSwingFrame gui;
	
	/**
	 * The contructor just gets a reference to the frame.
	 * @param in The GUI frame
	 */
	public CrypSwingKeyEvent(CrypSwingFrame in) {
		gui = in;
	}
	
	/**
	 * Given the name of a plaintext control, return the text in the corresponding ciphertext label.
	 * @param controlName The name of the control
	 * @return The text in the corresponding ciphertext label.
	 */
	private String getCiphertext(String controlName) {		
		int coordinatesStart = controlName.length() - 4;
		int letterIndex = Integer.valueOf(controlName.substring(coordinatesStart, coordinatesStart + 2));
		int lineIndex = Integer.valueOf(controlName.substring(coordinatesStart + 2, coordinatesStart + 4));
		return gui.ciphertext[letterIndex][lineIndex].getText();
	}
	
	/**
	 * All over the puzzle, translate the ciphertext character into the user's choice - or guess? Ha! - of plaintext.
	 * @param ciphertext The ciphertext character to translate
	 * @param plaintext The user's choice of plaintext translation
	 */
	private void translateEverywhere(String ciphertext, String plaintext) {
		int charIndex, lineIndex;
		char cipherChar = ciphertext.charAt(0);
		
		for (lineIndex = 0; lineIndex < gui.NUMBEROFLINES; lineIndex++)
			for (charIndex = 0; charIndex < gui.LINELENGTH; charIndex++)
				if (gui.ciphertext[charIndex][lineIndex].getText().charAt(0) == cipherChar)
					gui.plaintext[charIndex][lineIndex].setText(plaintext);
	}
	
	/**
	 * Move the cursor vertically if possible.
	 * @param currentControl The name of the control that's current.
	 * @param direction -1 means up; 1 means down.
	 */
	private void verticalMove (String currentControl, int direction) {
		// 
		
		int coordinatesStart = currentControl.length() - 4;
		int lineIndex = Integer.valueOf(currentControl.substring(coordinatesStart + 2, coordinatesStart + 4)) + direction;
		
		if (lineIndex < 0 || lineIndex >= gui.NUMBEROFLINES)
			return;
		
		int charIndex = Integer.valueOf(currentControl.substring(coordinatesStart, coordinatesStart + 2));
		
		gui.plaintext[charIndex][lineIndex].requestFocus();
		// (If it's not focusable, requestFocus() will do nothing.)
	}
	
	/**
	 * The event that gets triggered when the user presses a key but hasn't released it yet.
	 */
	public void keyPressed(KeyEvent event) {}
	/**
	 * The event that gets triggered when a user presses a key.
	 */
	public void keyTyped(KeyEvent event) {}
	
	/**
	 * The event that gets triggered when the user releases a key.
	 */
	public void keyReleased(KeyEvent event) {
		char inputChar;
		String inputString;
		int inputAsInt;

		inputAsInt = event.getKeyCode();
		inputString = Character.toString(event.getKeyChar()).toUpperCase();
		inputChar = inputString.charAt(0);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		
		if ((inputChar >= 'A' && inputChar <= 'Z') || inputChar == ' ') {
			translateEverywhere(getCiphertext(event.getComponent().getName()), inputString);
			manager.focusNextComponent();
		}
		else if (inputAsInt == KeyEvent.VK_SHIFT || inputAsInt == KeyEvent.VK_CAPS_LOCK);
		// Do nothing.
		else if (inputAsInt == KeyEvent.VK_LEFT)
			manager.focusPreviousComponent();
		else if (inputAsInt == KeyEvent.VK_RIGHT)
			manager.focusNextComponent();
		else if (inputAsInt == KeyEvent.VK_HOME)
			gui.plaintext[0][0].requestFocus();
		else if (inputAsInt == KeyEvent.VK_BACK_SPACE) {
			translateEverywhere (getCiphertext(event.getComponent().getName()), "");
			manager.focusPreviousComponent();
		}
		else if (inputAsInt == KeyEvent.VK_DELETE) {
			translateEverywhere (getCiphertext(event.getComponent().getName()), "");
			manager.focusNextComponent();
		}
		else if (inputAsInt == KeyEvent.VK_DOWN)
			verticalMove(event.getComponent().getName(), 1);
		else if (inputAsInt == KeyEvent.VK_UP)
			verticalMove(event.getComponent().getName(), -1);
		else
			((JTextField) event.getSource()).setText("");
	}

}
