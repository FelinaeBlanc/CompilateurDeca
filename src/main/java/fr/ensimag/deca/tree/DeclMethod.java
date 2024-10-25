package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class DeclMethod extends AbstractDeclMethod{

    protected AbstractMethodBody body;

    public DeclMethod( AbstractIdentifier type, AbstractIdentifier name,ListDeclParam paramList, AbstractMethodBody  body) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(paramList);
        Validate.notNull(body);
        this.type = type;
        this.name = name;
        this.paramList = paramList;
        this.body = body;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("(");
        paramList.decompile(s);
        s.print(") {");
        s.println();
        s.indent();
        body.decompile(s);
        s.unindent();
        s.print("}");
    }

    // Passe 4, optimisation
    protected void optimizeDeclMethod(DecacCompiler compiler) throws ContextualError  {
        body.optimizeMethodBody(compiler);
    }

    public void codeGenInst(DecacCompiler compiler){
        int pos = -3;
        for (AbstractDeclParam param : paramList.getList()) {
            compiler.addComment(param.paramName.getName().toString() + " " +  pos + "(LB)");
            param.codeGenDeclParam(compiler, pos);
            pos--;
        }

        body.codeGenInst(compiler);
    }

    @Override
    public void verifyMethodBody(DecacCompiler compiler, ClassDefinition classDefinition, Type returnType) throws ContextualError {
        this.body.verifyMethodBody( compiler,  classDefinition.getMembers(), localEnv, classDefinition, returnType,  this.type.getType());
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        paramList.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        paramList.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    protected void codeGenDeclMethod(DecacCompiler compiler){
        Label methodLabel = ((MethodDefinition) name.getDefinition()).getLabel();
        compiler.addLabel(methodLabel);

        // Reset Gestionnaire Mémoire
        compiler.getGestionnaireMemoire().init(compiler,false);

        body.codeGenMethodBody(compiler);

        if (type.getType().isVoid()){ // Return est implicite, on le déclare au cas où, il se peut qu'il soit en doublon mais osef ça a l'air chiant a vérifier
            compiler.addInstruction(new RTS());
        } else { // Sinon il faut vérifier si ont sort de la méthode sans return !
            if (!compiler.getCompilerOptions().getNoCheck()) {
                Label noReturnLbl = compiler.getGestionnaireErreur().getErreur("no_return").getLabel();
                compiler.addInstruction(new BRA(noReturnLbl));
            }
            
        }

        compiler.getGestionnaireMemoire().buildStack(compiler); // Construit TSTO et ADDSP
    }
}
