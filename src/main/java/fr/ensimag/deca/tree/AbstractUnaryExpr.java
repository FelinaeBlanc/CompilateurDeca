package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.DVal;

/**
 * Unary expression.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {


    private AbstractExpr operand;

    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }


    protected abstract String getOperatorName();
  
    public AbstractExpr getOperand() {
        return operand;
    }
    public void setOperand(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        s.print("(");
        this.getOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected DVal dval(){
        return null; 
    }


    // pour les Expressions booleen
    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        // On appel codeGenInst avec les deux registre pour faire l'instruction (dans ce registre)

        getOperand().codeExp(compiler, registreNb);
        codeGenInst(compiler,Register.getR(registreNb));
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
