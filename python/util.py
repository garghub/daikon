### util.py -- Michael Ernst's Python utilities

import math, types, re


###########################################################################
### Numbers
###

## Roundoff errors can make floating-point values not quite what they ought
## to be.  This is a fairly unpleasant hack around the problem.

def rationalize(num, epsilon=1e-8):
    """Attempt to return a rational value near NUM (within EPSILON of it).
The value is returned as an integer or (more likely) floating-point number.
Return None if the value cannot be rationalized.
*Do not* use the return value as a boolean, because 0 and 0.0 test false.
Instead, explicitly test the result against None."""

    def rationalize_simple(num, eps=epsilon):
        floor = math.floor(num)
        diff = num-floor
        if diff < eps:
            return floor
        if 1-diff < eps:
            return floor+1
        return None

    if type(num) == types.IntType:
        return num

    result = rationalize_simple(num)
    if result != None:
        return result

    for denominator in range(2,21):
        result = rationalize_simple(num*denominator)
        if result != None:
            return result/denominator
        result = rationalize_simple(denominator/num)
        if (result != None) and (result != 0):
            return denominator/result
    return None


###########################################################################
### Strings
###

def remove_trailing_newline(str):
    """Return a copy of the input string STR, sans trailing newline.
If the input string STR has no trailing newline, it is returned unmodified."""
    if str[-1] == "\n":
        return str[:-1]
    else:
        return str


###########################################################################
### Mappings
###

def mapping_increment(mapping, key, incr=1):
    """Given a MAPPING from keys to numbers, increment the value associated
with KEY by INCR.  If KEY does not appear in MAPPING, then add it, with
initial value INCR."""
    mapping[key] = mapping.get(key, 0) + incr

def mapping_append(mapping, key, element):
    """Given a MAPPING from keys to arrays, append, to the end of the value
associated with KEY, ELEMENT.  If KEY does not appear in MAPPING, then add
it, with initial value a list containing only ELEMENT."""
    mapping[key] = mapping.get(key, []) + [element]


###########################################################################
### Slicing
###

def slice_by_sequence(seq, indices):
    """Given a sequence SEQ and a sequence of INDICES, return a new sequence
whose length is the same as that of INDICES and whose elements are the values
of SEQ indexed by the respective elements of INDICES."""
    result = []
    for i in indices:
        result.append(seq[i])
    if type(seq) == types.TupleType:
        result = tuple(result)
    return result

def _test_slice_by_sequence():
    def _test_slice_by_sequence_helper(indices):
        assert slice_by_sequence(range(0,7), indices) == list(indices)
        assert slice_by_sequence(tuple(range(0,7)), indices) == tuple(indices)
    _test_slice_by_sequence_helper(range(0,7))
    _test_slice_by_sequence_helper(range(1,4))
    _test_slice_by_sequence_helper((1,2,4,5))



###########################################################################
### Sets
###

def sorted_list_difference(minuend, subtrahend):
    """Return a list of elements in sequence MINUEND but not in SUBTRAHEND.
Both input sequences are sorted, as is the output sequence."""
    # It's more efficient to do this by hand than to use "filter" and index.
    mmax = len(minuend)
    smax = len(subtrahend)
    if mmax == 0 or smax == 0:
        return minuend
    mindex = 0; mcurr = minuend[mindex]
    sindex = 0; scurr = subtrahend[sindex]
    result = []
    while (mindex < mmax) or (sindex < smax):
	while (mindex < mmax) and ((sindex == smax) or (mcurr < scurr)):
	    result.append(mcurr)
	    mindex = mindex+1
	    if (mindex < mmax): mcurr = minuend[mindex]
	while (sindex < smax) and ((mindex == mmax) or (scurr < mcurr)):
	    sindex = sindex+1
	    if (sindex < smax): scurr = subtrahend[sindex]
	if (mindex < mmax) and (sindex < smax) and (mcurr == scurr):
	    mindex = mindex+1
	    if (mindex < mmax): mcurr = minuend[mindex]
	    sindex = sindex+1
	    if (sindex < smax): scurr = subtrahend[sindex]
    return result

def _test_sorted_list_difference():
    assert sorted_list_difference([1,2,3,4,5,6], [2,4,6]) == [1, 3, 5]
    assert sorted_list_difference([1,2,3,4,5,6], [1,2,3]) == [4, 5, 6]
    assert sorted_list_difference([1,5,6], [1,2,3,4,5,6]) == []
    assert sorted_list_difference([1,5,6], [0,1,2,3,4,6,7]) == [5]
    assert sorted_list_difference([1,2,5,6], [2,3,4,5]) == [1, 6]

# There MUST be a better way to do this!
def same_elements(seq1, seq2):
    """Return true if sequences SEQ1 and SEQ2 contain the same elements
(in any order)."""
    s1 = list(seq1)
    s2 = list(seq2)
    s1.sort()
    s2.sort()
    return s1 == s2


###########################################################################
### Lists of numbers
###

def sum(nums):
    return reduce(lambda x,y: x+y, nums, 0)

def sorted_list_min_gap(sorted_nums):
    """Return the smallest difference between adjacent elements of the sorted list."""
    if len(sorted_nums) < 2:
        raise IndexError("List too short")
    min_gap = sorted_nums[1]-sorted_nums[0]
    for i in range(1, len(sorted_nums)-2):
        this_gap = sorted_nums[i+1]-sorted_nums[i]
        if this_gap < min_gap:
            min_gap = this_gap
    return min_gap

