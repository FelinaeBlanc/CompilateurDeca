package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(t);
        getRightOperand().verifyRValue(compiler, localEnv, currentClass, t);

        return t;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getRightOperand().codeGenInst(compiler);
        compiler.addInstruction(new STORE(Register.R1,getLeftOperand().getDAddr()));
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
