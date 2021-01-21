package crypSwing;

/**
 * This class represents one entry in a "known" key,
 * the knowledge that one ciphertext character represents one plaintext character.
 * e.g. the second character is a plaintext E.
 * @author Aaron Bitman
 * @version 1.0 03/20/19
 * @version 2.0 01/20/21
 */

public class CrypKeyEntry {

	/**
	 * The index in the String.
	 * For example, the 1st character would be 0, the 2nd character would be 1, etc.
	 */
	private int index;
	/**
	 * What that "index" character is in plaintext
	 */
	private char plainText;

	/**
	 * Constructs one "null" entry in the key.
	 */
	CrypKeyEntry () {

		this.index = -1;
		this.plainText = ' ';
	}
	
	/**
	 * Gets the index
	 * @return The index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the plaintext character
	 * @return the plaintext character
	 */
	public char getPlainText() {
		return plainText;
	}

	/**
	 * Sets one entry in the key.
	 * @param index     The index; see the "index" member for further clarification.
	 * @param plainText What that index character is in plaintext.
	 */
	void setCrypKeyEntry (int index, char plainText) {
		
		this.index = index;
		this.plainText = plainText;
	}

	/**
	 * Determines whether a plaintext word conforms to the key entry.
	 * @param plainText
	 * @return true if the word conforms, false otherwise
	 */
	boolean conforms (String plainText) {
		return plainText.charAt(index) == this.plainText;
	}

	/**
	 * Determines if the entry is valid.
	 * @return true if the user entered a real key entry, false if (s)he quit entering entries
	 */
	boolean isValid () {
		return index > -1;
	}
}
