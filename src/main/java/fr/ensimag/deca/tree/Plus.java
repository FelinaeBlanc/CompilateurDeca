package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.context.EnvironmentVarValue;
/**
 * @author gl07
 * @date 21/04/2023
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected float preCalculate(float val1,float val2){ return val1 + val2; }
    @Override
    protected int preCalculate(int val1,int val2){ return val1 + val2;}
    
    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB){
        compiler.addInstruction(new ADD(dval,RegB));
    }

}
