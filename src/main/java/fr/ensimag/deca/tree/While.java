package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.lang.Validate;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.ima.pseudocode.instructions.BRA;
/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        // On pourrait faire un detection spécifique des Variables à des-évalué / et de une optimisation de la condition qui modifie seulement si c'est un boolean mais pas le temps !
        //envVar.invalidateEnv();

        envVar.invalidateEnv();
        this.condition = this.condition.optimizeExp(compiler, envVar);

        envVar.invalidateEnv();
        this.body.optimizeListInst(compiler, envVar);
        envVar.invalidateEnv();

    }
    @Override // La boucle est fausse, on ne rentrera jamais dedans... On la vire !
    protected List<AbstractInst> optimizeInsts() throws ContextualError {
        if (this.condition instanceof BooleanLiteral){
            boolean val = ((BooleanLiteral)this.condition).getValue();
            if (!val){
                return (new ListInst()).getList(); // On ne garde rien, ni le while, on retourne une liste vide
            }
        }
        return null;
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label start_while = new Label("while");
        compiler.addLabel(start_while);

        Label end_while = new Label("end_while");
        condition.codeGenCond(compiler, false, end_while);
        
        body.codeGenListInst(compiler);

        compiler.addInstruction(new BRA(start_while));
        compiler.addLabel(end_while);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
                condition.verifyCondition(compiler, localEnv, currentClass);
                body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
