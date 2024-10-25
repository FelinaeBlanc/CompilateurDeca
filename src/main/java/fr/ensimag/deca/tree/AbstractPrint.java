package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.context.EnvironmentVarValue;
import java.util.List;


/**
 * Print statement (print, println, ...).
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        List<AbstractExpr> laListe = getArguments().getList();

        for (int i = 0; i < laListe.size(); i++) {
            getArguments().set(i, laListe.get(i).optimizeExp(compiler, envVar));
        }

    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass, Type returnType) throws ContextualError {

        for (AbstractExpr a : getArguments().getList()) {
            
            Type t = a.verifyExpr(compiler, localEnv, currentClass);
            if (a instanceof Identifier){
            // Vérifie que l'identifiant est bien une variable
                Identifier ident = (Identifier)a;
                Definition def = ident.getDefinition();
                if (!def.isExpression()){
                    throw new ContextualError("ERREUR ARGUMENT PRINT NON VALIDE", getLocation());
                }

            }

            if (!t.isInt() && !t.isFloat() && !t.isString()){
                throw new ContextualError("ERREUR ARGUMENT PRINT NON VALIDE", getLocation());
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        
        for (AbstractExpr a : getArguments().getList()) {
            if (printHex && a.getType().isFloat()) {
                a.codeGenPrintHex(compiler); 
            }
            else {
                a.codeGenPrint(compiler);
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print" + getSuffix() + "(");
        getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
