package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl07
 * @date 21/04/2023
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    //lastFreeReg is always free
    public void codeGenOp(DecacCompiler compiler, int lastFreeReg){


        int retReg = lastFreeReg;
        int auxReg = lastFreeReg + 1;
        if (lastFreeReg + 1 >= 16) {
            auxReg = (auxReg + 2) % 16;
            compiler.addInstruction(new PUSH(Register.getR(auxReg)), getOperatorName());
        }
        getLeftOperand().codeGenOp(compiler, retReg);

        getRightOperand().codeGenOp(compiler, auxReg);

        compiler.addInstruction(new SUB(auxReg, retReg), getOperatorName());

        if (lastFreeReg + 1 >= 16) {
            compiler.addInstruction(new POP(Register.getR(auxReg)), getOperatorName());
        }
    }
    
}
