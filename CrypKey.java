package crypSwing;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an entire key:
 * the many known ciphertext characters and their plaintext.
 * 
 * @author Aaron Bitman
 * @version 1.0 03/20/19
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
	 * @return <code>true</code> if the <code>plainText</code> conforms to the key
	 *         <code>false</code> if the <code>plainText</code> does not conform to the key

	 */
	boolean conforms (String plainText) {
		
		for (CrypKeyEntry temp : keyEntries) {
			if (!temp.conforms(plainText))
				return false;
		}
		return true;
	}
}
