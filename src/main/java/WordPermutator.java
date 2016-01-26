import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class WordPermutator {

  /**
   * Limits the number of generated permutations to conserve memory
   */
  static final int MAX_PERMUTATIONS = 3628800;

  /**
   * Generates a set of string permutations
   *
   * @param word any string containing a single word - that is, a string containing no spaces
   * @return the permutations of the specified string, up to a maximum of 10! results
   * @throws MultiWordStringNotSupportedException if the specified word contains space characters
   */
  public static Set<String> permute(String word) throws MultiWordStringNotSupportedException {
    if (word == null || word.isEmpty())
      return Collections.emptySet();

    if (containsMultipleWords(word))
      throw new MultiWordStringNotSupportedException(word);

    return permutationsOf(word);
  }

  private static boolean containsMultipleWords(String word) {
    return Pattern.compile("\\s").matcher(word).find();
  }

  private static Set<String> permutationsOf(String word) {
    Set<String> permutations = new HashSet<>();

    if (word.length() == 1) {
      permutations.add(word);
      return permutations;
    }

    char lastChar = word.charAt(word.length() - 1);

    for (String permutation : permutationsOf(charsBefore(lastChar, word)))
      for (int position = 0; position <= permutation.length(); position++)
        if (permutations.size() < MAX_PERMUTATIONS)
          permutations.add(wordWithLastCharAt(position, lastChar, permutation));

    return permutations;
  }

  private static String charsBefore(char c, String s) {
    return s.substring(0, s.lastIndexOf(c));
  }

  private static String wordWithLastCharAt(int position, char lastChar, String word) {
    return word.substring(0, position) + lastChar + word.substring(position, word.length());
  }

  static class MultiWordStringNotSupportedException extends IllegalArgumentException {
    MultiWordStringNotSupportedException(String message) {
      super(message);
    }
  }

}