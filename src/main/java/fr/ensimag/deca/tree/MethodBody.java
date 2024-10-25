package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentVarValue;

import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody{

    public MethodBody(ListDeclVar variables, ListInst instructions){
        assert(instructions != null);
        this.variables=variables;
        this.instructions = instructions;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        variables.decompile(s);
        instructions.decompile(s);
    }

    // Passe 4, optimisation
    protected void optimizeMethodBody(DecacCompiler compiler) throws ContextualError {

        EnvironmentVarValue envVar = new EnvironmentVarValue();

         // Passe 1 (Constant Folding)
        variables.optimizeListDeclVariable(compiler, envVar);
        instructions.optimizeListInst(compiler, envVar);

        // Passe 2 (Reduction du code mort)
        variables.rmDeadCodeDeclVariable(compiler, envVar); // envVar est utilisé pour savoir si une variable est déclaré à un moment
        instructions.rmDeadCodeListInst(compiler,envVar);
    }

    public void codeGenInst(DecacCompiler compiler){
        variables.codeGenListDeclVariable(compiler);
        instructions.codeGenListInst(compiler);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        variables.iter(f);
        instructions.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        variables.prettyPrint(s, prefix, false);
        instructions.prettyPrint(s, prefix, true);
    }

    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, ClassDefinition classDefinition, Type returnType, Type type) throws ContextualError {
        this.variables.verifyMethodListDeclVariable(compiler, envExp,  envExpParams, classDefinition);
        this.instructions.verifyListInst(compiler, envExpParams, classDefinition, returnType);
    }

    public void codeGenMethodBody(DecacCompiler compiler){
        variables.codeGenListDeclVariable(compiler);
        instructions.codeGenListInst(compiler);
    }
}
