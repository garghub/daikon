package daikon.derive.binary;

import daikon.*;
import daikon.inv.binary.twoScalar.*; // for IntComparison
import daikon.inv.unary.scalar.*; // for LowerBound

import utilMDE.*;
import java.util.*;

// *****
// Do not edit this file directly:
// it is automatically generated from SequenceSubscriptFactory.java.jpp
// *****

// This controls derivations which use the scalar as an index into the
// sequence, such as getting the element at that index or a subsequence up
// to that index.

public final class SequenceScalarSubscriptFactory  extends BinaryDerivationFactory {

  // When calling/creating the derivations, arrange that:
  //   base1 is the sequence
  //   base2 is the scalar

  public BinaryDerivation[] instantiate(VarInfo vi1, VarInfo vi2) {

    // check if the derivations are globally disabled
    boolean enable_subscript = SequenceScalarSubscript.dkconfig_enabled;
    boolean enable_subsequence = SequenceScalarSubsequence.dkconfig_enabled;
    if (!enable_subscript && !enable_subsequence) {
      return null;
    }

    // This is not the very most efficient way to do this, but at least it is
    // comprehensible.
    VarInfo seqvar;
    VarInfo sclvar;

    if ((vi1.rep_type == ProglangType.INT_ARRAY )
        && (vi2.rep_type == ProglangType.INT )) {
      seqvar = vi1;
      sclvar = vi2;
    } else if ((vi2.rep_type == ProglangType.INT_ARRAY )
               && (vi1.rep_type == ProglangType.INT )) {
      seqvar = vi2;
      sclvar = vi1;
    } else {
      return null;
    }

    if (!seqvar.aux.getFlag(VarInfoAux.HAS_ORDER)) {
      // Indexing doesn't make sense if order doesn't matter
      return null;
    }

    // Assert.assert(sclvar.isCanonical()); // [INCR]
    // Assert.assert(seqvar.isCanonical()); // [INCR]

    if (! seqvar.indexCompatible(sclvar))
      return null;

    // For now, do nothing if the sequence is itself derived.
    if (seqvar.derived != null)
      return null;
    // For now, do nothing if the scalar is itself derived.
    if (sclvar.derived != null)
      return null;

    VarInfo seqsize = seqvar.sequenceSize();
    /* [INCR]
    if (seqsize != null) {
      seqsize = seqsize.canonicalRep();
    }
    */
    // System.out.println("BinaryDerivation.instantiate: sclvar=" + sclvar.name
    //                    + ", sclvar_rep=" + sclvar.canonicalRep().name
    //                    + ", seqsize=" + seqsize.name
    //                    + ", seqsize_rep=" + seqsize.canonicalRep().name);

    // SUPPRESS DERIVED VARIABLE: a[i] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[..i] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[i..] where i == a.length
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where i == a.length
    // Since both are canonical, this is equivalent to
    // "if (sclvar.canonicalRep() == seqsize.canonicalRep()) ..."
    if (sclvar == seqsize) {
      // a[len] a[len-1] a[0..len] a[0..len-1] a[len..] a[len+1..]
      Global.tautological_suppressed_derived_variables += 6;
      return null;
    }

    // SUPPRESS DERIVED VARIABLE: a[i] where (i >= a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[i-1] where (i > a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[..i] where (i >= a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where (i > a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[i..] where (i > a.length) can be true
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where (i >= a.length) can be true
    // ***** This eliminates the derivation if it can *ever* be
    // nonsensical/missing.  Is that what I want?
    // Find an IntComparison relationship over the scalar and the sequence
    // size, if possible.
    PptSlice compar_slice = null;
    if (seqsize != null) {
      Assert.assert(sclvar.ppt == seqsize.ppt);
      compar_slice = sclvar.ppt.findSlice_unordered(sclvar, seqsize);
    }
    if (compar_slice != null) {
      if ((sclvar.varinfo_index < seqsize.varinfo_index)
          ? IntLessEqual.find(compar_slice) == null // sclvar can be more than seqsize
          : IntGreaterEqual.find(compar_slice) == null // seqsize can be less than sclvar
          ) {
        Global.nonsensical_suppressed_derived_variables += 6;
        return null;
      } else if (IntEqual.find(compar_slice) != null) {
        Global.nonsensical_suppressed_derived_variables += 3;
        ArrayList result = new ArrayList();
        if (enable_subscript) {
          result.add(new SequenceScalarSubscript (seqvar, sclvar, true)); // a[i-1]
        }
        if (enable_subsequence) {
          result.add(new SequenceScalarSubsequence (seqvar, sclvar, true, true)); // a[..i-1]
          result.add(new SequenceScalarSubsequence (seqvar, sclvar, false, false)); // a[i..]
        };
        return (BinaryDerivation[]) result.toArray(new BinaryDerivation[result.size()]);
      }
    }

    // Abstract out these next two.

    // If the scalar is a constant, then do all the following checks:
    //
    // If the scalar is a constant < 0:
    //   all derived variables are nonsensical
    // SUPPRESS DERIVED VARIABLE: a[i] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[..i] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[i..] where i<0 and i is constant
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where i<0 and i is constant
    // If the scalar is the constant 0:
    //   array[0] is already extracted
    //   array[-1] is nonsensical
    //   array[0..0] is already extracted
    //   array[0..-1] is nonsensical
    //   array[0..] is the same as array[]
    //   array[1..] should be extracted
    // SUPPRESS DERIVED VARIABLE: a[i] where i==0
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i==0
    // SUPPRESS DERIVED VARIABLE: a[..i] where i==0
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i==0
    // SUPPRESS DERIVED VARIABLE: a[i..] where i==0
    // If the scalar is the constant 1:
    //   array[1] is already extracted
    //   array[0] is already extracted
    //   array[0..1] should be extracted
    //   array[0..0] is already extracted
    //   array[1..] should be extracted
    //   array[2..] should be extracted
    // SUPPRESS DERIVED VARIABLE: a[i] where i==1
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i==1
    // SUPPRESS DERIVED VARIABLE: a[..i] where i==1
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i==1
    /* [INCR] ...
    if (sclvar.isConstant()) {
      long scl_constant = ((Long) sclvar.constantValue()).longValue();
      // System.out.println("It is constant (" + scl_constant + "): " + sclvar.name);
      if (scl_constant < 0) {
        Global.nonsensical_suppressed_derived_variables += 6;
	return null;
      }
      if (scl_constant == 0) {
        Global.tautological_suppressed_derived_variables += 3;
        Global.nonsensical_suppressed_derived_variables += 2;
        return (enable_subsequence) ? new BinaryDerivation[] {
          new SequenceScalarSubsequence (seqvar, sclvar, false, true),
        } : null;
      }
      if (scl_constant == 1) {
        Global.tautological_suppressed_derived_variables += 3;
        return (enable_subsequence) ? new BinaryDerivation[] {
          new SequenceScalarSubsequence (seqvar, sclvar, true, false),
          new SequenceScalarSubsequence (seqvar, sclvar, false, false),
          new SequenceScalarSubsequence (seqvar, sclvar, false, true),
	} : null;
      }
    }
    */ // ... [INCR]

    // Commented out:  seems to be eliminating desired invariants.
    if (false) {
    // If the lower bound for the variable is less than 0 (that is, if the
    // putative index can ever be negative), then suppress most of the
    // derived variables.
    // SUPPRESS DERIVED VARIABLE: a[i] where i<0 can be true
    // SUPPRESS DERIVED VARIABLE: a[i-1] where i<1 can be true
    // SUPPRESS DERIVED VARIABLE: a[..i] where i<-1 can be true
    // SUPPRESS DERIVED VARIABLE: a[..i-1] where i<0 can be true
    // SUPPRESS DERIVED VARIABLE: a[i..] where i<0 can be true
    // SUPPRESS DERIVED VARIABLE: a[i+1..] where i<-1 can be true
    PptSlice unary_slice = sclvar.ppt.findSlice(sclvar);
    if (unary_slice != null) {
      LowerBound lb_inv = LowerBound.find(unary_slice);
      if (lb_inv != null) {
        long lower_bound = lb_inv.core.min1;
        if (lower_bound < -1) {
          Global.nonsensical_suppressed_derived_variables += 6;
          return null;
        } else if (lower_bound == -1) {
          Global.nonsensical_suppressed_derived_variables += 5;
          return (enable_subsequence) ? new BinaryDerivation[] {
            new SequenceScalarSubsequence (seqvar, sclvar, true, false), // a[..i]
            new SequenceScalarSubsequence (seqvar, sclvar, false, true), // a[i+1..]
          } : null;
        } else if (lower_bound == 0) {
          Global.nonsensical_suppressed_derived_variables += 1;
	  ArrayList result = new ArrayList();
	  if (enable_subscript) {
            result.add(new SequenceScalarSubscript (seqvar, sclvar, false)); // a[i]
	  }
	  if (enable_subsequence) {
            result.add(new SequenceScalarSubsequence (seqvar, sclvar, false, false)); // a[i..]
            result.add(new SequenceScalarSubsequence (seqvar, sclvar, false, true)); // a[i+1..]
            result.add(new SequenceScalarSubsequence (seqvar, sclvar, true, false)); // a[..i]
            result.add(new SequenceScalarSubsequence (seqvar, sclvar, true, true)); // a[..i-1]
	  };
	  return (BinaryDerivation[]) result.toArray(new BinaryDerivation[result.size()]);
	}
      }
    }
    }

    // If, for some canonical j, j=index+1, don't create array[index+1..].
    // If j=index-1, then don't create array[index-1] or array[0..index-1].
    // (j can have higher or lower VarInfo index than i.)
    boolean suppress_minus_1 = false;
    boolean suppress_plus_1 = false;

    // This ought to be abstracted out, maybe
    {
      Assert.assert(sclvar.ppt == seqvar.ppt);
      Vector lbs = LinearBinary.findAll(sclvar);
      // System.out.println("For " + sclvar.name + ", " + lbs.size() + " LinearBinary invariants");
      for (int i=0; i<lbs.size(); i++) {
        LinearBinary lb = (LinearBinary) lbs.elementAt(i);
        if (lb.core.a == 1) {
          // Don't set unconditionally, and don't break:  we want to check
          // other variables as well.
          if (lb.core.b == -1) {
            if (lb.var1() == sclvar)
              suppress_minus_1 = true;
            else
              suppress_plus_1 = true;
          }
          if (lb.core.b == 1) {
            if (lb.var1() == sclvar)
              suppress_minus_1 = true;
            else
              suppress_plus_1 = true;
          }
          // System.out.println("For " + sclvar.name + " suppression: "
          //                    + "minus=" + suppress_minus_1
          //                    + " plus=" + suppress_plus_1
          //                    + " because of " + lb.format());
        }
      }
    }

    if (suppress_minus_1) {
      Global.tautological_suppressed_derived_variables += 2;
    }
    if (suppress_plus_1) {
      Global.tautological_suppressed_derived_variables += 1;
    }

    // End of applicability tests; now actually create the invariants

    Vector result = new Vector(6);
    if (enable_subscript) {
      // a[i]
      result.add(new SequenceScalarSubscript (seqvar, sclvar, false));
      // a[i-1]
      if (! suppress_minus_1)
	result.add(new SequenceScalarSubscript (seqvar, sclvar, true));
    }
    if (enable_subsequence) {
      // a[i..]
      result.add(new SequenceScalarSubsequence (seqvar, sclvar, false, false));
      // a[i+1..]
      if (! suppress_plus_1)
	result.add(new SequenceScalarSubsequence (seqvar, sclvar, false, true));
      // a[..i]
      result.add(new SequenceScalarSubsequence (seqvar, sclvar, true, false));
      // a[..i-1]
      if (! suppress_minus_1)
	result.add(new SequenceScalarSubsequence (seqvar, sclvar, true, true));
    }
    return (BinaryDerivation[]) result.toArray(new BinaryDerivation[0]);
  }

}
