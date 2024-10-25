package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.deca.context.EnvironmentVarValue;
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
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr = this.getOperand().optimizeExp(compiler, envVar);


        if (expr instanceof BooleanLiteral){
            return createBooleanLiteral( !((BooleanLiteral)expr).getValue(),compiler);
        }
        setOperand(expr);
        
        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError {
        Type t = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (t.isBoolean()) {
            setType(t);
            return compiler.environmentType.BOOLEAN;
            
        }else{
            throw new ContextualError("Not does not support type " + t.toString(), getLocation());
        }
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond(compiler,2,value,e);
    }

    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        getOperand().codeExpGenCond(compiler,registerNb,!value,e); // Inverse value !
    }

    // cas spécifique du not
    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        Label elseLabel = new Label("condition_false");
        Label endLabel = new Label("condition_end");

        codeExpGenCond(compiler,registreNb, false, elseLabel);
        // Condition vrai, (store 1)
        compiler.addInstruction(new LOAD(new ImmediateInteger(true),GPRegister.getR(registreNb)));
        compiler.addInstruction(new BRA(endLabel));
        
        // Condition faux (store 0)
        compiler.addLabel(elseLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(false),GPRegister.getR(registreNb)));
        // Label end
        compiler.addLabel(endLabel);
    }



    @Override
    protected String getOperatorName() {
        return "!";
    }
}
