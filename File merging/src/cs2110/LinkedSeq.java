package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A list of elements of type `T` implemented as a singly linked list.  Null elements are not
 * allowed.
 */
public class LinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> head;

    /**
     * Last node of the linked list starting at `head` (null if list is empty).  Next node must be
     * null.
     */
    private Node<T> tail;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;

            int count = 1;

            Node<T> current = head;

            while (current.next() != null){
                count += 1;
                current = current.next();
            }
            assert (current.equals(tail));
            assert (size == count);
        }
    }

    /**
     * Create an empty list.
     */
    public LinkedSeq() {
        size = 0;
        head = null;
        tail = null;

        assertInv();
    }

    /**
     * Return the size of this linked Sequal
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Add elem to the front of this linked sequal
     */
    @Override
    public void prepend(T elem) {
        assertInv();
        assert elem != null;

        head = new Node<>(elem, head);
        // If list was empty, assign tail as well
        if (tail == null) {
            tail = head;
        }
        size += 1;

        assertInv();
    }

    /**
     * Return a text representation of this list with the following format: the string starts with
     * '[' and ends with ']'.  In between are the string representations of each element, in
     * sequence order, separated by " , ".
     * <p>
     * Example: a list containing 4 7 8 in that order would be represented by "[4, 7, 8]".
     * <p>
     * Example: a list containing two empty strings would be represented by "[, ]".
     * <p>
     * The string representations of elements may contain the characters '[', ',', and ']'; these
     * are not treated specially.
     */
    @Override
    public String toString() {
        if (size() == 0){
            return "[]";
        }

        String str = "[";
        Node<T> current = head;
        for (int i = 0; i < size(); i++) {
            str += current.data();
            if (current.next() != null){
                str += ", ";
                current = current.next();
            }
        }
        str += "]";
        assertInv();
        return str;
    }

    /**
     * Return True if elem is a node in the linked equal, false otherwise
     */
    @Override
    public boolean contains(T elem) {
        assert elem != null;
        assertInv();

        Node<T> current = head;
        for (int i = 0; i < size(); i++) {
            if (current.data().equals(elem)){
                assertInv();
                return true;
            }
            current = current.next();
        }
        assertInv();
        return false;
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * Requires index is at most the size of the linked sequence
     */
    @Override
    public T get(int index) {

        assertInv();
        assert index >= 0 && index < size;

        Node<T> current = head;
        while (index > 0){
            current = current.next();
            index--;
        }
        assertInv();
        return current.data();
    }

    /**
     * Appends the specified element to the end of the list and the size of the list is
     * increased by one.
     *
     * Requires the element to append must not be null.
     *
     * Implementation constraint: The efficiency of the method must not depend on the size of the list.
     */
    @Override
    public void append(T elem) {

        assertInv();
        assert elem != null;

        Node<T> newNode = new Node<>(elem, null);
        if (size == 0){
            head = newNode;
            tail = newNode;
        }
        else{
            tail.setNext(newNode);
            tail = newNode;
        }
        size += 1;
        assertInv();
    }

    /**
     * Inserts the specified element into the list immediately before the first occurrence of the
     * specified successor element. The size of the list is increased by one.If `successor` is the head of the list,
     * `elem` becomes the new head. The size of the list increases by one.
     *
     * Requires: The element `elem` to insert must not be null, and the `successor` element must
     *           already exist in the list. The list must contain at least one element.
     *
     * Implementation constraint: This method assumes the precondition that `successor` is present
     *                            in the list, so no need to handle the case where `successor` is not
     *                            found.
     */
    @Override
    public void insertBefore(T elem, T successor) {

        assertInv();
        assert elem != null;

        Node<T> current = head;
        Node<T> temp = null;
        Node<T> insertNode = new Node<>(elem,null);

        // Insert at the beginning
        if (head.data().equals(successor)){
            prepend(elem);
        }
        else{
            boolean found = false;
            while (current.next() != null && !found){
                if (current.next().data().equals(successor)){
                    temp = current.next();
                    insertNode.setNext(temp);
                    current.setNext(insertNode);
                    found = true;
                    size += 1;
                }
                current = current.next();
            }
        }
        assertInv();
    }

    /**
     * If the element is present in the list, removes the first occurrence of `elem` and
     * returns `true`. If the element is not found, returns `false` without modifying the
     * list. If the element being removed is the last in the list, updates the tail to the
     * previous node. The size of the list decreases by one upon successful removal.
     * If the list contains multiple instances of the element, only the first occurrence
     * is removed. The size of the list decreases by one if the element is successfully removed.
     *
     * Requires: The element `elem` must not be null.
     *
     */
    @Override
    public boolean remove(T elem) {
        assertInv();
        assert elem != null;

        Node<T> current = head;
        if (size == 0){
            assertInv();
            return false;
            }

        // When removing the first item
        if (current.data().equals(elem)){
            if (current.next() == null){
                head = null;
                tail = null;
            }
            else{
                head = current.next();
            }
            size -= 1;
            assertInv();
            return true;
        }

        while (current.next() != null) {  // Continue while there's a next node
            if (current.next().data().equals(elem)) {
                // When removing the last item
                if (current.next().next() == null) {
                    current.setNext(null);
                    tail = current;  // Update the tail to the current node
                    size -= 1;
                    assertInv();
                    return true;
                } else {
                    current.setNext(current.next().next());
                    size -= 1;
                    assertInv();
                    return true;
                }
            }
            current = current.next();  // Move to the next node
        }
        assertInv();
        return false;
    }

    /**
     * Return whether this and `other` are `LinkedSeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `LinkedSeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        // Note: In the `instanceof` check, we write `LinkedSeq` instead of `LinkedSeq<T>` because
        // of a limitation inherent in Java generics: it is not possible to check at run-time
        // what the specific type `T` is.  So instead we check a weaker property, namely,
        // that `other` is some (unknown) instantiation of `LinkedSeq`.  As a result, the static
        // type returned by `currNodeOther.data()` is `Object`.
        if (!(other instanceof LinkedSeq)) {
            return false;
        }
        LinkedSeq otherSeq = (LinkedSeq) other;
        Node<T> currNodeThis = head;
        Node<T> currNodeOther = otherSeq.head;

        boolean temp = true;

        // Check the size
        if (this.size != otherSeq.size) {
            temp = false;
            return temp;
        }

        // Check each element
        while (currNodeThis != null && currNodeOther != null) {
            assert currNodeThis.data() != null && currNodeOther.data() != null;

            if (!currNodeThis.data().equals(currNodeOther.data())) {
                temp = false;
                return temp;
            }
            currNodeThis = currNodeThis.next();
            currNodeOther = currNodeOther.next();
        }

        return temp;
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered the implementation of these concepts in class.
     */

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        // Whenever overriding `equals()`, must also override `hashCode()` to be consistent.
        // This hash recipe is recommended in _Effective Java_ (Joshua Bloch, 2008).
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * list.  Requires that the list not be mutated while the iterator is in use.
     */
    @Override
    public Iterator<T> iterator() {
        assertInv();

        // Return an instance of an anonymous inner class implementing the Iterator interface.
        // For convenience, this uses Java features that have not eyt been introduced in the course.
        return new Iterator<>() {
            private Node<T> next = head;

            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = next.data();
                next = next.next();
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }
}
