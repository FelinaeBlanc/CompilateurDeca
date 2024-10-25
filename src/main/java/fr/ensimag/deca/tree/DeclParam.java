package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class DeclParam extends AbstractDeclParam {

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        paramName.decompile(s);
    }

    @Override
    public Type verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, int index) throws ContextualError {

        Type paramType = type.verifyType(compiler);
        if (paramType.isVoid()) {
            throw new ContextualError("DeclParam Type Invalide !", this.getLocation() );
        }
        ParamDefinition def = new ParamDefinition(paramType, getLocation(), index); 
        try {
            localEnv.declare(paramName.getName(), def);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("DeclParam name Invalide !", this.getLocation() );
        }
        Type name = paramName.verifyExpr(compiler, localEnv, currentClass);
        paramName.setDefinition(def);

        return paramType;
    }


    @Override
    public void codeGenDeclParam(DecacCompiler compiler, int posPile){
        // Assigne la var dans la l'espace suivante libre dans la pile ! + Fait la liaison uwu
        RegisterOffset registrePile = new RegisterOffset(posPile,Register.LB);
        paramName.getExpDefinition().setOperand(registrePile);
    }
/*
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

*/    @Override
protected
void iterChildren(TreeFunction f) {
    type.iter(f);
    paramName.iter(f);
}

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }


}
