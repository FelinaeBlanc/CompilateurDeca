package fr.ensimag.ima.pseudocode.instructions;

import java.math.BigDecimal;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand; 

/**
 * @author Ensimag
 * @date 21/04/2023
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(String i, GPRegister r) {
        this(new LabelOperand(new Label(i)), r);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

    public LOAD(float i, GPRegister r) {
        this(new ImmediateFloat(i), r);
    }

    
    public LOAD(String i, GPRegister r, boolean mybool) {
        this(new LabelOperand(new Label(i,mybool)), r);
    }



}
