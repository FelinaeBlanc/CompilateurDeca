package fr.ensimag.ima.pseudocode;

/**
 * Base class for instructions with 2 operands, the first being a
 * DVal, and the second a Register.
 *
 * @author Ensimag
 * @date 21/04/2023
 */
public class BinaryInstructionDValToReg extends BinaryInstruction {
    public BinaryInstructionDValToReg(DVal op1, GPRegister op2) {
        super(op1, op2);
    }
    
    public BinaryInstructionDValToReg(DVal op1, DVal op2) {
        super(op1, op2);
    }
}
