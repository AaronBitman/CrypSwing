package crypSwing;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an entire key:
 * the many known ciphertext characters and their plaintext.
 * 
 * @author Aaron Bitman
 * @version 1.0 03/20/19
 * @version 2.0 01/20/21
 */

public class CrypKey {

	/**
	 * Holds a list of known-key entries
	 */
	List <CrypKeyEntry> keyEntries = new ArrayList<>();

	/**
	 * Adds an entry to the key.
	 * @param entry The entry to enter into the key
	 */
	void addEntry (CrypKeyEntry entry) {
		CrypKeyEntry localEntry = new CrypKeyEntry();

		localEntry.setCrypKeyEntry(entry.getIndex(), entry.getPlainText());
		keyEntries.add(localEntry);
	}

	/**
	 * Determines whether a plaintext word conforms to the key.
	 * @param plainText The plaintext word
	 * @return true if the plaintext conforms to the key, false otherwise
	 */
	boolean conforms (String plainText) {
		
		for (CrypKeyEntry temp : keyEntries) {
			if (!temp.conforms(plainText))
				return false;
		}
		return true;
	}
}
