package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Null extends AbstractExpr {


    public Null() {

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.NULL);
        return getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "Null ()";
    }


    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");

    }
    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        compiler.addInstruction(new LOAD(new NullOperand(),Register.getR(registreNb)));
    }

    protected DVal dval(){
        return new NullOperand();
    }

}
