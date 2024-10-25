package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.deca.tree.BooleanLiteral;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import java.util.List;
/**
 * Full if/else if/else statement.
 *
 * @author gl07
 * @date 21/04/2023
 */
public class IfThenElse extends AbstractInst {
    
    private AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        this.condition = this.condition.optimizeExp(compiler, envVar);

        envVar.invalidateEnv(); // Invalide l'env, on ne connait plus les valeurs des éléments pour la suite

        // Optimise aussi les lists inst de ses branches !
        this.thenBranch.optimizeListInst(compiler, envVar);
        this.elseBranch.optimizeListInst(compiler, envVar);

        envVar.invalidateEnv(); // Invalide l'env, on ne connait plus les valeurs des éléments
    }
    @Override
    protected List<AbstractInst> optimizeInsts(){
        if (this.condition instanceof BooleanLiteral){
            boolean val = ((BooleanLiteral)this.condition).getValue();
            if (val){
                return this.thenBranch.getList();
            }else{
                return this.elseBranch.getList();
            }
        }
        return null;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
                ;
            condition.verifyCondition(compiler, localEnv, currentClass);
            thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
            elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

        Label elseBlLabel = new Label("else");
        Label endLabel = new Label("end_if");
        condition.codeGenCond(compiler, false,elseBlLabel);
        thenBranch.codeGenListInst(compiler);
        
        compiler.addInstruction(new BRA(endLabel));
        
        compiler.addLabel(elseBlLabel);
        elseBranch.codeGenListInst(compiler);

        compiler.addLabel(endLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.print("} ");
        if (elseBranch.getList().size() != 0) {
            s.println("else {");
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
            s.print("}");
        }
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
