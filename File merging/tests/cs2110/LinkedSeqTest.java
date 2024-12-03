package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedSeqTest {

    // Helper functions for creating lists used by multiple tests.  By constructing strings with
    // `new`, more likely to catch inadvertent use of `==` instead of `.equals()`.

    /**
     * Creates [].
     */
    static Seq<String> makeList0() {
        return new LinkedSeq<>();
    }

    /**
     * Creates ["A"].  Only uses prepend.
     */
    static Seq<String> makeList1() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B"].  Only uses prepend.
     */
    static Seq<String> makeList2() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B", "C"].  Only uses prepend.
     */
    static Seq<String> makeList3() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates a list containing the same elements (in the same order) as array `elements`.  Only
     * uses prepend.
     */
    static <T> Seq<T> makeList(T[] elements) {
        Seq<T> ans = new LinkedSeq<>();
        for (int i = elements.length; i > 0; i--) {
            ans.prepend(elements[i - 1]);
        }
        return ans;
    }

    @DisplayName("WHEN a LinkedSeq is first constructed, THEN it should be empty.")
    @Test
    void testConstructorSize() {
        Seq<String> list = new LinkedSeq<>();
        assertEquals(0, list.size());
    }

    @DisplayName("GIVEN a LinkedSeq, WHEN an element is prepended, " +
            "THEN its size should increase by 1 each time.")
    @Test
    void testPrependSize() {
        Seq<String> list = new LinkedSeq<>();

        // WHEN an element is prepended to an empty list
        list.prepend("a");
        assertEquals(1, list.size());

        // WHEN an element is prepended to a list with only one node(head and tail are the same)
        list.prepend("b");
        assertEquals(2, list.size());

        // WHEN an element is prepended to a list with two nodes(no nodes between its head and tail)
        list.prepend("c");
        assertEquals(3, list.size());

        // WHEN an element is prepended to a general list
        list.prepend("d");
        assertEquals(4, list.size());
    }

    @DisplayName("GIVEN a LinkedSeq containing a sequence of values, " +
            "THEN its string representation should include the string representations of its " +
            "values, in order, separated by a comma and space, all enclosed in square brackets.")
    @Test
    void testToString() {
        Seq<String> list=new LinkedSeq<>();

        // WHEN empty
        list.toString();
        assertEquals("[]", list.toString());

        // WHEN only one node in list.
        list.append("a");
        assertEquals("[a]", list.toString());

        // WHEN two nodes in list.
        list.append("b");
        assertEquals("[a, b]", list.toString());

        // WHEN there are at least 3 nodes
        list.append("c");
        assertEquals("[a, b, c]", list.toString());

        // WHEN values are not strings
        Seq<Integer> list2=new LinkedSeq<>();
        list2.append(1);
        list2.append(2);
        list2.append(3);
        assertEquals("[1, 2, 3]", list2.toString());
    }

    @DisplayName("GIVEN a LinkedSeq, WHEN elements are appended" +
            "        THEN it should correctly determine whether it contains the specified elements.")
    @Test
    void testContains() {
        Seq<Integer> numList = new LinkedSeq<>();

        Seq<String> list = new LinkedSeq<>();

        // Test empty list
        assertFalse(list.contains("CS 2110"));

        // Test contains single element
        list.append("CS 2110");
        assertTrue(list.contains("CS 2110"));

        // Test contains multiple elements
        list.append("ENGRD 2700");
        list.append("MATH 2210");
        list.append("MATH 2210");
        assertTrue(list.contains("MATH 2210"));

        // Test element not present
        assertFalse(list.contains("ASIAN 2299"));
    }


    @DisplayName("GIVEN a LinkedSeq with several elements, " +
            "THEN it should correctly return elements by index using the get method.")
    @Test
    void testGet() {
        Seq<String> strList = new LinkedSeq<>();

        strList.append("DEA 1500");
        strList.append("DEA 1110");
        strList.append("CHEM 2070");
        strList.append("CS 2800");
        strList.append("");

        // Get from a list with more than 3 items
        assertEquals("DEA 1500", strList.get(0));
        assertEquals("DEA 1110", strList.get(1));
        assertEquals("CHEM 2070", strList.get(2));
        assertEquals("CS 2800", strList.get(3));
        assertEquals("", strList.get(4));
    }

    @DisplayName("GIVEN a LinkedSeq, WHEN elements are appended, " +
            "THEN it should correctly append elements and update its size.")
    @Test
    void testAppend() {

        Seq<String> strList = new LinkedSeq<>();
        assertEquals("[]", strList.toString());

        //append to an empty list
        strList.append("CS 2110");
        assertEquals("[CS 2110]", strList.toString());

        //append to list with one node
        strList.append("ENGRD 2700");
        assertEquals("[CS 2110, ENGRD 2700]", strList.toString());

        //append to list with two nodes
        strList.append("MATH 2210");
        assertEquals("[CS 2110, ENGRD 2700, MATH 2210]", strList.toString());

        //append to list with more than two nodes
        strList.append("INFO 3300");
        assertEquals("[CS 2110, ENGRD 2700, MATH 2210, INFO 3300]", strList.toString());
    }

    @DisplayName("GIVEN a LinkedSeq, WHEN elements are inserted before specific elements, " +
            "THEN it should correctly insert the new elements at the correct positions.")
    @Test
    void testInsertBefore() {

        Seq<String> strList = new LinkedSeq<>();
        strList.append("CS 2110");

        // Insert when only have one element
        strList.insertBefore("CHEM 3570", "CS 2110");
        assertEquals("[CHEM 3570, CS 2110]", strList.toString());

        // Insert when only have two element
        strList.insertBefore("CHEM 2070", "CHEM 3570");
        assertEquals("[CHEM 2070, CHEM 3570, CS 2110]", strList.toString());

        strList.remove("CHEM 3570");
        strList.remove("CHEM 2070");

        strList.append("ENGRD 2700");
        strList.append("MATH 2210");

        // Insert before the middle element
        strList.insertBefore("CHEM 3570", "ENGRD 2700");
        assertEquals("[CS 2110, CHEM 3570, ENGRD 2700, MATH 2210]", strList.toString());

        // Insert before the head
        strList.insertBefore("PHYS 2213", "CS 2110");
        assertEquals("[PHYS 2213, CS 2110, CHEM 3570, ENGRD 2700, MATH 2210]", strList.toString());

        // Insert before the tail
        strList.insertBefore("CS 3110", "MATH 2210");
        assertEquals("[PHYS 2213, CS 2110, CHEM 3570, ENGRD 2700, CS 3110, MATH 2210]", strList.toString());
    }

    @DisplayName("GIVEN a LinkedSeq, WHEN elements are removed, " +
            "THEN it should correctly remove elements and update its size.")
    @Test
    void testRemove() {

        Seq<String> strList = new LinkedSeq<>();
        strList.append("CS 2110");
        strList.append("ENGRD 2700");
        strList.append("MATH 2210");

        // Test removing an element that doesn't exist
        assertFalse(strList.remove("ANSC 1500"));
        assertEquals("[CS 2110, ENGRD 2700, MATH 2210]", strList.toString());

        // Test removing an element from the middle
        assertTrue(strList.remove("ENGRD 2700"));
        assertEquals("[CS 2110, MATH 2210]", strList.toString());

        // Test removing the head
        assertTrue(strList.remove("CS 2110"));
        assertEquals("[MATH 2210]", strList.toString());

        // Test removing an element that doesn't exist
        assertFalse(strList.remove("ANSC 1500"));
        assertEquals("[MATH 2210]", strList.toString());

        // Test removing the tail
        assertTrue(strList.remove("MATH 2210"));
        assertEquals("[]", strList.toString());

        // Test removing an element that doesn't exist
        assertFalse(strList.remove("ANSC 1500"));
        assertEquals("[]", strList.toString());

        // Test removing from an empty list
        assertFalse(strList.remove("CS 3110"));
        assertEquals("[]", strList.toString());

        // Test removing the last element and re-adding
        strList.append("CS 2110");
        assertTrue(strList.remove("CS 2110"));
        assertEquals("[]", strList.toString());

        strList.append("CS 2110");
        strList.append("ENGRD 2700");
        strList.append("ENGRD 2700");
        strList.append("MATH 2210");
        strList.append("ENGRD 2700");
        strList.append("ANSC 1500");

        assertTrue(strList.remove("ENGRD 2700"));
        assertEquals("[CS 2110, ENGRD 2700, MATH 2210, ENGRD 2700, ANSC 1500]", strList.toString());

        // Test removing the tail
        assertTrue(strList.remove("ANSC 1500"));
        assertEquals("[CS 2110, ENGRD 2700, MATH 2210, ENGRD 2700]", strList.toString());
        // Test inserting before a non-existent element (should not change the list)

        // Insert before a duplicate element, should before the first
        Seq<String> duplicateList = new LinkedSeq<>();
        duplicateList.append("CS 2110");
        duplicateList.append("ENGRD 2700");
        duplicateList.append("CS 2110");  // Duplicate element
        duplicateList.insertBefore("PHYS 2213", "CS 2110");
        assertEquals("[PHYS 2213, CS 2110, ENGRD 2700, CS 2110]", duplicateList.toString());

        // Insert before the first element of a multi-element list
        duplicateList.insertBefore("INFO 2300", "PHYS 2213");
        assertEquals("[INFO 2300, PHYS 2213, CS 2110, ENGRD 2700, CS 2110]", duplicateList.toString());
    }

    @DisplayName("GIVEN two LinkedSeqs, WHEN comparing them, " +
            "THEN they should be equal if they contain the same elements in the same order.")
    @Test
    void testEquals() {

        Seq<String> strList1 = new LinkedSeq<>();
        strList1.append("CS 2110");
        strList1.append("ENGRD 2700");
        strList1.append("MATH 2210");

        Seq<String> strList2 = new LinkedSeq<>();
        strList2.append("CS 2110");
        strList2.append("ENGRD 2700");
        strList2.append("MATH 2210");

        Seq<String> strList3 = new LinkedSeq<>();
        strList3.append("CS 3110");
        strList3.append("ENGRD 2700");

        // Test equal lists
        assertTrue(strList1.equals(strList2));

        // Test unequal lists (different sizes)
        assertFalse(strList1.equals(strList3));

        // Test unequal lists (same size, different elements)
        strList2.remove("MATH 2210");
        strList2.append("PHYS 2213");
        assertFalse(strList1.equals(strList2));

        // Test prefix list (one list is a prefix of the other)
        strList3.prepend("CS 2110");
        assertFalse(strList3.equals(strList1));

        // Test equality when there are at least 3 nodes
        Seq<String> threeNodeList1 = new LinkedSeq<>();
        Seq<String> threeNodeList2 = new LinkedSeq<>();
        threeNodeList1.append("CS 2110");
        threeNodeList1.append("ENGRD 2700");
        threeNodeList1.append("INFO 2040");
        threeNodeList2.append("CS 2110");
        threeNodeList2.append("ENGRD 2700");
        threeNodeList2.append("INFO 2040");
        assertTrue(threeNodeList1.equals(threeNodeList2));
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered `hashCode()` or `assertThrows()` in class.
     */

    @DisplayName("GIVEN two distinct LinkedSeqs containing equivalent values in the same order, " +
            "THEN their hash codes should be the same.")
    @Test
    void testHashCode() {
        // WHEN empty
        assertEquals(makeList0().hashCode(), makeList0().hashCode());

        // WHEN head and tail are the same
        assertEquals(makeList1().hashCode(), makeList1().hashCode());

        // WHEN there are no nodes between head and tail
        assertEquals(makeList2().hashCode(), makeList2().hashCode());

        // WHEN there are at least 3 nodes
        assertEquals(makeList3().hashCode(), makeList3().hashCode());
    }

    @DisplayName("GIVEN a LinkedSeq, THEN its iterator should yield its values in order " +
            "AND it should stop yielding after the last value.")
    @Test
    void testIterator() {
        Seq<String> list;
        Iterator<String> it;

        // WHEN empty
        list = makeList0();
        it = list.iterator();
        assertFalse(it.hasNext());
        Iterator<String> itAlias = it;
        assertThrows(NoSuchElementException.class, () -> itAlias.next());

        // WHEN head and tail are the same
        list = makeList1();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());

        // WHEN there are no nodes between head and tail
        list = makeList2();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }
}