package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;

public class Dot extends AbstractLValue{

    private AbstractExpr left;
    private AbstractIdentifier right;

    public AbstractExpr getLeftOperand() {
        return left;
    }
    public AbstractExpr getRightOperand() {
        return right;
    }

    public Dot(AbstractExpr left, AbstractIdentifier right){
        this.right = right;
        this.left = left;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        left.decompile(s);
        s.print(".");
        right.decompile(s);
    }


    @Override
    protected
    void iterChildren(TreeFunction f) {
        left.iter(f);
        right.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        left.prettyPrint(s, prefix, false);
        right.prettyPrint(s, prefix, false);
    }

    @Override
    String prettyPrintNode() {
        return "Dot ";
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        
        Type leftT = this.left.verifyExpr(compiler, localEnv, currentClass);
        if(!leftT.isClass()) {
            throw new ContextualError("Attempted to access a property of a non-class element", this.getLocation());
        }
        ClassType leftClass = leftT.asClassType("Identifier is not a class", this.getLocation());
        
        if( leftClass.getDefinition().getMembers().get(right.getName()) == null){
            throw new ContextualError("Can't find right member", this.getLocation());
        }

        Type rightT = right.verifyExpr(compiler, leftClass.getDefinition().getMembers(), currentClass);
        FieldDefinition fieldDef = right.getDefinition().asFieldDefinition("Can't access a non property", this.getLocation());
    

        if (fieldDef.getVisibility() == Visibility.PROTECTED ){
            if (currentClass == null){
                throw new ContextualError("Can't access protected property in main", this.getLocation());
            }

            if ( !compiler.environmentType.subtype(leftT, currentClass.getType()) || !compiler.environmentType.subtype(currentClass.getType(),fieldDef.getContainingClass().getType() ) ){// le type de la classe courante doit être un sous-type de la classe où le champ protégé est déclaré
                throw new ContextualError("Can't access protected property from a non subclass", this.getLocation());
            }
        }
        setType(rightT);    

        return rightT;
    }

    @Override
    protected void codeExp(DecacCompiler compiler, int registreNb) {
        left.codeExp(compiler,registreNb);
        RegisterOffset rg = new RegisterOffset(right.getFieldDefinition().getIndex() +1 , GPRegister.getR(registreNb));
        compiler.addInstruction(new LOAD(rg,GPRegister.getR(registreNb)));
        
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2),GPRegister.getR(1)));

        Type leType = getType();
        if (leType.isInt()){
            compiler.addInstruction(new WINT());
        }else if(leType.isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected void codeGenPrintHex(DecacCompiler compiler) {
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2),GPRegister.getR(1)));

        Type leType = getType();
        
        if(leType.isFloat()){
            compiler.addInstruction(new WFLOATX());
        }
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond(compiler, 2, value, e);
    }


    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        codeExp(compiler,registerNb); // On met la valeur dans le registre 2

        //CMP
        compiler.addInstruction(new CMP(new ImmediateInteger(!value), Register.getR(registerNb)));
        compiler.addInstruction(new BNE(e));
    }

    @Override
    protected DVal dval(){
        return null;
    }

}
