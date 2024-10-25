package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentVarValue;

import fr.ensimag.deca.context.EnvironmentVarValue;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl07
 * @date 21/04/2023
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void optimizeMain(DecacCompiler compiler) throws ContextualError{
        LOG.debug("optimize Main: start");
        
        // Passe 1 (Constant Folding)
        EnvironmentVarValue envVar = new EnvironmentVarValue(); // Env pour la valeur actuel des variables.
        declVariables.optimizeListDeclVariable(compiler, envVar); // Remplie envVar
        insts.optimizeListInst(compiler, envVar); // Remplie envVar

        // Passe 2 (Reduction du code mort)
        declVariables.rmDeadCodeDeclVariable(compiler, envVar); // envVar est utilisé pour savoir si une variable est déclaré à un moment
        insts.rmDeadCodeListInst(compiler,envVar);

        LOG.debug("optimize Main: end");
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");

        EnvironmentExp env_exp = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler,env_exp,null);
        insts.verifyListInst(compiler,env_exp, null,compiler.environmentType.VOID);

        LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        compiler.addComment("Beginning of main instructions:");
        compiler.addComment("Beginning of var declaration:");
        declVariables.codeGenListDeclVariable(compiler);

        compiler.addComment("Beginning of Instructions:");
        insts.codeGenListInst(compiler);        
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
