// ***** This file is automatically generated from PptSlice.java.jpp

package daikon;

import daikon.inv.*;

import daikon.inv.unary.scalar.*;
import daikon.inv.unary.string.*;
import daikon.inv.unary.sequence.*;
import daikon.inv.unary.stringsequence.*;

import org.apache.log4j.Category;

import java.util.*;

import utilMDE.*;

// This file looks a *lot* like part of PptTopLevel.
// (That is fine; its purpose is similar and mostly subsumed by VarValues.)

public final class PptSlice1
  extends PptSlice
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20020122L;

  /**
   * Debug tracer
   **/
  public static final Category debugSpecific = Category.getInstance("daikon.PptSlice1");

  // This is in PptSlice; do not repeat it here!
  // Invariants invs;

  public VarInfo var_info;

  // values_cache maps (interned) values to 2-element arrays of
  // [num_unmodified, num_modified].

  int[] tm_total = new int[2];  // "tm" stands for "tuplemod"

  /**
   * Create a new PptSlice1 .  Warning: do not rearrange the contents
   * of var_infos once this has been created, as flow order is already
   * set up after construction.
   **/
  public PptSlice1(PptTopLevel parent, VarInfo[] var_infos) {
    super(parent, var_infos);
    Assert.assertTrue(var_infos.length == 1);

    var_info = var_infos[0];

    Dataflow.init_pptslice_po(this);

    // values_cache = new HashMap(); // [INCR]
    if (this.debugged || debug.isDebugEnabled() || debugSpecific.isDebugEnabled())
      debug.info("Created PptSlice1 " + this.name);

    // Make the caller do this, because
    //  1. there are few callers
    //  2. do not want to instantiate all invariants all at once
    // instantiate_invariants();
  }

  PptSlice1(PptTopLevel parent, VarInfo var_info) {
    this(parent, new VarInfo[] { var_info });
  }

  void instantiate_invariants(boolean excludeEquality) {
    Assert.assertTrue(!no_invariants);

    // This test should be done by caller (PptTopLevel):
    // if (isControlled()) return;

    // Instantiate invariants
    if (this.debugged || debug.isDebugEnabled() || debugSpecific.isDebugEnabled())
      debug.info("instantiate_invariants for " + name + ": originally " + invs.size() + " invariants in " + invs);

    Vector new_invs = null;

    ProglangType rep_type = var_info.rep_type;
    boolean is_scalar = rep_type.isScalar();
    if (is_scalar) {
      new_invs = SingleScalarFactory.instantiate(this, excludeEquality);
    } else if (rep_type == ProglangType.INT_ARRAY) {
      new_invs = SingleScalarSequenceFactory.instantiate(this, excludeEquality);
    } else if (Daikon.dkconfig_enable_floats
               && rep_type == ProglangType.DOUBLE) {
      new_invs = SingleFloatFactory.instantiate(this, excludeEquality);
    } else if (Daikon.dkconfig_enable_floats
               && rep_type == ProglangType.DOUBLE_ARRAY) {
      new_invs = SingleFloatSequenceFactory.instantiate(this, excludeEquality);
    } else if (rep_type == ProglangType.STRING) {
      new_invs = SingleStringFactory.instantiate(this, excludeEquality);
    } else if (rep_type == ProglangType.STRING_ARRAY) {
      new_invs = SingleStringSequenceFactory.instantiate(this, excludeEquality);
    } else {
      // Do nothing; do not even complain
    }

    if (new_invs != null) {
      for (int i=0; i<new_invs.size(); i++) {
        Invariant inv = (Invariant) new_invs.get(i);
        if (inv == null)
          continue;
        addInvariant(inv);
      }
    }

    if (this.debugged || debug.isDebugEnabled() || debugSpecific.isDebugEnabled()) {
      debug.info("after instantiate_invariants PptSlice1 " + name + " = " + this + " has " + invs.size() + " invariants in " + invs);
    }
    if ((this.debugged  || debugSpecific.isDebugEnabled()) && (invs.size() > 0)) {
      debug.info("the invariants are:");
      for (int i=0; i<invs.size(); i++) {
        Invariant inv = (Invariant) invs.get(i);
        debug.info("  " + inv.format());
        debug.info("    " + inv.repr());
      }
    }

  }

  /**
   * Set the number of samples for this slice to be at least count.
   **/
  public void set_samples (int count) {
    if (tm_total[0] < count) tm_total[0] = count;
  }

  // These accessors are for abstract methods declared in Ppt
  public int num_samples() {

    int result = tm_total[0] + tm_total[1];

    Assert.assertTrue(result >= 0);
    return result;
  }

  public int num_mod_non_missing_samples() {

    int result = tm_total[1];

    Assert.assertTrue(result >= 0);
    return result;
  }

  public String tuplemod_samples_summary() {
    Assert.assertTrue(! no_invariants);
    return "U=" + tm_total[0]
      + ", M=" + tm_total[1];
  }

  // public int num_missing() { return values_cache.num_missing; }

  // Accessing data
  int num_vars() {
    return var_infos.length;
  }
  Iterator var_info_iterator() {
    return Arrays.asList(var_infos).iterator();
  }

  boolean compatible(Ppt other) {
    // This insists that the var_infos lists are identical.  The Ppt
    // copy constructor does reuse the var_infos field.
    return (var_infos == other.var_infos);
  }

  ///////////////////////////////////////////////////////////////////////////
  /// Manipulating values
  ///

  /**
   * This procedure accepts a sample (a ValueTuple), extracts the values
   * from it, casts them to the proper types, and passes them along to the
   * invariants proper.  (The invariants accept typed values rather than a
   * ValueTuple that encapsulates objects of any type whatever.)
   * @param invsFlowed after this method, holds the Invariants that
   * flowed.
   **/
  public List add(ValueTuple full_vt, int count) {
    //     if (debugFlow.isDebugEnabled()) {
    //       debugFlow.debug ("<< Doing add for " + this.toString());
    //       StringBuffer sb = new StringBuffer();
    //       for (int i = 0; i < var_infos.length; i++) {
    //         VarInfo vi = var_infos[i];
    //         Object val = vi.getValue(full_vt);
    //         sb.append (" ");
    //         sb.append (ValueTuple.valToString (val));
    //       }
    //       debugFlow.debug ("    with values:" + sb);
    //     }

    Assert.assertTrue(! no_invariants);
    Assert.assertTrue(invs.size() > 0);
    // Assert.assertTrue(! already_seen_all); // [INCR]
    for (int i=0; i<invs.size(); i++) {
      Assert.assertTrue(invs.get(i) != null);
    }

    // if (Global.debugInfer.isDebugEnabled()) {
    //   Global.debugInfer.debug ("PptSlice1.add(" + full_vt + ", " + count + ") for " + name);
    // }

    // Do not bother putting values into a slice if missing.

    VarInfo vi1 = var_info;

    int mod1 = full_vt.getModified(vi1);
    if (mod1 == ValueTuple.MISSING_FLOW || mod1 == ValueTuple.MISSING_NONSENSICAL) {
      // System.out.println("Bailing out of add(" + full_vt + ") for " + name);
      return emptyList;
    }
    if (mod1 == ValueTuple.STATIC_CONSTANT) {
      Assert.assertTrue(vi1.is_static_constant);
      mod1 = ((num_mod_non_missing_samples() == 0)
              ? ValueTuple.MODIFIED : ValueTuple.UNMODIFIED);
    }

    Object val1 = full_vt.getValue(vi1);

    // if (! already_seen_all) // [INCR]
    {

      Object vals = val1;

      /* [INCR] ...
      int[] tm_arr = (int[]) values_cache.get(vals);
      if (tm_arr == null) {
        tm_arr = new int[2];
        values_cache.put(vals, tm_arr);
      }
      */ // ... [INCR]

      int mod_index = mod1;

      // tm_arr[mod_index] += count; // [INCR]
      tm_total[mod_index] += count;
    }

    // System.out.println("PptSlice1 " + name + ": add " + full_vt + " = " + vt);
    // System.out.println("PptSlice1 " + name + " has " + invs.size() + " invariants.");

    // defer_invariant_removal(); [INCR]

    // Supply the new values to all the invariant objects.
    int num_invs = invs.size();

    Assert.assertTrue((mod1 == vi1.getModified(full_vt))
                  || ((vi1.getModified(full_vt) == ValueTuple.STATIC_CONSTANT)
                      && ((mod1 == ValueTuple.UNMODIFIED)
                          || (mod1 == ValueTuple.MODIFIED))));

    Assert.assertTrue(mod1 != ValueTuple.MISSING_FLOW && mod1 != ValueTuple.MISSING_NONSENSICAL);
    ProglangType rep = vi1.rep_type;
    boolean rep_is_scalar = rep.isScalar();
    if (rep_is_scalar) {
      // long value = vi1.getIntValue(full_vt);
      long value = ((Long) val1).longValue();
      for (int i=0; i<num_invs; i++) {
        SingleScalar inv = (SingleScalar)invs.get(i);
        if (inv.falsified) continue;
        // Should the suppressed invariants be put in their own list?
        if (inv.getSuppressor() != null) continue;
        inv.add(value, mod1, count);
      }
    } else if (rep == ProglangType.DOUBLE) {
      // double value = vi1.getDoubleValue(full_vt);
      double value = ((Double) val1).doubleValue();
      for (int i=0; i<num_invs; i++) {
        SingleFloat inv = (SingleFloat)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value, mod1, count);
      }
    } else if (rep == ProglangType.STRING) {
      // String value = vi1.getStringValue(full_vt);
      String value = (String) val1;
      for (int i=0; i<num_invs; i++) {
        // System.out.println("Trying " + invs.get(i));
        SingleString inv = (SingleString) invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value, mod1, count);
      }
    } else if (rep == ProglangType.DOUBLE_ARRAY) {
      // double[] value = vi1.getDoubleArrayValue(full_vt);
      double[] value = (double[]) val1;
      for (int i=0; i<num_invs; i++) {
        SingleFloatSequence inv = (SingleFloatSequence)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value, mod1, count);
      }
    } else if (rep == ProglangType.INT_ARRAY) {
      // long[] value = vi1.getIntArrayValue(full_vt);
      long[] value = (long[]) val1;
      for (int i=0; i<num_invs; i++) {
        SingleScalarSequence inv = (SingleScalarSequence)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value, mod1, count);
      }
    } else if (rep == ProglangType.STRING_ARRAY) {
      String[] value = (String[]) val1;
      for (int i=0; i<num_invs; i++) {
        SingleStringSequence inv = (SingleStringSequence)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value, mod1, count);
      }
    } else {
      throw new Error("unrecognized representation " + rep.format());
    }

    // undefer_invariant_removal(); [INCR]
    return flow_and_remove_falsified();
  }

  public void addInvariant(Invariant invariant) {
    Assert.assertTrue(invariant != null);
    invs.add(invariant);
    Global.instantiated_invariants++;
    if (Global.debugStatistics.isDebugEnabled() || this.debugged || debugSpecific.isDebugEnabled())
      debug.info("instantiated_invariant: " + invariant.format()
                 // [INCR] + "; already_seen_all=" + already_seen_all
                 );

    /* [INCR] ... I think this is now unnecessary; not sure. XXX
    if (already_seen_all) {
      // Make this invariant up to date by supplying it with all the values
      // which have already been seen.
      // (Do not do
      //   Assert.assertTrue(values_cache.entrySet().size() > 0);
      // because all the values might have been missing.  We used to ignore
      // variables that could have some missing values, but no longer.)

      ProglangType rep = var_info.rep_type;

      if (rep == ProglangType.INT) {
        SingleScalar inv = (SingleScalar) invariant;
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          long val = ((Long) entry.getKey()).longValue();
          int[] tm_array = (int[]) entry.getValue();
          inv.add(val, 0, tm_array[0]);
          inv.add(val, 1, tm_array[1]);
          if (inv.falsified)
            break;
        }
      } else if (rep == ProglangType.DOUBLE) {
        SingleFloat inv = (SingleFloat) invariant;
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          double val = ((Double) entry.getKey()).doubleValue();
          int[] tm_array = (int[]) entry.getValue();
          inv.add(val, 0, tm_array[0]);
          inv.add(val, 1, tm_array[1]);
          if (inv.falsified)
            break;
        }
      } else if (rep == ProglangType.STRING) {
        SingleString inv = (SingleString) invariant;
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          String val = (String) entry.getKey();
          int[] tm_array = (int[]) entry.getValue();
          inv.add(val, 0, tm_array[0]);
          inv.add(val, 1, tm_array[1]);
          if (inv.falsified)
            break;
        }
      } else if (rep == ProglangType.INT_ARRAY) {
        SingleScalarSequence inv = (SingleScalarSequence) invariant;
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          long[] val = (long[]) entry.getKey();
          int[] tm_array = (int[]) entry.getValue();
          inv.add(val, 0, tm_array[0]);
          inv.add(val, 1, tm_array[1]);
          if (inv.falsified)
            break;
        }
      } else if (rep == ProglangType.DOUBLE_ARRAY) {
        SingleFloatSequence inv = (SingleFloatSequence) invariant;
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          double[] val = (double[]) entry.getKey();
          int[] tm_array = (int[]) entry.getValue();
          inv.add(val, 0, tm_array[0]);
          inv.add(val, 1, tm_array[1]);
          if (inv.falsified)
            break;
        }
      } else if (rep == ProglangType.STRING_ARRAY) {
        SingleStringSequence inv = (SingleStringSequence) invariant;
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          String[] val = (String[]) entry.getKey();
          int[] tm_array = (int[]) entry.getValue();
          inv.add(val, 0, tm_array[0]);
          inv.add(val, 1, tm_array[1]);
          if (inv.falsified)
            break;
        }
      } else {
        throw new Error("unrecognized representation " + rep.format());
      }

    }
    */ // ... [INCR]
  }

  /**
   * @see daikon.PptSlice
   **/
  protected PptSlice cloneOnePivot (VarInfo leader, VarInfo newLeader) {
    VarInfo[] newVarInfos = new VarInfo[arity];
    // rename the VarInfo references to subsitute newLeader for leader
    for (int i = 0; i < var_infos.length; i++) {
      if (var_infos[i] == leader) {
        newVarInfos[i] = newLeader;
      } else {
        newVarInfos[i] = var_infos[i];
      }
    }
    // Why not just clone?  Because then index order wouldn't be
    // preserved
    Arrays.sort (newVarInfos, VarInfo.IndexComparator.theInstance);
    PptSlice1 result = new PptSlice1(this.parent, newVarInfos);

    // Why do we have to pick out the permutation again?  Because we
    // sort it above by index order
    int[] permutation = new int[arity];
    for (int i = 0; i < var_infos.length; i++) {
      if (var_infos[i] == leader) {
        permutation[i] = ArraysMDE.indexOfEq (newVarInfos, newLeader);
      } else {
        permutation[i] = ArraysMDE.indexOfEq (newVarInfos, var_infos[i]);
      }
      Assert.assertTrue (permutation[i] != -1);
    }

    // Set sample counts
    for (int i = 0; i < tm_total.length; i++) {
      result.tm_total[i] = this.tm_total[i];
    }

    // re-parent the invariants and copy them out
    List newInvs = new LinkedList();
    for (Iterator i = invs.iterator(); i.hasNext(); ) {
      Invariant inv = (Invariant) i.next();
      Assert.assertTrue (inv.ppt == this);
      Invariant newInv = inv.transfer (result, permutation);
      //if (!newInv.isObvious()) {
      newInvs.add (newInv);
      parent.attemptSuppression (newInv);
      Assert.assertTrue (newInv != inv);
      Assert.assertTrue (newInv.ppt == result);
      Assert.assertTrue (inv.ppt == this);
      //}
    }

    result.invs.addAll (newInvs);
    if (PptSliceEquality.debug.isDebugEnabled()) {
      PptSliceEquality.debug.debug ("cloneOnePivot: newInvs " + invs);
    }
    result.repCheck();
    return result;
  }

  /**
   * @see daikon.PptSlice
   **/
  protected PptSlice cloneAllPivots () {
    // No need to do it if this is a slice for equality
    if (this.var_infos.length == 2 &&
        this.var_infos[0].equalitySet ==
          this.var_infos[1].equalitySet) {
      return this;
    }
    VarInfo[] newVarInfos = new VarInfo[this.var_infos.length];
    boolean pivoted = false;
    for (int i = 0; i < this.var_infos.length; i++) {
      VarInfo vi = this.var_infos[i];
      if (vi.canonicalRep() != vi) {
        pivoted = true;
        newVarInfos[i] = vi.canonicalRep();
      } else {
        newVarInfos[i] = vi;
      }
    }
    if (!pivoted) return this;

    // Why not just clone?  Because then index order wouldn't be
    // preserved
    Arrays.sort (newVarInfos, VarInfo.IndexComparator.theInstance);
    PptSlice1 result = new PptSlice1(this.parent, newVarInfos);

    // Why do we have to pick out the permutation again?  Because we
    // sort it above by index order
    int[] permutation = new int[arity];
    for (int i = 0; i < var_infos.length; i++) {
      permutation[i] = ArraysMDE.indexOfEq (newVarInfos,
                                            var_infos[i].canonicalRep());
      Assert.assertTrue (permutation[i] != -1);
    }

    // Set sample counts
    for (int i = 0; i < tm_total.length; i++) {
      result.tm_total[i] = this.tm_total[i];
    }

    // re-parent the invariants and copy them out
    List newInvs = new LinkedList();
    for (Iterator i = invs.iterator(); i.hasNext(); ) {
      Invariant inv = (Invariant) i.next();
      Assert.assertTrue (inv.ppt == this);
      if (Equality.debugPostProcess.isDebugEnabled()) {
        Equality.debugPostProcess.debug ("before: " + inv.repr());
      }
      Invariant newInv = inv.transfer (result, permutation);
      if (Equality.debugPostProcess.isDebugEnabled()) {
        Equality.debugPostProcess.debug ("after: " + newInv.repr());
      }
      newInvs.add (newInv);
      // if (!newInv.isObvious()) {
      parent.attemptSuppression (newInv);
      Assert.assertTrue (newInv != inv);
      Assert.assertTrue (newInv.ppt == result);
      Assert.assertTrue (inv.ppt == this);
      // }
    }

    result.invs.addAll (newInvs);
    if (Equality.debugPostProcess.isDebugEnabled()) {
      Equality.debugPostProcess.debug ("cloneAllPivots: newInvs " + invs);
    }
    result.repCheck();
    return result;
  }
}
