package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;

import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;


/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr1  = this.getOperand().optimizeExp(compiler, envVar);
        this.setOperand(expr1);

        if ( expr1 instanceof FloatLiteral){
            return expr1;
        }

        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = new FloatType(compiler.symbolTable.create("float"));
        this.setType(t);
        return t;  
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        compiler.addInstruction(new FLOAT(R,R));
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
