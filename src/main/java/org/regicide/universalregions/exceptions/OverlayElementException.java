package org.regicide.universalregions.exceptions;

/**
 * Throws when one element is inherited by two, although this should not happen (for example, one chunk belongs to two different regions, etc.)
 */
public class OverlayElementException extends Exception {
    public OverlayElementException() {
        super();
    }
}
