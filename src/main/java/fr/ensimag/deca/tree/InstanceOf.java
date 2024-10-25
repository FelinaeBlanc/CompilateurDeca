package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class InstanceOf extends AbstractBinaryBoolExpr {

    // leftOperand = objet
    // rightOperad == class
    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        //verifier qu'a gauche c'est un objet et a droite c'est une classe
        
        Type type1  = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2  = ( (AbstractIdentifier) getRightOperand()).verifyType(compiler);

        if ((!type1.isClass() && !type1.isNull()) || !type2.isClass()) {
            throw new ContextualError("L'élément de gauche de instanceOf doit être un objet et celui de droite, une classe", getLocation());
        }
        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond(compiler, 2, value, e);
    }

    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        getLeftOperand().codeExp(compiler,registerNb); // Met la ref de la classe dans R2
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(registerNb)), Register.R1)); // Prend la ref dans la table des méthodes

        // Début boucle
        Label loop_instance_of = new Label("loop_instance_of");
        Label fin_loop_instance_of = new Label("fin_loop_instance_of");

        compiler.addLabel(loop_instance_of);
        compiler.addInstruction(new LEA(((ClassDefinition) ( (AbstractIdentifier) getRightOperand()).getDefinition()).getAddrVtable(), Register.R0));
        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        if(value) {
            compiler.addInstruction(new BNE(e));
        } else {
            compiler.addInstruction(new BEQ(fin_loop_instance_of));
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R1), Register.R1));
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BNE(loop_instance_of));
        compiler.addInstruction(new BRA(e)); // NUll ! on va vers le else

        compiler.addLabel(fin_loop_instance_of);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        getLeftOperand().decompile(s);
        s.print(" instanceof ");
        getRightOperand().decompile(s);
    }
    

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        getLeftOperand().prettyPrint(s, prefix, false);
        getRightOperand().prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        getLeftOperand().iter(f);
        getRightOperand().iter(f);
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }
}
