package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        getLeftOperand().codeGenInst(compiler,Register.getR(2));
        getRightOperand().codeGenInst(compiler,Register.getR(3));

        compiler.addInstruction(new CMP(Register.getR(3),Register.getR(2)));
        if (value){ // SAUTE SI x <= x
            compiler.addInstruction(new BLE(e));
        } else {  // SAUTE SI x > x
            compiler.addInstruction(new BGT(e));
        }
    }

}
