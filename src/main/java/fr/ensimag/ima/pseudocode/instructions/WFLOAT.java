package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

/**
 *
 * @author Ensimag
 * @date 21/04/2023
 */
public class WFLOAT extends UnaryInstruction {
    public WFLOAT(ImmediateFloat op) {
        super(op);
    }
    
    public WFLOAT(float value) {
        super(new ImmediateFloat(value));
    }
}