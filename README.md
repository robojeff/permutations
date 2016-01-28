# permutations
The purpose of this code sample is to demonstrate my though process when using a test-driven approach to writing software.

#####The Problem

Given a word generate all the permutations of that word.

#####The Three Laws of Test Driven Development (TDD)

1. Write no production code except to pass a failing test.
2. Write only enough of a test to demonstrate a failure (and not compiling is a failure).
3. Write only enough production code to pass the currently failing test.

There are excellent books and videos on this methodology by Robert C. Martin and Roy Osherove.

#####Start with the degenerate test cases

The test cases are in the source file WordPermutatorTest.java. I start by solving for all obvious degenerate test cases  such as null and empty inputs as well as any other conditions that need to be rejected with an exception:

1. A null string shall return the empty set
2. An empty string shall return the empty set
3. A multi-word string shall be rejected

As each test is implemented I look for refactoring opportunities.  With these test cases done my test class looks like so:

<pre>
<code>
public class WordPermutatorTest {

  private Set<String> permutations;

  private void permute(String word) {
    if (word == null || word.isEmpty())
      permutations = Collections.emptySet();

    if (Pattern.compile("\\s").matcher(word).find())
      throw new IllegalArgumentException(word);
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
    
  @Test(expected = IllegalArgumentException.class)
  public void permutationsOf_multiWordString_isNotSupported() {
    permute("String  containing  spaces");
  }

}
</code>
</pre>

#####Solve the primary test cases

With the degenerate tests cases covered I begin to tackle the actual problem starting simplest test case I can think of:

1. The permutations of "A" is the set {"A"}
2. The permutations of "AB" is the set {"BA", "AB"}
3. The permutations of "ABC" is the set {"CBA", "BCA", "BAC", "CAB", "ACB", "ABC"}


There's a pattern here: recursively find permutations of the string with its last character removed.

With these four test cases working I refactored the permutation code into the WordPermutator class.  I also decided to throw a more descriptive exception type if a multi-word string was passed to the permute() method.  The low level private methods are omitted here for brevity:

<pre>
<code>
public class WordPermutator {

    public static Set<String> permute(String word) throws MultiWordStringNotSupportedException {
      if (word == null || word.isEmpty())
        return Collections.emptySet();
    
      if (containsMultipleWords(word))
        throw new MultiWordStringNotSupportedException(word);
    
      return permutationsOf(word);
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
          permutations.add(wordWithLastCharAt(position, lastChar, permutation));
    
      return permutations;
    }
}
</code>
</pre>

I think this code reads pretty well.

#####Keeping the test code clean

Test code is every bit important as production code.  Given a set of tests that gave me 100% coverage (a lofty, but very noble goal) I should be able recreate the system from scratch using only my tests.  I can't easily recreate a system without a set of tests because how could I ever recall all the special cases or know when my program was functionally complete? I refactor my test code continuously just as I do with production code.

I also will generally use a test naming convention that describes the test in three parts:

1. The name of the method under test (or something that succinctly describes what functionality is being exercised)
2. The input condition
3. The asserted output condition

For example:

<pre>
<code>
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
</code>
</pre>

The test name is straight forward: The permutations of ABC is CBA,BCA,BAC,CAB,ACB, and ABC.

My test code follows the best practice of "Arrange, Act, (logical) Assert".  The first step of each test sets up the test inputs.  The second step interacts with the code under test.  Thirdly there are one or more related assertions.  I avoid as much as possible any type of logic within my tests as this hurts reading comprehension and may result in an increased cost of maintainability.

Test code that is easy to read is easy to review.  A nice byproduct of tests is that they provide a low-level API documentation to help other developers understand how to use the component under test.

####Conclusion

During my testing I discovered that my implementation was reasonably fast up to a string containing ten unique characters (10! or 3,628,800 permutations).  My i7 powered iMac can generate 10! strings in about 3 seconds.  However, a string with 11! (39,916,800) permutations exceeds the default JVM heap size after about 90 seconds.  To scale for very long strings a solution that minimizes heap storage is required.  To eliminate any possibility of stack overflow I'd investigate an iterative rather than recursive solution.  To scale for any size string I'd look into whether it were possible to keep intermediate results stored externally, possibly with something the reads and writes fast such as a memory mapped file.