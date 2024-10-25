package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean preCalculateBool(boolean val1, boolean val2){ 
        return val1 && val2;
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond(compiler,2, value, e);
    }

    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        if (value){
            Label end_not_and = new Label("end_not_and");

            getLeftOperand().codeExpGenCond(compiler,registerNb, !value,end_not_and);
            getRightOperand().codeExpGenCond(compiler,registerNb, value,e);
            
            compiler.addLabel(end_not_and);
        } else {
            
            getLeftOperand().codeExpGenCond(compiler,registerNb, value,e);
            getRightOperand().codeExpGenCond(compiler,registerNb, value,e);

        }
    }

}

