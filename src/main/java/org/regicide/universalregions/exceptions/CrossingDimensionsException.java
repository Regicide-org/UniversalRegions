package org.regicide.universalregions.exceptions;

/**
 * Throws when you try to implement an entity in two different dimensions, although you should do it in one (for example, creating a region in two different worlds)
 */
public class CrossingDimensionsException extends Exception {
    public CrossingDimensionsException() {
        super();
    }
}
