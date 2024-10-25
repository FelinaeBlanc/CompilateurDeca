package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;

import fr.ensimag.ima.pseudocode.instructions.POP;

import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * @author gl07
 * @date 21/04/2023
 */
public class Cast extends AbstractExpr {
    final private AbstractIdentifier type;
    final private AbstractExpr operand;

    //TO DO ajouter le cast pour les classes

    public Cast(AbstractIdentifier type, AbstractExpr operand) {
        Validate.notNull(type);
        Validate.notNull(operand);
        this.type = type;
        this.operand = operand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(")");
        s.print("(");
        operand.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    String prettyPrintNode() {
        return "Cast ("+ type.getName().getName() +")" ;
    }


    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");

    }
    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    protected void codeGenPrint(DecacCompiler compiler) {
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2), GPRegister.getR(1)));

        Type castType = type.getType();
        if (castType.isInt()){
            compiler.addInstruction(new WINT());
        }else if (castType.isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        operand.codeExp(compiler, registreNb);

        GPRegister reg = GPRegister.getR(registreNb);
        Type leType = operand.getType();
        Type castToType = type.getType();

        if (leType.isFloat() && castToType.isInt()){
            compiler.addInstruction(new INT(reg,reg));
        } else if (leType.isInt() && castToType.isFloat()){
            compiler.addInstruction(new FLOAT(reg,reg));
        } else if (leType.isClass() &&  castToType.isClassOrNull()){
            compiler.addInstruction(new PUSH(GPRegister.getR(registreNb)));

            Label errorLabel = compiler.getGestionnaireErreur().getErreur("cast_illegal").getLabel();
            InstanceOf iof = new InstanceOf(operand, type);
            iof.codeExpGenCond(compiler,registreNb,false, errorLabel);

            compiler.addInstruction(new POP(GPRegister.getR(registreNb)));
        }
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type leftType = this.type.verifyType(compiler);
        setType(leftType);

        Type rightType = operand.verifyExpr(compiler, localEnv, currentClass);
        if (!compiler.environmentType.cast_compatible(rightType,leftType)){ throw new ContextualError("Cannot cast !",this.getLocation()); }



        return leftType;
    }

    @Override
    protected DVal dval(){
        return null;
    }

}
