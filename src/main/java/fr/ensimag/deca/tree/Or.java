package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean preCalculateBool(boolean val1, boolean val2){ 
        return val1 || val2;
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond( compiler, 2, value, e);
    }

    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        if (value){ // On inverse

            getLeftOperand().codeExpGenCond(compiler,registerNb, value,e);
            getRightOperand().codeExpGenCond(compiler,registerNb, value,e);

        } else {
            Label end_or = new Label("end_or");

            getLeftOperand().codeExpGenCond(compiler,registerNb, !value,end_or);
            getRightOperand().codeExpGenCond(compiler,registerNb, value,e);

            compiler.addLabel(end_or);
        }
    }
}