package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.rawAsm;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.List;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class DeclMethodAsm extends AbstractDeclMethod{
    
    protected String asmCode;

    public DeclMethodAsm( AbstractIdentifier type, AbstractIdentifier name,ListDeclParam paramList, String asmCode) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(paramList);
        Validate.notNull(asmCode);
        this.type = type;
        this.name = name;
        this.paramList = paramList;
        this.asmCode = asmCode;
    }

    // Ne fait rien, c'est à la charge du programmer de bien faire son code.
    protected void optimizeDeclMethod(DecacCompiler compiler) throws ContextualError  {} 
    

    @Override
    public void decompile(IndentPrintStream s) {

        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("() ");
        s.print("asm(");
        s.print("\"");

        
        String[] caracteresSpeciaux = {"\\","\""};

        for (int i = 0 ; i < caracteresSpeciaux.length ; i++){
            asmCode = asmCode.replace(caracteresSpeciaux[i],"\\"+ caracteresSpeciaux[i]);
        }
        s.print(asmCode);

        s.print("\");");
    }


    public void codeGenDeclMethod(DecacCompiler compiler){

        String str = this.asmCode;
        // Remplacer \\n par un saut à la ligne
        str = str.replaceAll("\\\\n", "\n");
        str = str.replaceAll("\\\\\\\\", "\\");
        str = str.replaceAll("\\\\\"", "\"");




        Label methodLabel = ((MethodDefinition) name.getDefinition()).getLabel();
        compiler.addLabel(methodLabel);

        compiler.add(new rawAsm(str + "\n"));


        // Le return est gérer par le programmeur directement dans ASM, on a juste a vérifier si on en sort
        if (!compiler.getCompilerOptions().getNoCheck()) {
            Label noReturnLbl = compiler.getGestionnaireErreur().getErreur("no_return").getLabel();
            compiler.addInstruction(new BRA(noReturnLbl));
        }
    }


    @Override
    public void verifyMethodBody(DecacCompiler compiler, ClassDefinition classDefinition, Type returnType) throws ContextualError {
        // Aucune Vérification dans le corp de Asm (selon la doc page 95)
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    public boolean isAsm(){ return true; }

}
