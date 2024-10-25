package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.UndefinedVar;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * @author gl07
 * @date 21/04/2023
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected Symbol getName(){
        return this.varName.getName();
    }

    @Override
    protected void optimizeDeclVar(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        // Si initialisation seulement =>
        if (initialization instanceof Initialization){
            initialization.optimizeInitialization(compiler,envVar);
            
            // Set dans l'env des valeurs des vars la valeur
            envVar.initValue(varName.getName(), ((Initialization) initialization).getExpression());
        } else {
            UndefinedVar undefVar = new UndefinedVar();
            undefVar.setLocation(getLocation());

            envVar.initValue(varName.getName(), undefVar);
        }
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type varType = type.verifyType(compiler);
        if (varType.isVoid()) {
            throw new ContextualError("DeclVar Type Invalide !", this.getLocation() );
        }
        
        initialization.verifyInitialization(compiler, varType, localEnv, currentClass);

        try {
            localEnv.declare(varName.getName(), new VariableDefinition(varType, getLocation()));
        } catch (DoubleDefException e) {
            throw new ContextualError("L'identifier " + varName.getName().getName() + " est utilisé plusieurs fois.", this.getLocation() );
        }
        Type name = varName.verifyExpr(compiler, localEnv, currentClass);

        return;
    }

    @Override
    public void verifyMethodListDeclVariable(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, ClassDefinition classDefinition) throws ContextualError {
        Type varType = type.verifyType(compiler);
        if (varType.isVoid()) {
            throw new ContextualError("DeclVar Type Invalide !", this.getLocation() );
        }
        try {
            envExpParams.declare(varName.getName(), new VariableDefinition(varType, getLocation()));
        } catch (DoubleDefException e) {
            throw new ContextualError("L'identifier " + varName.getName().getName() + " est utilisé plusieurs fois.", this.getLocation() );
        }
        Type name = varName.verifyExpr(compiler, envExpParams, classDefinition);
        initialization.verifyInitialization(compiler, varType, envExpParams, classDefinition);

    }
    @Override
    public void codeGenDeclVar(DecacCompiler compiler){
        // Assigne la var dans la l'espace suivante libre dans la pile ! + Fait la liaison uwu
        RegisterOffset registrePile = compiler.getGestionnaireMemoire().getNextRegister();
        varName.getExpDefinition().setOperand(registrePile);
        
        // Si initialisation =>
        if (initialization instanceof Initialization){
            AbstractExpr expr = ((Initialization) initialization).getExpression();
            expr.codeExp(compiler,2);
            compiler.addInstruction(new STORE(Register.R2,registrePile));
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        if (initialization instanceof Initialization){
            s.print(" = ");
            initialization.decompile(s);
        }
        s.print(";");
    } 

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
