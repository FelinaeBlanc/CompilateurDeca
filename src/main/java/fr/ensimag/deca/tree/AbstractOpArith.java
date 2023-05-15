package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1  = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2  = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (type1.isInt() && type2.isInt()){
            return compiler.environmentType.INT;
        }else if (type1.isInt() && type2.isFloat()) {
            return compiler.environmentType.INT;
        } else if (type1.isFloat() && type2.isInt()) {
            return compiler.environmentType.FLOAT;
        }else if (type1.isFloat() && type2.isFloat()) {
            return compiler.environmentType.FLOAT;
        }else {
            throw new ContextualError("Error unsupported types", getLocation());
        }

    }
}
