package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.deca.tools.IndentPrintStream;

public abstract class AbstractBinaryBoolExpr extends AbstractBinaryExpr {

    public AbstractBinaryBoolExpr(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr1 = this.getLeftOperand().optimizeExp(compiler, envVar);
        AbstractExpr expr2 = this.getRightOperand().optimizeExp(compiler, envVar);

        if (expr1 instanceof BooleanLiteral && expr2 instanceof BooleanLiteral){
            return createBooleanLiteral(preCalculateBool( ((BooleanLiteral)expr1).getValue(),((BooleanLiteral)expr2).getValue()),compiler);
        }

        setLeftOperand(expr1);
        setRightOperand(expr2);
        
        return this;
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeExp(compiler, 2);
    }

    @Override
    protected void codeExp(DecacCompiler compiler, int registerNb){ // Génère la condition, et met le résultat dans le bon registre

        Label elseLabel = new Label("condition_false");
        Label endLabel = new Label("condition_end");

        codeExpGenCond(compiler,registerNb, false, elseLabel);
        //codeExp(compiler,registerNb,false,elseLabel );
        // Condition vrai, (store 1)
        compiler.addInstruction(new LOAD(new ImmediateInteger(true),GPRegister.getR(registerNb)));
        compiler.addInstruction(new BRA(endLabel));
        
        // Condition faux (store 0)
        compiler.addLabel(elseLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(false),GPRegister.getR(registerNb)));
        // Label end
        compiler.addLabel(endLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(")");
        s.print(" " + getOperatorName() + " ");

        s.print("(");
        getRightOperand().decompile(s);
        s.print(")");
        s.print(")");
    }

    
}