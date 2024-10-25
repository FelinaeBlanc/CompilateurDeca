package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractLValue extends AbstractExpr {

    DAddr getDAddr() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
