package daikon.inv.unary.stringsequence;

import daikon.*;
import daikon.inv.*;

import utilMDE.*;

import java.util.*;

// *****
// Automatically generated from OneOf-cpp.java
// *****

// States that the value is one of the specified values.

// This subsumes an "exact" invariant that says the value is always exactly
// a specific value.  Do I want to make that a separate invariant
// nonetheless?  Probably not, as this will simplify implication and such.

public final class EltOneOfString  extends SingleStringSequence  implements OneOf {
  final static int LIMIT = 5;	// maximum size for the one_of list
  // Probably needs to keep its own list of the values, and number of each seen.
  // (That depends on the slice; maybe not until the slice is cleared out.
  // But so few values is cheap, so this is quite fine for now and long-term.)

  private String [] elts;
  private int num_elts;

  EltOneOfString (PptSlice ppt) {
    super(ppt);

    elts = new String [LIMIT];

    num_elts = 0;

  }

  public static EltOneOfString  instantiate(PptSlice ppt) {
    return new EltOneOfString (ppt);
  }

  public int num_elts() {
    return num_elts;
  }

  public Object elt() {
    if (num_elts != 1)
      throw new Error("Represents " + num_elts + " elements");

    return elts[0];

  }

  private void sort_rep()
  {
    Arrays.sort(elts, 0, num_elts  );
  }

  private String subarray_rep() {
    // Not so efficient an implementation, but simple;
    // and how often will we need to print this anyway?
    sort_rep();
    StringBuffer sb = new StringBuffer();
    sb.append("{ ");
    for (int i=0; i<num_elts; i++) {
      if (i != 0)
        sb.append(", ");
      sb.append("\"" + UtilMDE.quote( elts[i] ) + "\"" );
    }
    sb.append(" }");
    return sb.toString();
  }

  public String repr() {
    double probability = getProbability();
    return "EltOneOfString(" + var().name + "): "
      + "no_invariant=" + no_invariant
      + ", num_elts=" + num_elts
      + ", elts=" + subarray_rep();
  }

  public String format() {
    if (no_invariant || (num_elts == 0) || (! justified()))
      return null;
    if (num_elts == 1) {

      return var().name + " elements = \"" + UtilMDE.quote( elts[0] ) + "\"" ;

    } else {
      return var().name + " elements one of " + subarray_rep();
    }
  }

  public void add_modified(String[] a, int count) {
    for (int ai=0; ai<a.length; ai++) {
      String v = a[ai];

    for (int i=0; i<num_elts; i++)
      if (elts[i] == v)
        return;
    if (num_elts == LIMIT) {
      destroy();
      return;
    }

    elts[num_elts] = v;
    num_elts++;

    }
  }

  protected double computeProbability() {
    // This is wrong; fix it
    return Invariant.PROBABILITY_JUSTIFIED;
  }

  public boolean isSameFormula(Invariant o)
  {
    EltOneOfString  other = (EltOneOfString ) o;    
    if (elts.length != other.elts.length)
      return false;

    sort_rep();
    other.sort_rep();
    for (int i=0; i < elts.length; i++)
      if (elts[i] != other.elts[i]) // elements are interned
	return false;

    return true;
  }
  
}
