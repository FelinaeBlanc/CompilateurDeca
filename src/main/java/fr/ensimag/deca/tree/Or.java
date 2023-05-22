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
            Label end_not_or = new Label("end_not_or");

            getLeftOperand().codeGenCond(compiler,value,end_not_or);
            getRightOperand().codeGenCond(compiler,value,e);

            compiler.addLabel(end_not_or);
        } else {
            getLeftOperand().codeGenCond(compiler,value,e);
            getRightOperand().codeGenCond(compiler,value,e);
        }
    }
}