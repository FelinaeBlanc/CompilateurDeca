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
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        if (value){

            getLeftOperand().codeGenCond(compiler,value,e);
            getRightOperand().codeGenCond(compiler,value,e);

        } else {
            Label end_or = new Label("end_or");

            getLeftOperand().codeGenCond(compiler,!value,end_or);
            getRightOperand().codeGenCond(compiler,value,e);

            compiler.addLabel(end_or);
        }
    }
}