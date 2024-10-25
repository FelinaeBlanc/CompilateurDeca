package fr.ensimag.deca.context;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.TreeFunction;
public class UnknowVar extends AbstractExpr {

    public UnknowVar(){}

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }



    public void decompile(IndentPrintStream s) {}
    protected void iterChildren(TreeFunction f) {}
    protected void prettyPrintChildren(PrintStream s, String prefix) {}
}