package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.context.EnvironmentVarValue;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Return extends AbstractExpr {
    public AbstractExpr getExpr() {
        return expr;
    }

    private AbstractExpr expr;

    public Return(AbstractExpr expr) {
        this.expr = expr;
    }

    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        if (!expr.getType().isVoid()){ // On optimise seulement si il y'a une expression
            this.expr = expr.optimizeExp(compiler, envVar);
        }
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        Type settype;

        if (returnType.isVoid()) {
            throw new ContextualError("You can't return something with void type", this.getLocation());
        } 

        try {
            this.expr = this.expr.verifyRValue(compiler, localEnv, currentClass, returnType);
            settype = this.expr.getType();
        } catch (Exception e) {
            throw e; 
        }

        if (!settype.sameType(returnType)) {
            throw new ContextualError("The expression returned is not the same as the method declaration", this.getLocation()); 
        }

        setType(settype); 

    }
    
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.expr.codeExp(compiler, 2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2), GPRegister.R0));
        compiler.addInstruction(new RTS());
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    String prettyPrintNode() {
        return "Return";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        expr.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyExpr'");
    }

}