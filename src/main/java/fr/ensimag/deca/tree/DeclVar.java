package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type varType = type.verifyType(compiler);
        if (varType.isVoid()) {
            throw new ContextualError("DeclVar Type Invalide !", this.getLocation() );
        }
        try {
            localEnv.declare(varName.getName(), new VariableDefinition(varType, getLocation()));
        } catch (DoubleDefException e) {
            throw new ContextualError("DeclVar name Invalide !", this.getLocation() );
        }

        Type name = varName.verifyExpr(compiler, localEnv, currentClass);
        initialization.verifyInitialization(compiler, varType, localEnv, currentClass);


        return;
    }

    @Override
    public void codeGenDeclVar(DecacCompiler compiler, int posPile){
        // Assigne la var dans la l'espace suivante libre dans la pile ! + Fait la liaison uwu
        RegisterOffset registrePile = new RegisterOffset(posPile,Register.LB);
        varName.getExpDefinition().setOperand(registrePile);
        
        // Si initialisation =>
        if (initialization instanceof Initialization){
            AbstractExpr expr = ((Initialization) initialization).getExpression();
            expr.codeGenInst(compiler); // LOAD
            compiler.addInstruction(new STORE(Register.R1,registrePile));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        varName.decompile(s);
        s.print("=");
        initialization.decompile(s);
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
