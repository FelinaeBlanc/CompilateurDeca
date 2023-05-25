package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

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
    protected String getOperatorName() {
        return "==";
    }    

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        getLeftOperand().codeGenInst(compiler,Register.getR(2));
        getRightOperand().codeGenInst(compiler,Register.getR(3));

        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        if (value){ // SAUTE SI x == x
            compiler.addInstruction(new BEQ(e));
        } else {  // SAUTE SI x > x
            compiler.addInstruction(new BNE(e));
        }
    }
}
