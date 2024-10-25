package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class This extends AbstractExpr {


    public This() {

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        if (currentClass == null) {
            if (getLocation() == null) {
                throw new ContextualError("You can't use 'this' in this block.", Location.BUILTIN);
            } else {
                throw new ContextualError("You can't use 'this' in this block.", getLocation());
            }
        }

        setType(currentClass.getType());
        return currentClass.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
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
        return "This ";
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
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(registreNb)));
    }

    @Override
    protected DVal dval(){
        return null;
    }

}
