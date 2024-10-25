package fr.ensimag.deca.tree;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;

/**
 * read...() statement.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }


    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        codeGenInst(compiler,Register.getR(registreNb));
    }
    protected DVal dval(){
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else {
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected void codeGenPrintHex(DecacCompiler compiler) {
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        Type leType = getType();
        
        if(leType.isFloat()){
            compiler.addInstruction(new WFLOATX());
        }
    }
}
