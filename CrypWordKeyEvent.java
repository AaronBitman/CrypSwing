package crypSwing;

import javax.swing.*;
import java.awt.event.*;
import java.awt.KeyboardFocusManager;

/**
 * This class has all the code for what to do when a user hits a key in the word-solving GUI.
 * @author Bitman
 * @version 1.0 07/21/19
 */
public class CrypWordKeyEvent implements KeyListener {

	CrypWordFrame gui;
	
	/**
	 * The constructor gets a reference to the GUI.
	 * @param in The GUI
	 */
	public CrypWordKeyEvent(CrypWordFrame in) {
		gui = in;
	}
	
	/**
	 * Determines the actual ciphertext word for which the program will search.
	 * @return The ciphertext word
	 */
	private String actualCiphertextWord() {
		char ciphertextChar;
		int index = 0;
		String fieldContent, temp = "";
		
		for (index=0; index < gui.MAXWORDLENGTH; index++) {
			fieldContent = gui.ciphertext[index].getText();
			if (fieldContent.isEmpty())
				return temp;
			ciphertextChar = fieldContent.charAt(0);
			if (!charValid(ciphertextChar))
				return temp;
			temp += ciphertextChar;
		}
		
		return temp;
	}
	
	/**
	 * Determines if a character is valid in this kind of puzzle.
	 * @param c The character
	 * @return true if valid; false if not
	 */
	private boolean charValid (char c) {
		return ((c == '\'') || (c >= 'A' && c <= 'Z') || (c == '-'));
	}
	
	/**
	 * This method determines what to do if the user presses, but hasn't yet released, a key.
	 *  (It's really just to allow the user to hold down the backspace and delete a lot.)
	 */
	public void keyPressed(KeyEvent event) {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

		if (Character.toString(event.getKeyChar()).charAt(0) == '\b'
				&& !event.getComponent().getName().endsWith("text0"))
			manager.focusPreviousComponent();
	}

	/**
	 * This method is for what to do when a user types a key.
	 */
	public void keyTyped(KeyEvent event) {}
	
	/**
	 * This method determines what to do when the user releases a key.
	 */
	public void keyReleased(KeyEvent event) {
		boolean ciphertextIsCurrent;
		char inputChar;
		String inputString;
		int inputAsInt;
		
		ciphertextIsCurrent = event.getComponent().getName().startsWith("ciphertext");
		inputAsInt = event.getKeyCode();
		inputString = Character.toString(event.getKeyChar()).toUpperCase();
		inputChar = inputString.charAt(0);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		
		if (charValid(inputChar)) {
			((JTextField) event.getSource()).setText(inputString);
			manager.focusNextComponent();
		}
		else if (inputAsInt == KeyEvent.VK_SHIFT || inputAsInt == KeyEvent.VK_CAPS_LOCK);
		// Do nothing.
		else if (inputAsInt == KeyEvent.VK_LEFT)
			manager.focusPreviousComponent();
		else if (inputAsInt == KeyEvent.VK_RIGHT)
			manager.focusNextComponent();
		else if (inputAsInt == KeyEvent.VK_UP || inputAsInt == KeyEvent.VK_DOWN) {
			if (ciphertextIsCurrent)
				gui.plaintext[Integer.parseInt(event.getComponent().getName().substring(10))].requestFocus();
			else // Presumably, plaintext is current
				gui.ciphertext[Integer.parseInt(event.getComponent().getName().substring(9))].requestFocus();
		}
		else if (inputAsInt == KeyEvent.VK_HOME)
			if (ciphertextIsCurrent)
				gui.ciphertext[0].requestFocus();
			else // Presumably, plaintext is current
				gui.plaintext[0].requestFocus();
		//This next bit is unnecessary now that we made the Solve button the default.
		//else if (inputAsInt == KeyEvent.VK_ENTER) {
		//	ActionEvent ae = 
			//gui.CrypWordBtnActions.actionPerformed(gui.CrypWordBtnActions.);
		//}
		else
		{
			((JTextField) event.getSource()).setText("");
			if (inputChar == ' ')
				manager.focusNextComponent();
		}
				
		if (ciphertextIsCurrent)
			gui.actualCiphertextWord.setText(actualCiphertextWord());
	}
}
