// ***** This file is automatically generated from Member.java.jpp

package daikon.inv.binary.sequenceScalar;

import daikon.*;
import daikon.inv.*;
import daikon.inv.binary.twoScalar.*;
import daikon.inv.binary.twoSequence.*;
import daikon.inv.binary.twoScalar.IntLessThan;
import daikon.inv.binary.twoScalar.IntGreaterThan;
import daikon.inv.binary.twoScalar.IntLessEqual;
import daikon.inv.binary.twoScalar.IntGreaterEqual;
import daikon.derive.*;
import daikon.derive.unary.*;
import daikon.derive.binary.*;
import daikon.suppress.*;
import java.util.*;
import utilMDE.*;
import org.apache.log4j.Category;

public final class MemberFloat
  extends SequenceFloat
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20020122L;

  public static final Category debug =
    Category.getInstance ("daikon.inv.binary.Member");

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /**
   * Boolean.  True iff Member invariants should be considered.
   **/
  public static boolean dkconfig_enabled = true;

  protected MemberFloat (PptSlice ppt, boolean seq_first) {
    super(ppt, seq_first);
    Assert.assertTrue(sclvar().rep_type == ProglangType.DOUBLE);
    Assert.assertTrue(seqvar().rep_type == ProglangType.DOUBLE_ARRAY);
  }

  // This constructor enables testing with InvariantFormatTester.
  public static MemberFloat  instantiate(PptSlice ppt) {
    return instantiate(ppt,true);
  }

  public static MemberFloat  instantiate(PptSlice ppt, boolean seq_first) {
    if (!dkconfig_enabled) return null;

    VarInfo seqvar = ppt.var_infos[seq_first ? 0 : 1];
    VarInfo sclvar = ppt.var_infos[seq_first ? 1 : 0];

    // SUPPRESSED INVARIANT: Member, if isEqualToObviousMember (complicated)
    // [INCR] for now, don't use equalTo() information.
    // if (isEqualToObviousMember(sclvar, seqvar)) {
    if (isObviousMember(sclvar, seqvar)) {
      Global.implied_noninstantiated_invariants += 1;
      if (debug.isDebugEnabled()) {
        debug.debug ("Member not instantiated (obvious): "
                     + sclvar.name + " in " + seqvar.name);
      }
      return null;
    }

    if (debug.isDebugEnabled()) {
      debug.debug ("Member instantiated: "
                   + sclvar.name + " in " + seqvar.name);
    }
    return new MemberFloat (ppt, seq_first);
  }

  public boolean isObviousImplied() {
    return isEqualToObviousMember(sclvar(), seqvar());
  }

  // Like isObviousMember, but also checks everything equal to the given
  // variables.
  public static boolean isEqualToObviousMember(VarInfo sclvar, VarInfo seqvar) {
    /* [INCR]
    Assert.assertTrue(sclvar.isCanonical());
    Assert.assertTrue(seqvar.isCanonical());
    Vector scl_equalto = sclvar.equalTo();
    scl_equalto.add(0, sclvar);
    Vector seq_equalto = seqvar.equalTo();
    seq_equalto.add(0, seqvar);

    for (int sclidx=0; sclidx<scl_equalto.size(); sclidx++) {
      for (int seqidx=0; seqidx<seq_equalto.size(); seqidx++) {
        VarInfo this_sclvar = (VarInfo) scl_equalto.elementAt(sclidx);
        VarInfo this_seqvar = (VarInfo) seq_equalto.elementAt(seqidx);
        if (isObviousMember(this_sclvar, this_seqvar))
          return true;
      }
    }
    */ // [INCR]
    return false;
  }

  public static boolean isObviousMember(VarInfo sclvar, VarInfo seqvar) {

    VarInfo sclvar_seq = sclvar.isDerivedSequenceMember();
    // System.out.println("Member.isObviousMember(" + sclvar.name + ", " + seqvar.name + "):");
    // System.out.println("  sclvar.derived=" + sclvar.derived
    //                    + ", sclvar_seq=" + ((sclvar_seq == null) ? "null" : sclvar_seq.name));

    if (sclvar_seq == null) {
      // The scalar is not obviously (lexically) a member of any array.
      return false;
    }
    // isObviousImplied: a[i] in a; max(a) in a
    if (sclvar_seq == seqvar) {
      // The scalar is a member of the same array.
      return true;
    }
    // The scalar is a member of a different array than the sequence.
    // But maybe the relationship is still obvious, so keep checking.

    // isObviousImplied: when b==a[0..i]:  b[j] in a; max(b) in a
    // If the scalar is a member of a subsequence of the sequence, then
    // the scalar is a member of the full sequence.
    // This is satisfied, for instance, when determining that
    // max(B[0..I]) is an obvious member of B.
    VarInfo sclseqsuper = sclvar_seq.isDerivedSubSequenceOf();
    if (sclseqsuper == seqvar)
      return true;

    // We know the scalar was derived from some array, but not from the
    // sequence variable.  If also not from what the sequence variable was
    // derived from, we don't know anything about membership.
    // Check:
    //  * whether comparing B[I] to B[0..J]
    //  * whether comparing min(B[0..I]) to B[0..J]
    VarInfo seqvar_super = seqvar.isDerivedSubSequenceOf();
    if ((seqvar_super != sclvar_seq)
        && (seqvar_super != sclseqsuper)) {
      // System.out.println("Member.isObviousMember(" + sclvar.name + ", " + seqvar.name + "):"
      //                    + " isDerivedSubSequenceOf() != " + sclvar_seq.name);
      return false;
    }

    // If the scalar is a positional element of the sequence from which
    // the sequence at hand was derived, then any relationship will be
    // (mostly) obvious by comparing the length of the sequence to the
    // index.  By contrast, if the scalar is max(...) or min(...), all bets
    // are off.

    if (seqvar.derived instanceof SequenceFloatSubsequence) {
      // the sequence is B[0..J-1] or similar.  Get information about it.
      SequenceFloatSubsequence  seqsss = (SequenceFloatSubsequence) seqvar.derived;
      // System.out.println("seqvar: " + seqvar.name);
      VarInfo seq_index = seqsss.sclvar();
      int seq_shift = seqsss.index_shift;
      boolean seq_from_start = seqsss.from_start;

      if (sclvar.derived instanceof SequenceFloatSubscript) {
        // B[I] in B[0..J]

        SequenceFloatSubscript  sclsss = (SequenceFloatSubscript) sclvar.derived;
        VarInfo scl_index = sclsss.sclvar(); // "I" in "B[I]"
        int scl_shift = sclsss.index_shift;
        // System.out.println("scl_shift = " + scl_shift + ", seq_shift = " + seq_shift);
        // when b[i+d] in b[0..i+d+x] or b[i+d+x] in b[i+d..]
        if (scl_index == seq_index &&
            (seq_from_start ?
             seq_shift >= scl_shift :
             seq_shift <= scl_shift)
            ) {
          if (debug.isDebugEnabled()) {
            debug.debug ("IsObviousMember in ppt: " + sclvar.ppt.name);
            debug.debug ("  scl_index: " + scl_index.name.name());
            debug.debug ("  seq_index: " + seq_index.name.name());
            debug.debug ("  sclvar   : " + sclvar.name.name());
            debug.debug ("  seqvar   : " + seqvar.name.name());
          }
          return true;
        }

        // This test returns true if scl+scl_shift<=seq+seq_shift
        // isObviousImplied: when i<=j, b[i] in b[0..j]
        // isObviousImplied: when i>=j, b[i] in b[j..]
        if (VarInfo.compare_vars(scl_index, scl_shift, seq_index, seq_shift,
                                 seq_from_start)) {
          return true;
        }
      } else if (sclvar.derived instanceof SequenceInitialFloat) {
        // System.out.println("sclvar derived from SequenceInitial: " + sclvar.name);

        // isObviousImplied: B[0] in B[0..J]; also B[-1] in B[J..]
        SequenceInitialFloat  sclse = (SequenceInitialFloat) sclvar.derived;
        int scl_index = sclse.index;
        if (((scl_index == 0) && seq_from_start)
            || ((scl_index == -1) && !seq_from_start))
          // It might not be true, because the array could be empty;
          // but if the array isn't empty, then it's obvious.
          return true;
      } else if ((sclvar.derived instanceof SequenceMin)
                 || (sclvar.derived instanceof SequenceMax)) {
        if (sclvar_seq.derived instanceof SequenceFloatSubsequence) {
          // min(B[0..I]) in B[0..J]
          // System.out.println("seqvar_super = " + seqvar_super + ", sclseqsuper = " + sclseqsuper);
          // System.out.println("seqvar_super = " + seqvar_super.name + ", sclseqsuper = " + sclseqsuper.name);
          Assert.assertTrue(seqvar_super == sclseqsuper);
          SequenceFloatSubsequence  sclsss = (SequenceFloatSubsequence) sclvar_seq.derived;
          boolean scl_from_start = sclsss.from_start;
          if (scl_from_start == seq_from_start) {
            VarInfo scl_index = sclsss.sclvar();
            int scl_shift = sclsss.index_shift;
            boolean comparison = VarInfo.compare_vars(scl_index, scl_shift, seq_index, seq_shift,
                                                      seq_from_start);
            // System.out.println("comparison="+comparison+" for obvious membership: " + sclvar.name + " " + seqvar.name);
            // isObviousImplied: when i<=j, min(B[0..I]) in B[0..J]; also for max and B[j..0]
            if (comparison) {
              return true;
            }
          }
        }
      }
    }

    /// I need to test this code!
    // Now do tests over variable name, to avoid invariants like:
    //   header.next in header.~ll~next~
    //   header.next.element in header.~ll~next~.element
    //   header.next in header.next.~ll~next~
    //   return.current in return.current.~ll~next~
    String sclname = sclvar.name.name(); // mistere adds: this code
    String seqname = seqvar.name.name(); // looks pretty sketchy (XXX)
    int llpos = seqname.indexOf("~ll~");
    if (llpos != -1) {
      int tildepos = seqname.indexOf("~", llpos+5);
      if (tildepos != -1) {
        int midsize = tildepos-llpos-4;
        int lastsize = seqname.length()-tildepos-1;
        if (seqname.regionMatches(0, sclname, 0, llpos)
            && (((tildepos == seqname.length() - 1)
                 && (llpos == sclname.length()))
                || (seqname.regionMatches(llpos+4, sclname, llpos, midsize)
                    && seqname.regionMatches(tildepos+1, sclname, tildepos-4, lastsize))))
          // isObviousImplied: to do
          return true;
      }
    }

    // int lastdot = sclvar.lastIndexOf(".");
    // if (lastdot != -1) {
    //   if (sclname.substring(0, lastdot).equals(seqname.substring(0, lastdot))
    //       && seqname.substring(lastdot).equals("~ll~" + sclname.substring(lastdot) + "~")) {
    //     return true;
    //   }
    // }

    return false;
  }

  public String repr() {
    return "Member" + varNames() + ": "
      + "falsified=" + falsified;
  }

  public String format_using(OutputFormat format) {
    if (format == OutputFormat.DAIKON) {
      return format_daikon();
    } else if (format == OutputFormat.JAVA) {
      return format_java();
    } else if (format == OutputFormat.IOA) {
      return format_ioa();
    } else if (format == OutputFormat.SIMPLIFY) {
      return format_simplify();
    } else if (format == OutputFormat.ESCJAVA) {
      return format_esc();
    } else if (format == OutputFormat.JML) {
      return format_jml();
    } else {
      return format_unimplemented(format);
    }
  }

  public String format_daikon() {
    return sclvar().name.name() + " in " + seqvar().name.name();
  }

  public String format_java() {
    return "( (daikon.inv.FormatJavaHelper.memberOf("
      + sclvar().name.name()
      + " , " + seqvar().name.name() + " ) == true ) ";
  }

  public String format_ioa() {
    return sclvar().name.ioa_name() + " \\in " + seqvar().name.ioa_name();
  }

  public String format_esc() {
    // "exists x in a..b : P(x)" gets written as "!(forall x in a..b : !P(x))"
    String[] form =
      VarInfoName.QuantHelper.format_esc(new VarInfoName[]
        { seqvar().name, sclvar().name });
    return "!" + form[0] + "(" + form[1] + " != " + form[2] + ")" + form[3];
  }

  public String format_jml() {
    // Uses jml exists option
    String[] form =
      VarInfoName.QuantHelper.format_jml(new VarInfoName[]
        { seqvar().name, sclvar().name },false,false);
    return form[0] + form[1] + " == " + form[2] + form[3];
  }

  public String format_simplify() {
    // "exists x in a..b : P(x)" gets written as "!(forall x in a..b : !P(x))"
    String[] form =
      VarInfoName.QuantHelper.format_simplify(new VarInfoName[]
        { seqvar().name, sclvar().name });
    return "(NOT " + form[0] + "(NEQ " + form[1] + " " + form[2] + ")" + form[3] + ")";
  }

  public void add_modified(double [] a, double  i, int count) {
    if (ArraysMDE.indexOf(a, i) == -1) {
      if (debug.isDebugEnabled()) {
        debug.debug ("Member destroyed:  " + format() + " because " + i +
                     " not in " + ArraysMDE.toString(a));
      }
      destroyAndFlow();
      return;
    }
  }

  protected double computeProbability() {
    if (falsified)
      return Invariant.PROBABILITY_NEVER;
    else
      return Invariant.PROBABILITY_JUSTIFIED;
  }

  public boolean isSameFormula(Invariant other)
  {
    Assert.assertTrue(other instanceof MemberFloat);
    return true;
  }

  private static final SuppressionFactory[] suppressionFactories =
    new SuppressionFactory[] {
      MemberSuppressionFactory1.getInstance(),
      MemberSuppressionFactory2.getInstance()
    };

  public SuppressionFactory[] getSuppressionFactories() {
    return suppressionFactories;
  }

  /**
   * Suppression in the form of A subset B => A[i] subset B.  Note
   * that A[i] could also be max(A), etc.
   **/
  public static class MemberSuppressionFactory1 extends SuppressionFactory {

    public static final Category debug =
      Category.getInstance("daikon.suppress.factories.MemberSuppressionFactory");

    private static final MemberSuppressionFactory1 theInstance =
      new MemberSuppressionFactory1();

    private MemberSuppressionFactory1() {
      template = new SuppressionTemplate();
      template.invTypes = new Class[1];
      template.varInfos = new VarInfo[][] {new VarInfo[2]};
    }

    public static SuppressionFactory getInstance() {
      return theInstance;
    }

    private Object readResolve() {
      return theInstance;
    }

    private transient SuppressionTemplate template;

    public SuppressionLink generateSuppressionLink (Invariant arg) {
      Assert.assertTrue (arg instanceof Member);
      Member inv = (Member) arg;
      VarInfo sclSequence = inv.sclvar().isDerivedSequenceMember();
      if (sclSequence == null) return null;
      VarInfo seqvar = inv.seqvar();
      if (sclSequence.isDerivedSubSequenceOf() == seqvar) {
        return null;
        // This should never get instantiated
      }

      template.resetResults();
      template.varInfos[0][0] = sclSequence;
      template.varInfos[0][1] =  seqvar;
      {
        template.invTypes[0] = PairwiseIntComparison.class;
        SuppressionLink sl = byTemplate (template, inv);
        if (sl != null) {
          String comparator = ((PairwiseIntComparison) template.results[0]).getComparator();
          if (comparator.indexOf ("=") != -1 ||
              comparator.indexOf ("?") != -1) {
            return sl;
          }
        }
      }

      {
        // Try to see if SubSet invariant is there
        template.resetResults();
        template.invTypes[0] = SubSet.class;
        SuppressionLink sl = byTemplate (template, inv);
        if (sl != null) {
          // First transformed var in first invariant
          VarInfo transSclSequence = template.transforms[0][0];
          // Second transformed var in first invariant
          VarInfo transSeqvar = template.transforms[0][1];
          SubSet subSet = (SubSet) template.results[0];
          if ((subSet.var1_in_var2 && subSet.var1() == transSclSequence) ||
              (subSet.var2_in_var1 && subSet.var2() == transSclSequence)) {
            if (debug.isDebugEnabled()) {
              debug.debug ("Suppressed by subset: " + subSet.repr());
              debug.debug ("  sclSeq " + transSclSequence.name.name());
              debug.debug ("  seqVar " + transSeqvar.name.name());
            }
            return sl;
          }
        }
      }

      {
        // Failed on finding the right SubSet invariant.  Now try SubSequence
        template.resetResults();
        template.invTypes[0] = SubSequence.class;
        SuppressionLink sl = byTemplate (template, inv);
        if (sl != null) {
          // First transformed var in first invariant
          VarInfo transSclSequence = template.transforms[0][0];
          // Second transformed var in first invariant
          VarInfo transSeqvar = template.transforms[0][1];
          SubSequence subSeq = (SubSequence) template.results[0];
          if ((subSeq.var1_in_var2 && subSeq.var1() == transSclSequence) ||
              (subSeq.var2_in_var1 && subSeq.var2() == transSclSequence)) {
            return sl;
          }
        }
      }
      return null;
    }
  }

  /**
   * Suppression in the form of <pre>  0<=i<=j  ==>  b[i] in b[0..j] </pre>
   **/
  public static class MemberSuppressionFactory2 extends SuppressionFactory {

    public static final Category debug =
      Category.getInstance("daikon.suppress.factories.MemberSuppressionFactory2");

    private static final MemberSuppressionFactory2 theInstance =
      new MemberSuppressionFactory2();

    public static SuppressionFactory getInstance() {
      return theInstance;
    }

    private Object readResolve() {
      return theInstance;
    }

    /**
     * Check if leftIndex < rightIndex.
     **/
    public SuppressionLink generateSuppressionLink (Invariant arg) {
      Assert.assertTrue (arg instanceof MemberFloat);
      MemberFloat  inv = (MemberFloat) arg;
      VarInfo sclvar = inv.sclvar();
      VarInfo sclSequence = sclvar.isDerivedSequenceMember();
      if (debug.isDebugEnabled()) {
        debug.debug ("Trying for: " + inv.repr());
      }

      if (sclSequence == null) {
        debug.debug ("  Sclvar is not from a sequence");
        return null;
      }
      SequenceFloatSubscript  sssc = (SequenceFloatSubscript) inv.sclvar().derived;
      VarInfo leftIndex = sssc.sclvar();
      VarInfo seqvar = inv.seqvar();
      VarInfo origSeqvar = seqvar.isDerivedSubSequenceOf();
      if (origSeqvar == null) {
        debug.debug ("  Seqvar is not a subsequence derived var");
        return null;
      }
      if (sclSequence != origSeqvar) {
        debug.debug ("  Not from the same sequences");
        return null;
      }
      SequenceFloatSubsequence  ssss = (SequenceFloatSubsequence) seqvar.derived;
      VarInfo rightIndex = ssss.sclvar();
      if (debug.isDebugEnabled()) {
        debug.debug ("  Attempting to find <= template for: ");
        debug.debug ("  " + leftIndex.name.name());
        debug.debug ("  " + rightIndex.name.name());
        debug.debug ("  In inv: " + inv.repr());
      }
      if (leftIndex == rightIndex) {
        return null;
      }

      // Here's the math, explained:

      // We want A[i] in A[0..j+shift]
      // i <= j+shift
      // That's like saying:
      // i <= j + interval
      // interval = - shift

      // We want A[i] in A[j+shift..]
      // j+shift <= i
      // That's like saying:
      // j <= i + interval
      // interval = shift

      int interval = 0;
      if (ssss.from_start) {
        interval = sssc.index_shift - ssss.index_shift ;
        return findLessEqual (leftIndex, rightIndex, inv, interval);
      } else {
        interval = ssss.index_shift + sssc.index_shift;
        return findLessEqual (rightIndex, leftIndex, inv, interval);
      }
    }
  }

}
