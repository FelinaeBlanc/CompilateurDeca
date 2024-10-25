package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.context.EnvironmentVarValue;

/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryBoolExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr1 = this.getLeftOperand().optimizeExp(compiler, envVar);
        AbstractExpr expr2 = this.getRightOperand().optimizeExp(compiler, envVar);
        
        int numberLiteral = 0;
        float valA = 0;
        float valB = 0;

        if (expr1 instanceof IntLiteral){
            valA = (float)((IntLiteral)expr1).getValue();
            numberLiteral++;
        }else if (expr1 instanceof FloatLiteral){
            numberLiteral++;
            valA = ((FloatLiteral)expr1).getValue();
        }else if (expr1 instanceof BooleanLiteral){
            numberLiteral++;
            valA = (((BooleanLiteral)expr1).getValue()) ? 1 : 0;;
        }

        
        if (expr2 instanceof IntLiteral){
            valB = (float)((IntLiteral)expr2).getValue();
            numberLiteral++;
        }else if (expr2 instanceof FloatLiteral){
            numberLiteral++;
            valB = ((FloatLiteral)expr2).getValue();
        }else if (expr2 instanceof BooleanLiteral){
            numberLiteral++;
            valB = (((BooleanLiteral)expr2).getValue()) ? 1 : 0;;
        }

        if (numberLiteral == 2){
            return createBooleanLiteral(preCalculateBool(valA, valB),compiler);
        }

        setLeftOperand(expr1);
        setRightOperand(expr2);
        
        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type test1;
        Type test2;
        try {
            test1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            test2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        } catch (ContextualError e) {
            throw e;
        }
        if (test1.isInt() && test2.isFloat()){
            ConvFloat floattest= new ConvFloat(this.getLeftOperand()); 
            this.setLeftOperand(floattest);
            floattest.verifyExpr(compiler, localEnv, currentClass);
        }
        else if (test2.isInt() && test1.isFloat()){
            ConvFloat floattest= new ConvFloat(this.getRightOperand()); 
            this.setRightOperand(floattest);
            floattest.verifyExpr(compiler, localEnv, currentClass);
        }
        else if ((!test1.isInt() && !test1.isFloat())||(!test2.isInt() && !test2.isFloat())) {
            throw new ContextualError("Les deux expressions comparées ne sont pas des entiers ou des flotants",this.getLocation());
        } else if(!test1.sameType(test2)) {
            throw new ContextualError("Les deux expressions comparées n'ont pas le meme types",this.getLocation());
        }

        this.setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;

    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExp(compiler,2,value,e );
    }



    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registreNb, boolean value, Label e) {
        codeExp(compiler,registreNb,value,e );
    }
    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb, boolean value, Label e){
        // On appel codeGenInst avec les deux registre pour faire l'instruction (dans ce registre)
        DVal dvalE2 = getRightOperand().dval();

        //System.out.println("DVAL => "+dvalE2+" "+getRightOperand());
        if (dvalE2 != null){
            getLeftOperand().codeExp( compiler, registreNb);
            codeGenInst(compiler,dvalE2,Register.getR(registreNb),value,e);

        }else {
            
            if (registreNb < (compiler.getCompilerOptions().getRMax()-1)){ // n < Max
                getLeftOperand().codeExp( compiler, registreNb);
                getRightOperand().codeExp( compiler, registreNb+1);
                codeGenInst(compiler,Register.getR(registreNb+1),Register.getR(registreNb),value,e);

            } else { // n >= Max
                getLeftOperand().codeExp( compiler, registreNb);
    
                compiler.addInstruction(new PUSH(Register.getR(registreNb)));
                getRightOperand().codeExp( compiler, registreNb);
    
                compiler.addInstruction(new LOAD(Register.getR(registreNb),Register.getR(1)));
                compiler.addInstruction(new POP(Register.getR(registreNb)));
                codeGenInst(compiler,Register.getR(1),Register.getR(registreNb),value,e);

            }
        }
    }
}
