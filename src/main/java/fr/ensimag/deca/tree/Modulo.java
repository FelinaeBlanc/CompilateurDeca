package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.context.EnvironmentVarValue;
/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    protected float preCalculate(float val1,float val2){ return val1 % val2; }
    @Override
    protected int preCalculate(int val1,int val2){ return val1 % val2;}

    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type test1 = this.getLeftOperand().verifyExpr(compiler,localEnv, currentClass); 
        Type test2 = this.getRightOperand().verifyExpr(compiler,localEnv, currentClass);
        if (!test1.isInt() || !test2.isInt()) {
            throw new ContextualError("You can't use modulo on another type than integers",this.getLocation()); 
        }
        this.getLeftOperand().setType(test1); 
        this.getRightOperand().setType(test2);
        
        setType(compiler.environmentType.INT);
        return compiler.environmentType.INT; 
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }



    @Override
    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB){
        compiler.addInstruction(new LOAD(dval, Register.getR(1))); 
       
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new CMP(0, Register.getR(1) )); 
            compiler.addInstruction(new BEQ(compiler.getGestionnaireErreur().getErreur("division_by_zero_error").getLabel())); 
        } 
        compiler.addInstruction(new REM(dval,RegB));
    }
}
