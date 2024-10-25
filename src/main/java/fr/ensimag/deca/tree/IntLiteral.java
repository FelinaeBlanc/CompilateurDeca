package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.DVal;
/**
 * Integer literal
 *
 * @author gl07
 * @date 21/04/2023
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
            this.setType(compiler.environmentType.INT);
            return compiler.environmentType.INT;   
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeExp(compiler,1);
        compiler.addInstruction(new WINT());
    }

    @Override
    protected DVal dval(){
        return new ImmediateInteger(value);
    }

    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        compiler.addInstruction(new LOAD(new ImmediateInteger(value),Register.getR(registreNb)));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeExp(compiler,2);
    }

    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}