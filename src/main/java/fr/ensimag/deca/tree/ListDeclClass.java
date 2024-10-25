package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("Verification de la liste des classes en cours... ");
        for (AbstractDeclClass c : getList()) {
            LOG.trace("Verification de la classe : " + c.className.getName().toString()); 
            c.verifyClass(compiler);
            LOG.trace("Verification de la classe " + c.className.getName().toString() + " terminé ! "); 
        }
        LOG.trace("Verification de la liste des classes terminée ! ");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    // Pas appelé /!\ (elle est traité automatiquement dans le verifyClass)
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError { 
        for (AbstractDeclClass c : getList()) {
            c.verifyClassMembers(compiler);
        }
    }

        
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("Verification du corps des classes en cours...");
        for (AbstractDeclClass c : getList()) {
            LOG.trace("Verification du corps de la classe : " + c.className.getName().getName().toString()); 
            c.verifyClassBody(compiler);
            LOG.trace("Verification du corps de la classe " + c.className.getName().getName().toString() + " terminé ! "); 
        }
        LOG.trace("Verification du corps des classes terminée !");
    }



     // Passe 4 optionnel,  optimisation 
     public void optimizeClasses(DecacCompiler compiler) throws ContextualError {
        LOG.trace("Optimisation des classes en cours...");
        for (AbstractDeclClass c : getList()) {
            String className = c.className.getName().getName();
            LOG.trace("Optimisation de la classe "+className);
            c.optimizeDeclClass(compiler);
            LOG.trace("Optimisation de la classe "+className+" terminé ! ");
        }
        LOG.trace("Optimisation des classes terminée !");
     }


    public void codeGenCreateVtable(DecacCompiler compiler) {

        LOG.debug("Generation de la table des methodes en cours..."); 

        compiler.addComment("--------------------------------------------------"); 
        compiler.addComment("Construction des tables des methodes"); 
        compiler.addComment("--------------------------------------------------");


        // Réserve les deux premiers registre pour l'objet Objet
        RegisterOffset regBase =  compiler.getGestionnaireMemoire().getNextRegister();
        RegisterOffset regMethodEquals =  compiler.getGestionnaireMemoire().getNextRegister();


        compiler.addInstruction(new LOAD(new NullOperand(),GPRegister.R0)); // Base
        compiler.addInstruction(new STORE(GPRegister.R0,regBase));

        Label labelEquals = compiler.environmentType.OBJECT.getDefinition().getVTable().get(0).getName().getMethodDefinition().getLabel();
        compiler.addInstruction(new LOAD(new LabelOperand(labelEquals),GPRegister.R0)); // Method equals
        compiler.addInstruction(new STORE(GPRegister.R0,regMethodEquals));

        compiler.environmentType.OBJECT.getDefinition().setAddrVtable(regBase); // Enregistre sa position dans la vtable

        // Génère les Vtable des autres classes
        for (AbstractDeclClass c : getList()) {
            LOG.trace("Implementation dans la Vtable de la classe " + c.className.getName().getName().toString() + " en cours..."); 
            c.codeGenVTable(compiler);
            LOG.trace("Implementation dans la Vtable de la classe " + c.className.getName().getName().toString() + " terminé ! "); 
        }

        LOG.trace("Generation de la table des methodes terminée!"); 
    }

   

    public void codeGenMethods(DecacCompiler compiler)
    {

        LOG.debug("Generation des methodes en cours...");  
        compiler.addComment("--------------------------------------------------"); 
        compiler.addComment("Construction des methodes"); 
        compiler.addComment("--------------------------------------------------");


        // Génère le code de Object equals
        Label labelFaux = new Label("Equals_Faux",true);
        Label labelEquals = compiler.environmentType.OBJECT.getDefinition().getVTable().get(0).getName().getMethodDefinition().getLabel();

        compiler.addLabel(labelEquals);
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB),GPRegister.getR(0))); // Charge le param dans R0
        compiler.addInstruction(new CMP(new RegisterOffset(-2, Register.LB), GPRegister.getR(0))); // Compare this et l'objet passer en paramètre
        compiler.addInstruction(new BNE(labelFaux));


        compiler.addInstruction(new LOAD(new ImmediateInteger(true),GPRegister.getR(0)));
        compiler.addInstruction(new RTS());

        compiler.addLabel(labelFaux);
        compiler.addInstruction(new LOAD(new ImmediateInteger(false),GPRegister.getR(0)));
        compiler.addInstruction(new RTS());
        

        for (AbstractDeclMethod method : compiler.methodGlobalList) {
            LOG.trace("Generation du code de la méthode " + method.name.getName().toString() + " en cours..."); 
            method.codeGenDeclMethod(compiler);
            LOG.trace("Generation du code de la methode " + method.name.getName().toString() + " terminé !" ); 
        }

        LOG.trace("Generation des methodes terminé");
    }

    public void codeGenInits(DecacCompiler compiler){

        LOG.debug("Generation des inits en cours...");  

        compiler.addComment("--------------------------------------------------"); 
        compiler.addComment("Construction des inits"); 
        compiler.addComment("--------------------------------------------------");

        // Génère le code de Object equals
        Label initLbl = compiler.environmentType.OBJECT.getDefinition().getInitLabel();
        
        compiler.addLabel(initLbl);
        compiler.addInstruction(new RTS());

        
        for (AbstractDeclClass c : getList()) {
            LOG.trace("Generation des inits de la classe " + c.className.getName().getName().toString() + " en cours..."); 
            c.codeGenInit(compiler); 
            LOG.trace("Generation des inits de la classe " + c.className.getName().getName().toString() + " terminé ! "); 
        }
        LOG.trace("Generation des inits terminés");  
    }


}
