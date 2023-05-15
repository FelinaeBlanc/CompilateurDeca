package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        
            Type t = getOperand().verifyExpr(compiler, localEnv, currentClass);
            if (t.isInt()) {
                return compiler.environmentType.INT;
            } else {
                throw new ContextualError("ConvFloat does not support type " + t.toString(), getLocation());
            }
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
