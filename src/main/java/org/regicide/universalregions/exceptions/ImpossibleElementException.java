package org.regicide.universalregions.exceptions;

/**
 * Throws when you create an element in conditions in which it should not be created (for example, a new region in a world where the region mechanics are disabled)
 */
public class ImpossibleElementException extends Exception {

    public ImpossibleElementException() {
        super();
    }
}
