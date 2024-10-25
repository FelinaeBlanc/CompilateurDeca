package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.IMAProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Class declaration.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractDeclClass extends Tree {


    protected AbstractIdentifier className;
    protected AbstractIdentifier superClass;
    protected ListDeclField fields;
    protected ListDeclMethod  methods;

    // Passe 4 optimisation
    protected abstract void optimizeDeclClass(DecacCompiler compiler) throws ContextualError;

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.
     */
    protected abstract void verifyClass(DecacCompiler compiler) throws ContextualError;

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler) throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler) throws ContextualError;


    protected abstract void codeGenVTable(DecacCompiler compiler); 

    protected abstract void codeGenInit(DecacCompiler compiler); 


}
