package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.INT);
        return compiler.environmentType.INT;
    }

    @Override
    public void codeGenInst(DecacCompiler compiler){
        compiler.addInstruction(new RINT());
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(compiler.getGestionnaireErreur().getErreur("io_error").getLabel()));
        }
        compiler.addInstruction(new LOAD(Register.getR(1), GPRegister.R2));
    }

    public void codeGenInst(DecacCompiler compiler, int lastFreeReg){
        compiler.addInstruction(new RINT());
        if (!compiler.getCompilerOptions().getNoCheck()) {
          compiler.addInstruction(new BOV(compiler.getGestionnaireErreur().getErreur("io_error").getLabel()));      
        } 
        compiler.addInstruction(new LOAD(Register.getR(1), Register.getR(lastFreeReg)));
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        compiler.addInstruction(new RINT());
        if (!compiler.getCompilerOptions().getNoCheck()) {
           compiler.addInstruction(new BOV(compiler.getGestionnaireErreur().getErreur("io_error").getLabel()));     
        } 
        compiler.addInstruction(new LOAD(Register.R1, R));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
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
