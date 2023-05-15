package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type t = getOperand().verifyExpr(compiler, localEnv, currentClass);
                if (t.isBoolean()) {
                    setType(t);
                    return compiler.environmentType.BOOLEAN;
                    
                }else{
                    throw new ContextualError("Not does not support type " + t.toString(), getLocation());
                }
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
