package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentVarValue;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractOpBool extends AbstractBinaryBoolExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr1 = this.getLeftOperand().optimizeExp(compiler, envVar); // On évalue toujours la partie gauche, mais pas la droite !

        envVar.invalidateEnv();
        AbstractExpr expr2 = this.getRightOperand().optimizeExp(compiler, envVar);

        if (expr1 instanceof BooleanLiteral && expr2 instanceof BooleanLiteral){
            return createBooleanLiteral(preCalculateBool( ((BooleanLiteral)expr1).getValue(),((BooleanLiteral)expr2).getValue()),compiler);
        }

        setLeftOperand(expr1);
        setRightOperand(expr2);
        
        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type type1  = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type type2  = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        
                if (!type1.isBoolean() || !type2.isBoolean()) {
                    throw new ContextualError(" Error operation " + getOperatorName() + "not supported for types : " + type1.toString() + " " + type2.toString()
                    , getLocation());
                    
                }

                this.setType(compiler.environmentType.BOOLEAN);
                return compiler.environmentType.BOOLEAN;
        
    }


}
