package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField i : getList()) {
            i.decompile(s);
            s.println();
        }
    }

    void optimizeListDeclField(DecacCompiler compiler) throws ContextualError {
        EnvironmentVarValue envVar = new EnvironmentVarValue();
        for (AbstractDeclField f : getList()){
            f.optimizeDeclField(compiler,envVar);
        }
    }
    
    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclFields(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError {

        throw new UnsupportedOperationException("not yet implemented");/*
        for (AbstractDeclField d : getList()){
            d.verifyDeclVar(compiler,localEnv,currentClass);
        }*/
    }

    void codeGenListDeclField(DecacCompiler compiler ) {
        for (AbstractDeclField d : getList()){
            d.codeGenDeclField(compiler);
        }
    }
}
