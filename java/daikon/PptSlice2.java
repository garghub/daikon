// ***** This file is automatically generated from PptSlice.java.jpp

package daikon;

import daikon.inv.*;

import daikon.inv.binary.twoScalar.*;
import daikon.inv.binary.twoSequence.*;
import daikon.inv.binary.twoString.*;
import daikon.inv.binary.sequenceScalar.*;

import org.apache.log4j.Category;

import java.util.*;

import utilMDE.*;

// This file looks a *lot* like part of PptTopLevel.
// (That is fine; its purpose is similar and mostly subsumed by VarValues.)

public final class PptSlice2
  extends PptSlice
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20020122L;

  /**
   * Debug tracer
   **/
  public static final Category debugSpecific = Category.getInstance("daikon.PptSlice2");

  // This is in PptSlice; do not repeat it here!
  // Invariants invs;

  // values_cache maps (interned) values to 4-element arrays of
  // [num_unmod_unmod, num_unmod_mod, num_mod_unmod, num_mod_mod].

  int[] tm_total = new int[4];  // "tm" stands for "tuplemod"

  /**
   * Create a new PptSlice2 .  Warning: do not rearrange the contents
   * of var_infos once this has been created, as flow order is already
   * set up after construction.
   **/
  public PptSlice2(PptTopLevel parent, VarInfo[] var_infos) {
    super(parent, var_infos);
    Assert.assertTrue(var_infos.length == 2);

    Dataflow.init_pptslice_po(this);

    // values_cache = new HashMap(); // [INCR]
    if (this.debugged || debug.isDebugEnabled() || debugSpecific.isDebugEnabled())
      debug.info("Created PptSlice2 " + this.name);

    // Make the caller do this, because
    //  1. there are few callers
    //  2. do not want to instantiate all invariants all at once
    // instantiate_invariants();
  }

  PptSlice2(PptTopLevel parent, VarInfo var_info1, VarInfo var_info2) {
    this(parent, new VarInfo[] { var_info1, var_info2 });
  }

  void instantiate_invariants(boolean excludeEquality) {
    Assert.assertTrue(!no_invariants);

    // This test should be done by caller (PptTopLevel):
    // if (isControlled()) return;

    // Instantiate invariants
    if (this.debugged || debug.isDebugEnabled() || debugSpecific.isDebugEnabled())
      debug.info("instantiate_invariants for " + name + ": originally " + invs.size() + " invariants in " + invs);

    Vector new_invs = null;

    ProglangType rep1 = var_infos[0].rep_type;
    ProglangType rep2 = var_infos[1].rep_type;
    boolean rep1_is_scalar = rep1.isScalar();
    boolean rep2_is_scalar = rep2.isScalar();
    boolean rep1_is_float  = rep1.isFloat();
    boolean rep2_is_float  = rep2.isFloat();
    if (rep1_is_scalar && rep2_is_scalar) {
      new_invs = TwoScalarFactory.instantiate(this, excludeEquality);
    } else if ((rep1 == ProglangType.STRING)
        && (rep2 == ProglangType.STRING)) {
      new_invs = TwoStringFactory.instantiate(this, excludeEquality);
    } else if ((rep1 == ProglangType.INT)
               && (rep2 == ProglangType.INT_ARRAY)) {
      new_invs = SequenceScalarFactory.instantiate(this, excludeEquality);
    } else if ((rep1 == ProglangType.INT_ARRAY)
               && (rep2 == ProglangType.INT)) {
      new_invs = SequenceScalarFactory.instantiate(this, excludeEquality);
    } else if ((rep1 == ProglangType.INT_ARRAY)
               && (rep2 == ProglangType.INT_ARRAY)) {
      new_invs = TwoSequenceFactory.instantiate(this, excludeEquality);
    } else if (Daikon.dkconfig_enable_floats
               && rep1_is_float && rep2_is_float) {
      new_invs = TwoFloatFactory.instantiate(this, excludeEquality);
    } else if (Daikon.dkconfig_enable_floats
               && (rep1 == ProglangType.DOUBLE)
               && (rep2 == ProglangType.DOUBLE_ARRAY)) {
      new_invs = SequenceFloatFactory.instantiate(this, excludeEquality);
    } else if (Daikon.dkconfig_enable_floats
               && (rep1 == ProglangType.DOUBLE_ARRAY)
               && (rep2 == ProglangType.DOUBLE)) {
    new_invs = SequenceFloatFactory.instantiate(this, excludeEquality);
    } else if (Daikon.dkconfig_enable_floats
               && (rep1 == ProglangType.DOUBLE_ARRAY)
               && (rep2 == ProglangType.DOUBLE_ARRAY)) {
    new_invs = TwoSequenceFactoryFloat.instantiate(this, excludeEquality);
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
      debug.info("after instantiate_invariants PptSlice2 " + name + " = " + this + " has " + invs.size() + " invariants in " + invs);
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

    int result = tm_total[0] + tm_total[1] + tm_total[2] + tm_total[3];

    Assert.assertTrue(result >= 0);
    return result;
  }

  public int num_mod_non_missing_samples() {

    int result = tm_total[1] + tm_total[2] + tm_total[3];

    Assert.assertTrue(result >= 0);
    return result;
  }

  public String tuplemod_samples_summary() {
    Assert.assertTrue(! no_invariants);
    return "UU=" + tm_total[0]
      + ", UM=" + tm_total[1]
      + ", MU=" + tm_total[2]
      + ", MM=" + tm_total[3];
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
    //   Global.debugInfer.debug ("PptSlice2.add(" + full_vt + ", " + count + ") for " + name);
    // }

    // Do not bother putting values into a slice if missing.

    VarInfo vi1 = var_infos[0];
    VarInfo vi2 = var_infos[1];

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

    int mod2 = full_vt.getModified(vi2);
    if (mod2 == ValueTuple.MISSING_FLOW || mod2 == ValueTuple.MISSING_NONSENSICAL) {
      // System.out.println("Bailing out of add(" + full_vt + ") for " + name);
      return emptyList;
    }
    if (mod2 == ValueTuple.STATIC_CONSTANT) {
      Assert.assertTrue(vi2.is_static_constant);
      mod2 = ((num_mod_non_missing_samples() == 0)
              ? ValueTuple.MODIFIED : ValueTuple.UNMODIFIED);
    }

    Object val1 = full_vt.getValue(vi1);

    Object val2 = full_vt.getValue(vi2);

    // if (! already_seen_all) // [INCR]
    {

      Object[] vals = Intern.intern(new Object[] { val1, val2 });

      /* [INCR] ...
      int[] tm_arr = (int[]) values_cache.get(vals);
      if (tm_arr == null) {
        tm_arr = new int[4];
        values_cache.put(vals, tm_arr);
      }
      */ // ... [INCR]

      int mod_index = mod1 * 2 + mod2;

      // tm_arr[mod_index] += count; // [INCR]
      tm_total[mod_index] += count;
    }

    // System.out.println("PptSlice2 " + name + ": add " + full_vt + " = " + vt);
    // System.out.println("PptSlice2 " + name + " has " + invs.size() + " invariants.");

    // defer_invariant_removal(); [INCR]

    // Supply the new values to all the invariant objects.
    int num_invs = invs.size();

    Assert.assertTrue((mod1 == vi1.getModified(full_vt))
                  || ((vi1.getModified(full_vt) == ValueTuple.STATIC_CONSTANT)
                      && ((mod1 == ValueTuple.UNMODIFIED)
                          || (mod1 == ValueTuple.MODIFIED))));

    Assert.assertTrue((mod1 != ValueTuple.MISSING_FLOW && mod1 != ValueTuple.MISSING_NONSENSICAL)
                  && (mod2 != ValueTuple.MISSING_FLOW && mod2 != ValueTuple.MISSING_NONSENSICAL));
    int mod_index = mod1 * 2 + mod2;
    boolean string1 = vi1.rep_type == ProglangType.STRING;
    boolean string2 = vi2.rep_type == ProglangType.STRING;
    boolean array1 = vi1.rep_type.isArray();
    boolean array2 = vi2.rep_type.isArray();
    boolean stringArray1 = vi1.rep_type == ProglangType.STRING_ARRAY;
    boolean stringArray2 = vi2.rep_type == ProglangType.STRING_ARRAY;
    if (string1 && string2) {
      String value1 = (String) val1;
      String value2 = (String) val2;
      for (int i=0; i<num_invs; i++) {
        TwoString inv = (TwoString)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value1, value2, mod_index, count);
      }
    } else if (string1 || string2) {
      throw new Error("impossible");
    } else if (vi1.rep_type==ProglangType.INT && vi2.rep_type==ProglangType.INT) {
      // long value1 = vi1.getIntValue(full_vt);
      // long value2 = vi2.getIntValue(full_vt);
      long value1 = ((Long) val1).longValue();
      long value2 = ((Long) val2).longValue();
      for (int i=0; i<num_invs; i++) {
        TwoScalar inv = (TwoScalar)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value1, value2, mod_index, count);
      }
    } else if (vi1.rep_type==ProglangType.DOUBLE && vi2.rep_type==ProglangType.DOUBLE) {
      double value1 = ((Double) val1).doubleValue();
      double value2 = ((Double) val2).doubleValue();
      for (int i=0; i<num_invs; i++) {
        TwoFloat inv = (TwoFloat)invs.get(i);
        inv.add(value1, value2, mod_index, count);
      }
    } else if (vi1.rep_type==ProglangType.DOUBLE_ARRAY && vi2.rep_type==ProglangType.DOUBLE_ARRAY) {
      double[] value1 = (double[]) val1;
      double[] value2 = (double[]) val2;
      for (int i=0; i<num_invs; i++) {
        TwoSequenceFloat inv = (TwoSequenceFloat)invs.get(i);
        inv.add(value1, value2, mod_index, count);
      }
    } else if (vi1.rep_type==ProglangType.DOUBLE && vi2.rep_type==ProglangType.DOUBLE_ARRAY) {
      double value1 = ((Double) val1).doubleValue();
      double[] value2 = (double[]) val2;
      for (int i=0; i<num_invs; i++) {
        SequenceFloat inv = (SequenceFloat)invs.get(i);
        inv.add(value2, value1, mod_index, count);
      }
   }   else if (vi1.rep_type==ProglangType.DOUBLE_ARRAY && vi2.rep_type==ProglangType.DOUBLE) {
        double[] value1 = (double[]) val1;
        double value2 = ((Double) val2).doubleValue();
      for (int i=0; i<num_invs; i++) {
        SequenceFloat inv = (SequenceFloat)invs.get(i);
        inv.add(value1, value2, mod_index, count);
      }

    } else if (array1 && (!array2)) {
      long[] seqval = (long[]) val1;
      long sclval = ((Long) val2).longValue();
      for (int i=0; i<num_invs; i++) {
        SequenceScalar inv = (SequenceScalar)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(seqval, sclval, mod_index, count);
      }
    } else if ((!array1) && (array2)) {
      long[] seqval = (long[]) val2;
      long sclval = ((Long) val1).longValue();
      for (int i=0; i<num_invs; i++) {
        SequenceScalar inv = (SequenceScalar)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(seqval, sclval, mod_index, count);
      }
    } else if (array1 && array2 && !stringArray1 && !stringArray2) {
      long[] value1 = (long[]) val1;
      long[] value2 = (long[]) val2;
      for (int i=0; i<num_invs; i++) {
        TwoSequence inv = (TwoSequence)invs.get(i);
        if (inv.falsified) continue;
        if (inv.getSuppressor() != null) continue;
        inv.add(value1, value2, mod_index, count);
      }
    } else {
      throw new Error("impossible");
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

      VarInfo vi1 = var_infos[0];
      VarInfo vi2 = var_infos[1];
      boolean string1 = vi1.rep_type == ProglangType.STRING;
      boolean string2 = vi2.rep_type == ProglangType.STRING;
      boolean array1 = vi1.rep_type.isArray();
      boolean array2 = vi2.rep_type.isArray();
      boolean doublearray1 = vi1.rep_type == ProglangType.DOUBLE_ARRAY;
      boolean doublearray2 = vi2.rep_type == ProglangType.DOUBLE_ARRAY;

      if (string1 && string2) {
        TwoString inv = (TwoString) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          String value1 = (String) vals[0];
          String value2 = (String) vals[1];
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(value1, value2, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }
      } else if (string1 || string2) {
        throw new Error("impossible");
      } else if ((!array1) && (!array2)) {
        TwoScalar inv = (TwoScalar) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          long value1 = ((Long) vals[0]).longValue();
          long value2 = ((Long) vals[1]).longValue();
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(value1, value2, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }
      } else if (doublearray1 && (!array2)) {
        SequenceFloat inv = (SequenceFloat) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          double[] seqval = (double[]) vals[0];
          double sclval = ((Double) vals[1]).doubleValue();
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(seqval, sclval, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }
      } else if (array1 && (!array2)) {
        SequenceScalar inv = (SequenceScalar) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          long[] seqval = (long[]) vals[0];
          long sclval = ((Long) vals[1]).longValue();
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(seqval, sclval, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }
       } else if ((!array1) && doublearray2) {
        SequenceFloat inv = (SequenceFloat) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          double[] seqval = (double[]) vals[1];
          double sclval = ((Double) vals[0]).doubleValue();
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(seqval, sclval, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }

       } else if ((!array1) && array2) {
        SequenceScalar inv = (SequenceScalar) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          long[] seqval = (long[]) vals[1];
          long sclval = ((Long) vals[0]).longValue();
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(seqval, sclval, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }
      } else if (doublearray1 && doublearray2) {
        TwoSequenceFloat inv = (TwoSequenceFloat) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          double[] val1 = (double[]) vals[0];
          double[] val2 = (double[]) vals[1];
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(val1, val2, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }

      } else if (array1 && array2) {
        TwoSequence inv = (TwoSequence) invariant;
        // Make this invariant up to date by supplying it with all the values.
        for (Iterator itor = values_cache.entrySet().iterator() ; itor.hasNext() ; ) {
          Map.Entry entry = (Map.Entry) itor.next();
          Object[] vals = (Object[]) entry.getKey();
          long[] val1 = (long[]) vals[0];
          long[] val2 = (long[]) vals[1];
          int[] tm_array = (int[]) entry.getValue();
          for (int mi=0; mi<tm_array.length; mi++) {
            if (tm_array[mi] > 0) {
              inv.add(val1, val2, mi, tm_array[mi]);
              if (inv.falsified)
                break;
            }
          }
          if (inv.falsified)
            break;
        }
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
    PptSlice2 result = new PptSlice2(this.parent, newVarInfos);

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
    PptSlice2 result = new PptSlice2(this.parent, newVarInfos);

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
