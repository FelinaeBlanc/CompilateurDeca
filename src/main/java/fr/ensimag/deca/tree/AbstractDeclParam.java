package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Class declaration.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractDeclParam extends Tree {



    protected AbstractIdentifier type;
    protected AbstractIdentifier paramName;

    public abstract Type verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, int index)
            throws ContextualError;

    abstract public void codeGenDeclParam(DecacCompiler compiler, int posPile);
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
}
