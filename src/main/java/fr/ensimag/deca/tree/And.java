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
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        if (value){
            getLeftOperand().codeGenCond(compiler,value,e);
            getRightOperand().codeGenCond(compiler,value,e);

        } else {
            Label end_not_and = new Label("end_not_and");

            getLeftOperand().codeGenCond(compiler,value,end_not_and);
            getRightOperand().codeGenCond(compiler,value,e);

            compiler.addLabel(end_not_and);
        }
    }

}

