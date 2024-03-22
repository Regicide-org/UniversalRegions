package org.regicide.universalregions.exceptions;

/**
 * Throws when you try to create an element in the collection that should not be there.
 */
public class AlreadyExistingElementException extends Exception {
    public AlreadyExistingElementException() {
        super();
    }
}
