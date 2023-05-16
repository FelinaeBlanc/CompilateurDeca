package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

/**
 * @author Ensimag
 * @date 21/04/2023
 */
public class WINT extends UnaryInstruction {
    public WINT(ImmediateInteger op) {
        super(op);
    }
    
    public WINT(int value) {
        super(new ImmediateInteger(value));
    }
    
}
