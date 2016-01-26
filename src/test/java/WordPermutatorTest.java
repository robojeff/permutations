import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WordPermutatorTest {

  private Set<String> permutations;

  private void permute(String word) {
    permutations = WordPermutator.permute(word);
  }

  private void assertNumberOfPermutations(int expected) {
    assertThat(permutations.size(), is(equalTo(expected)));
  }

  private void assertPermutationsContains(String expected) {
    assertThat(permutations.contains(expected), is(true));
  }

  @Test
  public void permutationsOf_nullString_isEmptySet() {
    permute(null);

    assertNumberOfPermutations(0);
  }

  @Test
  public void permutationsOf_emptyString_isEmptySet() {
    permute("");

    assertNumberOfPermutations(0);
  }

  @Test(expected = WordPermutator.MultiWordStringNotSupportedException.class)
  public void permutationsOf_multiWordString_isNotSupported() {
    permute("String  containing  spaces");
  }

  @Test
  public void permutationsOf_A_isA() {
    permute("A");

    assertNumberOfPermutations(1);
    assertPermutationsContains("A");
  }

  @Test
  public void permutationsOf_AA_isAA() {
    permute("AA");

    assertNumberOfPermutations(1);
    assertPermutationsContains("AA");
  }

  @Test
  public void permutationsOf_AB_isBA_AB() {
    permute("AB");

    assertNumberOfPermutations(2);
    assertPermutationsContains("BA");
    assertPermutationsContains("AB");
  }

  @Test
  public void permutationsOf_ABC_isCBA_BCA_BAC_CAB_ACB_ABC() {
    permute("ABC");

    assertNumberOfPermutations(6);
    assertPermutationsContains("CBA");
    assertPermutationsContains("BCA");
    assertPermutationsContains("BAC");
    assertPermutationsContains("CAB");
    assertPermutationsContains("ACB");
    assertPermutationsContains("ABC");
  }

  @Test
  public void permutationsOf_toffee_returns180Permutations() {
    permute("toffee");

    assertNumberOfPermutations(180); // 6! / (2!*2!)
  }

  @Test
  public void permutationsOf_mississippi_returns34650Permutations() {
    permute("mississippi");

    assertNumberOfPermutations(34650); // 11! / (4!*4!*2!)
  }

  @Test
  public void permutationsOf_tenUniqueCharacters_returnsMaxNumberOfPermutations() {
    permute("abcdefghij");

    assertNumberOfPermutations(WordPermutator.MAX_PERMUTATIONS); // 10!
  }

  @Test
  public void permutationsOf_elevenUniqueCharacters_returnsMaxNumberOfPermutations() {
    permute("abcdefghijk");

    assertNumberOfPermutations(WordPermutator.MAX_PERMUTATIONS); // 11! limited to 10!
  }
}