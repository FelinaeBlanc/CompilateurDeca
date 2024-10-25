package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.ima.pseudocode.IMAProgram;

/**
 * Class declaration.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractDeclField extends Tree {



    protected AbstractIdentifier type;
    protected AbstractIdentifier name;
    protected AbstractInitialization initialization;
    protected Visibility visibility;



    // Passe 1, déclarer le field
    protected abstract void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;

    /**
     * 
     * Pass 2, vérifie et déclare le contenu du field !
     * 
     * 
     */
    protected abstract void verifyInitField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;

    

    protected abstract void codeGenDeclField(DecacCompiler compiler);
    protected abstract void codeGenDeclFieldDefault(DecacCompiler compiler);
    protected abstract void optimizeDeclField(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError ;
}
