package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.OPP;

import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl07
 * @date 21/04/2023
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr = this.getOperand().optimizeExp(compiler, envVar);


        if (expr instanceof FloatLiteral){
            return createFloatLiteral( -((FloatLiteral)expr).getValue(),compiler);
        }else if (expr instanceof IntLiteral){
            return createIntLiteral( -((IntLiteral)expr).getValue(),compiler);
        }
        setOperand(expr);
        
        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
                Type t = getOperand().verifyExpr(compiler, localEnv, currentClass);
                if (t.isFloat()) {
                    setType(t);
                    return compiler.environmentType.FLOAT;
                }else if (t.isInt()){
                    setType(t);
                    return compiler.environmentType.INT;
                }else{
                    throw new ContextualError("Minus does not support type " + t, getLocation());
                }
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        compiler.addInstruction(new OPP(R,R));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else {
            compiler.addInstruction(new WFLOAT());
        }
    }

     @Override
    protected void codeGenPrintHex(DecacCompiler compiler){
         codeExp(compiler,2);

        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        Type leType = getType();
        
        if(leType.isFloat()){
            compiler.addInstruction(new WFLOATX());
        }
    }

}