def _test_sorted_list_min_gap():
    assert sorted_list_min_gap([2,4,6,8]) == 2
    assert sorted_list_min_gap([2,4,60,80]) == 2
    assert sorted_list_min_gap([20,40,600,608]) == 20
    assert sorted_list_min_gap([2,40,46,80]) == 6
    assert sorted_list_min_gap([2,4]) == 2
    # Errors
    # sorted_list_min_gap([2])
    # sorted_list_min_gap([])


def common_modulus(nums):
    """Return a tuple of (r,m) where each number in NUMS is equal to r (mod m).
The largest possible modulus is used, and the trivial constraint that all
integers are equal to 0 mod 1 is not returned (None is returned instead)."""
    nums = list(nums)			# convert arg if it's a tuple
    if len(nums) < 3:
        return None
    nums.sort()
    gap = sorted_list_min_gap(nums);
    if gap < 2:
        return None
    remainder = nums[0] % gap
    for elt in nums:
        if remainder != elt % gap:
            return None
    return (remainder, gap)

def _test_common_modulus():
    assert common_modulus([3,7,47,51]) == (3,4)
    assert common_modulus([3,11,43,51]) == (3,8)
    assert common_modulus([3,11,47,55]) == None

# This is perhaps too strict; even a single missing number means we don't
# make the inference.  Maybe it should be enough that there's at least one
# number in the set with each modulus.

def common_nonmodulus_strict(nums):
    """Return a tuple of (r,m) where no number in NUMS is equal to r (mod m)
but all missing numbers in their range are."""
    nums = list(nums)
    nums.sort()
    if nums[-1] - nums[0] > 65536:
        return None
    return common_modulus(sorted_list_difference(range(nums[0], nums[-1]), nums))

def _test_common_nonmodulus_strict():
    assert common_nonmodulus_strict([1,2,3,5,6,7,9]) == (0,4)
    assert common_nonmodulus_strict([1,2,3,5,6,7,9,11]) == None

def common_nonmodulus_nonstrict(nums):
    """Return a tuple of (r,m) where no number in NUMS is equal to r (mod m)
but for every number in NUMS, at least one is equal to every non-r remainder.
The modulus is chosen as small as possible, but no greater than half the
range of the input numbers (else None is returned)."""
    nums = list(nums)
    nums.sort()
    max_modulus = (nums[-1] - nums[0]) / 2
    # no real sense checking 2, as common_modulus would have found it, but
    # include it to make this function stand on its own
    for m in range(2, max_modulus+1):
        remainders = list((0,) * m)
        # It might be more efficient to quit as soon as we see every remainder
        # instead of processing each element of the input list.
        for num in nums:
            remainders[num % m] = 1
        if remainders.count(0) == 1:
            return (remainders.index(0), m)
    return None

def _test_common_nonmodulus_nonstrict():
    assert common_nonmodulus_nonstrict([1,2,3,5,6,7,9]) == (0,4)
    assert common_nonmodulus_nonstrict([1,2,3,5,6,7,9,11]) == (0,4)
    assert common_nonmodulus_nonstrict([1,2,3,5,6,7,9,11,12,13]) == (4,6)
    assert common_nonmodulus_nonstrict([1,2,3,5,6,7,9,11,12,13,16]) == None


###########################################################################
### Sorting
###

# Not worth defining -- too trivial
# def cmp_second_element(a, b):
#     # index 1 is second element
#     return cmp(a[1],  b[1])


###########################################################################
### Combinatorics
###

def choose(num, objs):
    """Choose NUM of the obs, without repetition.  Order is irrelevant.
Returns a list of the results, which are themselves lists."""
    if num == 0:
        return []
    if num == 1:
        return map(lambda x: [x], objs)
    if num == len(objs):
        return [list(objs)]
    if num > len(objs):
        raise IndexError("Can't choose %d objects from list of length %d" % (num, len(objs)))
    def make_conser(first):
        return lambda x, f=first: [f] + x
    rest = objs[1:]
    return choose(num, rest) + map(make_conser(objs[0]), choose(num-1, rest))

def _test_choose():
    assert util.choose(3,[1,2,3]) == [[1,2,3]]
    assert util.choose(3,[1,2,3,4]) == [[2,3,4], [1,3,4], [1,2,3], [1,2,4]]


# There must be a better way of doing this!
def permutations(objs):
    """Return a list containing all permutations of OBJS; each permutation
is itself a list.
Assumes OBJS contains no two elements eql to one another (they are
considered to appear just once)."""
    if len(objs) == 0:
        return [[]]
    result = []
    for obj in objs:
        subobjs = list(objs)            # makes a copy
        subobjs.remove(obj)
        for subperm in permutations(subobjs):
            subperm.append(obj)
            result.append(subperm)
    return result

def _test_permutations():
    assert permutations(()) == [[]]
    assert permutations((1,)) == [[1]]
    assert permutations((1,2)) == [[2,1], [1,2]]
    assert permutations((1,2,3)) == [[3,2,1], [2,3,1], [3,1,2], [1,3,2], [2,1,3], [1,2,3]]


###########################################################################
### Functions
###

def function_rep(fn):
    """Return an abbreviated printed representation for function FN."""
    fnrep = `fn`
    match = re.compile(r'^<built-in function ([a-zA-Z_]+)>$').match(fnrep)
    if match:
        fnrep = match.group(1)
    return fnrep


###########################################################################
### Tests
###

def _test():
    _test_sorted_list_difference()
    _test_sorted_list_min_gap()
    _test_common_modulus()
    _test_common_nonmodulus_strict()
    _test_common_nonmodulus_nonstrict()
    _test_slice_by_sequence()
    _test_choose()
    _test_permutations()

# It's slightly annoying that these run every time that I load this directly.
# But I shouldn't load it directly except to test anyway.
if __name__=='__main__':
    _test()
    print "util.py tests completed successfully"
