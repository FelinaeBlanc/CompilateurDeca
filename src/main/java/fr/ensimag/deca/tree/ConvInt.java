package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.ima.pseudocode.instructions.INT;


/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class ConvInt extends AbstractUnaryExpr {
    
    public ConvInt(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = compiler.environmentType.INT ;
        this.setType(t);
        return t;  
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr1  = this.getOperand().optimizeExp(compiler, envVar);
        this.setOperand(expr1);

        if ( expr1 instanceof IntLiteral){
            return expr1;
        }

        return this;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        compiler.addInstruction(new INT(R,R));
    }

    @Override
    protected String getOperatorName() {
        return "/* conv int */";
    }

}
