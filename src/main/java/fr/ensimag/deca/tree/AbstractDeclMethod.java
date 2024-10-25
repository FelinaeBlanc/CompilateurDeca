package fr.ensimag.deca.tree;

import java.util.List;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Class declaration.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractDeclMethod extends Tree {

    protected AbstractIdentifier name;
    protected AbstractIdentifier type;
    protected ListDeclParam paramList;
    protected EnvironmentExp localEnv;
    protected abstract void optimizeDeclMethod(DecacCompiler compiler) throws ContextualError ;

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.
     */
    abstract public void verifyMethodBody(DecacCompiler compiler, ClassDefinition classDefinition, Type returnType) throws ContextualError ; 

    public AbstractIdentifier getName(){ return name; }
    public AbstractIdentifier getType(){ return type; }
    public boolean isAsm(){ return false; }

    //abstract public AbstractMethodBody getBody();
    
    abstract protected void codeGenDeclMethod(DecacCompiler compiler); 

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the Method  declaration is OK
     * without looking at its content.
     */

    public void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
            
        int index;
        this.localEnv = new EnvironmentExp(currentClass.getMembers());

        Type methodType = type.verifyType(compiler);
        Signature sig = new Signature();

        type.setDefinition(new TypeDefinition(methodType, type.getLocation()));
        paramList.verifyListDeclParam(compiler, this.localEnv, currentClass, sig);

        // Vérification si redefinition d'une méthode parent
        ClassDefinition superClass = currentClass.getSuperClass();
        if (superClass != null){

            EnvironmentExp superExp = superClass.getMembers();
            ExpDefinition parentDef = superExp.get(name.getName());
            // Vérifie si le parent a un élement qui porte le même nom, et si il est une méthode et possède la même signature
            if (parentDef != null){ // l'élément existe
                if (!parentDef.isMethod()) {
                    throw new ContextualError("Method name " + name.getName().getName() + " exist in parent but is not a Method !", this.getLocation() );
                } 

                MethodDefinition parentMethodDef = (MethodDefinition) parentDef;
                if (!compiler.environmentType.subtype(methodType,parentMethodDef.getType())){ // Si pas le même type de retour 
                    throw new ContextualError("Method " + name.getName().getName() + " exist in parent but don't have same return type", this.getLocation() );
                } else if (!parentMethodDef.getSignature().equals(sig)) { // Si pas même signature
                    throw new ContextualError("Method " + name.getName().getName() + " exist in parent but don't have same signature !", this.getLocation() );
                }

                index = parentMethodDef.getIndex(); // L'index de la méthode dans la vtable est la même que celle du parent si redefinie
            }  else {
                index = currentClass.getNumberOfMethods(); // Si pas redéfinie, alors sa position dans la table des méthodes est égale à +1
            }              
        } else {
            index = 0;
        }

        MethodDefinition def = new MethodDefinition(methodType, this.getLocation(), sig, index);
        List<AbstractDeclMethod> listVTable = currentClass.getVTable();

        if (listVTable.size() > (index) ){ // Si l'index existe !
            listVTable.set(index, this); // Ajout la méthode a la list ! (pas add, car elle peut overide une méthode de la liste)
        }else{
            listVTable.add(this);
        }

        def.setLabel(new Label("code."+currentClass.getType().getName().getName()+"."+name.getName().getName(),false));
        name.setDefinition(def);

        try {
            currentClass.getMembers().declare(name.getName(), def);
        } catch (DoubleDefException e) {
            throw new ContextualError("DeclMethod name already defined !", this.getLocation() );
        }

        // Ajoute la méthode à la liste globale !
        compiler.methodGlobalList.add(this);
        return;
    }
}