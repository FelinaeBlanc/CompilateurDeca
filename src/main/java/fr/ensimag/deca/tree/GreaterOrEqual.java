package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
/**
 * Operator "x >= y"
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean preCalculateBool(float val1, float val2){ 
        return val1 >= val2;
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB, boolean value, Label e){
        compiler.addInstruction(new CMP(dval,RegB));
        if (value){ // SAUTE SI x >= x
            compiler.addInstruction(new BGE(e));
        } else {  // SAUTE SI x < x
            compiler.addInstruction(new BLT(e));
        }
    }
}
