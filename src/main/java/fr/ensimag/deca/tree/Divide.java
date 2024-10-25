package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.context.EnvironmentVarValue;
/**
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected float preCalculate(float val1,float val2){ return val1 / val2; }
    @Override
    protected int preCalculate(int val1,int val2){ return val1 / val2;}
    

    @Override
    protected String getOperatorName() {
        return "/";
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB){
        compiler.addInstruction(new LOAD(dval, Register.getR(1))); 
            
        if (getType().isInt()) {
           
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new CMP(0, Register.getR(1) )); 
                compiler.addInstruction(new BEQ(compiler.getGestionnaireErreur().getErreur("division_by_zero_error").getLabel())); 
            } 
            compiler.addInstruction(new QUO(dval,RegB));
        } else {

            
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new CMP(0.0f, Register.getR(1) ));
                compiler.addInstruction(new BEQ(compiler.getGestionnaireErreur().getErreur("division_by_zero_error").getLabel()));   
            } 
            compiler.addInstruction(new DIV(dval,RegB));
        }
    }


}
