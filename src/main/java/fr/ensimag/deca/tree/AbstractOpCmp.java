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
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type type1  = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type type2  = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                setType(compiler.environmentType.BOOLEAN);

                String op = getOperatorName();

                switch (op) {
                    case ">":
                    case "<":
                    case ">=":
                    case "<=":
                        if ((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat())){
                            return compiler.environmentType.BOOLEAN;
                        }
                        
                        break;

                    case "==":
                    case "!=":
                        if ( type1.equals(type2) ){
                            return compiler.environmentType.BOOLEAN;
                        }
                        break;

                
                    default:
                        break;
                }
                throw new ContextualError(" Error operation " + getOperatorName() + "not supported for types : " + type1.toString() + " " + type2.toString()
                        , getLocation());
    }


}
