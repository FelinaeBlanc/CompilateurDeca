package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Class declaration.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractMethodBody extends Tree {
    ListDeclVar variables;
    ListInst instructions;

    public abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, ClassDefinition classDefinition, Type returnType, Type type)  throws ContextualError ;
    protected abstract void optimizeMethodBody(DecacCompiler compiler) throws ContextualError ;


    abstract public void codeGenInst(DecacCompiler compiler);
/*
    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.

    protected abstract void verifyField(DecacCompiler compiler)
            throws ContextualError;
    /*
    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     /
    protected abstract void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
     /
    protected abstract void verifyClassBody(DecacCompiler compiler)
            throws ContextualError;
*/

public ListDeclVar getVariables() {
    return variables;
}
public ListInst getInstructions() {
    return instructions;
}

public abstract void codeGenMethodBody(DecacCompiler compiler);
}
