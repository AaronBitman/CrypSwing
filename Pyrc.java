package crypSwing;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * This self-contained program generates a cryptogram.
 * @author Bitman
 * @version 2.0 01/20/21
 */
public class Pyrc {

	/**
	 * The main method does the whole job.
	 * @param args not used
	 */
	public static void main(String[] args) {
		final String plaintext = "My wife's jealousy is getting ridiculous. The other day she "
				+ "looked at my calendar and wanted to know who May was. (Rodney Dangerfield)";
		String temp = "";
		List<Character> alphabetList = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M',
								'N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
		Collections.shuffle(alphabetList);
		for (char ch: plaintext.toUpperCase().toCharArray())
			if (ch < 'A' || ch > 'Z') temp += ch;
			else temp += alphabetList.get(((int) ch) - 64);
		System.out.println(temp);
	}
}
