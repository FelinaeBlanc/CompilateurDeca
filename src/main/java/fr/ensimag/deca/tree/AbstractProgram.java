package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl07
 * @date 21/04/2023
 *
 */
public abstract class AbstractProgram extends Tree {
    public abstract void optimizeProgram(DecacCompiler compiler) throws ContextualError;
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;
    public abstract void codeGenProgram(DecacCompiler compiler) ;
    public abstract void codeGenErrors(DecacCompiler compiler);

}
