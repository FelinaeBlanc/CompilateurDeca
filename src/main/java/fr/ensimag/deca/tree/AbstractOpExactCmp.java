package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        try { 
            super.verifyExpr(compiler, localEnv, currentClass);

        } catch (ContextualError e) {
            // if types are neither floats or int
            Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

            if ( (!type1.isBoolean() || !type2.isBoolean())  && ( !type1.isClassOrNull() || !type2.isClassOrNull())) {
                throw new ContextualError("Type error : incompatible types for comparaison.", getLocation());
            }
        } 

        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }
}
