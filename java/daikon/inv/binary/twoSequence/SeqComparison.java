package daikon.inv.binary.twoSequence;

import daikon.*;
import daikon.inv.*;
import daikon.inv.binary.twoScalar.*;

import utilMDE.*;

import java.util.*;

public class SeqComparison extends TwoSequence implements Comparison {

  static Comparator comparator = new ArraysMDE.LongArrayComparatorLexical();

  public final boolean only_check_eq;

  boolean can_be_eq = false;
  boolean can_be_lt = false;
  boolean can_be_gt = false;

  int num_sc_samples = 0;

  protected SeqComparison(PptSlice ppt, boolean only_eq) {
    super(ppt);
    only_check_eq = only_eq;
  }

  public static SeqComparison instantiate(PptSlice ppt) {
    // System.out.println("Ppt: " + ppt.name);
    // System.out.println("vars[0]: " + ppt.var_infos[0].type.format());
    // System.out.println("vars[1]: " + ppt.var_infos[1].type.format());
    ProglangType type1 = ppt.var_infos[0].type;
    ProglangType type2 = ppt.var_infos[1].type;
    boolean only_eq = (! ((type1.dimensions() == 1)
                          && type1.baseIsIntegral()
                          && (type2.dimensions() == 1)
                          && type2.baseIsIntegral()));
    return new SeqComparison(ppt, only_eq);
  }

  public String repr() {
    double probability = getProbability();
    return "SeqComparison(" + var1().name + "," + var2().name + "): "
      + "can_be_eq=" + can_be_eq
      + ",can_be_lt=" + can_be_lt
      + ",can_be_gt=" + can_be_gt
      + ",only_check_eq=" + only_check_eq
      + "; probability = " + probability;
  }

  public String format() {
    // System.out.println("Calling SeqComparison.format for: " + repr());
    String comparator = IntComparisonCore.format_comparator(can_be_lt, can_be_eq, can_be_gt);
    return var1().name + " " + comparator + " " + var2().name
      + " (lexically)";
  }


  public void add_modified(long[] v1, long[] v2, int count) {
    /// This does not do the right thing; I really want to avoid comparisons
    /// if one is missing, but not if one is zero-length.
    // Don't make comparisons with empty arrays.
    if ((v1.length == 0) || (v2.length == 0)) {
      return;
    }
    num_sc_samples += count;

    int comparison = comparator.compare(v1, v2);
    // System.out.println("SeqComparison(" + var1().name + "," + var2().name + "): "
    //                    + "compare(" + ArraysMDE.toString(v1)
    //                    + ", " + ArraysMDE.toString(v2) + ") = " + comparison);
    if (comparison == 0)
      can_be_eq = true;
    else if (comparison < 0)
      can_be_lt = true;
    else
      can_be_gt = true;
    if ((can_be_lt && can_be_gt)
        || (only_check_eq && (can_be_lt || can_be_gt))) {
      destroy();
      return;
    }
  }

  protected double computeProbability() {
    if (no_invariant) {
      return Invariant.PROBABILITY_NEVER;
    } else if (can_be_lt || can_be_gt) {
      return Math.pow(.5, ppt.num_values());
    } else if (num_sc_samples == 0) {
      return Invariant.PROBABILITY_UNKNOWN;
    } else {
      return Invariant.PROBABILITY_JUSTIFIED;
    }
  }

  // For Comparison interface
  public double eq_probability() {
    if (can_be_eq && (!can_be_lt) && (!can_be_gt))
      return computeProbability();
    else
      return Invariant.PROBABILITY_NEVER;
  }

  public boolean isSameFormula(Invariant o)
  {
    SeqComparison other = (SeqComparison) o;
    return
      (can_be_eq == other.can_be_eq) &&
      (can_be_lt == other.can_be_lt) &&
      (can_be_gt == other.can_be_gt);
  }

  public boolean isExclusiveFormula(Invariant o)
  {
    if (o instanceof SeqComparison) {
      SeqComparison other = (SeqComparison) o;
      return (! ((can_be_eq && other.can_be_eq)
                 || (can_be_lt && other.can_be_lt)
                 || (can_be_gt && other.can_be_gt)));
    }
    return false;
  }

}
