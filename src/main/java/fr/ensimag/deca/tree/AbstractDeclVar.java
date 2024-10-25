package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;

/**
 * Variable declaration
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractDeclVar extends Tree {
    
    protected abstract void optimizeDeclVar(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError;
    protected abstract Symbol getName();
    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    
    protected abstract void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;


    public abstract void codeGenDeclVar(DecacCompiler compiler);

    abstract public void verifyMethodListDeclVariable(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, ClassDefinition classDefinition) throws ContextualError;
}
