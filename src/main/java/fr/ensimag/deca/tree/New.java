package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class New extends AbstractExpr {

    private AbstractIdentifier identifier;
    public New(AbstractIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type;
        try{
            type = identifier.verifyType(compiler);
        } catch (ContextualError e){
            throw e;
        }
        if(!type.isClass()){
            throw new ContextualError("Identifier isn't a class.", this.getLocation());
        }
        this.setType(type);
        return type;
    }

    @Override
    protected void codeExp(DecacCompiler compiler, int registreNb) {
        codeGenInst(compiler, Register.getR(registreNb));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        identifier.decompile(s);
        s.print("()");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        identifier.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        identifier.prettyPrint(s, prefix, false);
    }

    @Override
    String prettyPrintNode() {
        return "New (" + identifier.prettyPrintNode() +")";
    }


    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {

        DAddr vTable = this.identifier.getClassDefinition().getAddrVtable();  
        
        compiler.addInstruction(new NEW(identifier.getClassDefinition().getNumberOfFields() + 1, R));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(compiler.getGestionnaireErreur().getErreur("tas_plein").getLabel()));
        }
        compiler.addInstruction(new LEA(vTable, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, R)));
        compiler.addInstruction(new PUSH(R));
        compiler.addInstruction(new BSR(identifier.getClassDefinition().getInitLabel()));
        compiler.addInstruction(new POP(R));
        //compiler.addInstruction(new STORE(R,new RegisterOffset(7, Register.GB)));
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
