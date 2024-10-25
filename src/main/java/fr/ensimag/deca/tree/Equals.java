package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.DVal;
/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean preCalculateBool(float val1, float val2){ 
        return val1 == val2;
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }    

    @Override
    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB, boolean value, Label e){
        compiler.addInstruction(new CMP(dval,RegB));
        
        if (value){ // SAUTE SI x == x
            compiler.addInstruction(new BEQ(e));
        } else {  // SAUTE SI x > x
            compiler.addInstruction(new BNE(e));
        }
    }


}
