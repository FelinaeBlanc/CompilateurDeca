package fr.ensimag.deca.tree;


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
    protected void codeGenCond(DecacCompiler compiler, bool value, Label e) {
        leftOperand.codeGenCond(compiler,value,e);
        rightOperand.codeGenCond(compiler,value,e);
    }
}