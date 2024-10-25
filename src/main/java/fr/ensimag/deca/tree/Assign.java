package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;

import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
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
    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        if (getLeftOperand() instanceof Identifier){
            this.setRightOperand(getRightOperand().optimizeExp(compiler, envVar));
        }
        envVar.declareValue( ((Identifier) getLeftOperand()).getName(), getRightOperand());
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr actualVal = getRightOperand().optimizeExp(compiler, envVar);
        this.setRightOperand(actualVal);

        envVar.declareValue( ((Identifier) getLeftOperand()).getName(), actualVal);

        if (actualVal instanceof FloatLiteral || actualVal instanceof IntLiteral || actualVal instanceof BooleanLiteral){
            return actualVal;
        }

        return this;
    }

    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(t);

        this.setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, t));

        return t;
    }


    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){

        // La gauche est forcément un identifier, ou un dot !
        getRightOperand().codeExp(compiler, registreNb);

        if (super.getLeftOperand() instanceof Identifier){
            Identifier expL = (Identifier)super.getLeftOperand();
            if (expL.getExpDefinition().isField()){ // est field
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.R1)); // On charge le this
                compiler.addInstruction(new STORE(Register.getR(registreNb), new RegisterOffset(expL.getFieldDefinition().getIndex()+1, GPRegister.R1) ));

            }else{ // identifier normale
                compiler.addInstruction(new STORE(Register.getR(registreNb),getLeftOperand().getDAddr()));
            }
        }else if (super.getLeftOperand() instanceof Dot) {
            compiler.addInstruction(new PUSH(GPRegister.getR(registreNb))); // sauvegarde

            Dot expL = (Dot)super.getLeftOperand();
            expL.getLeftOperand().codeExp(compiler,registreNb);

            compiler.addInstruction(new POP(GPRegister.getR(1))); // remet sur R1
            compiler.addInstruction(new STORE(Register.getR(1), new RegisterOffset( ((Identifier)expL.getRightOperand()).getFieldDefinition().getIndex()+1, GPRegister.getR(registreNb)) ));
            
        } else {
            System.out.println("Souldn't be called");
        }

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //getRightOperand().codeGenInst(compiler);
        //compiler.addInstruction(new STORE(Register.R2,getLeftOperand().getDAddr()));
        codeExp(compiler,2);
    }

    // Si appel de codeGenCond, ça implique que l'Assign doit être sur un booleen !
    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond(compiler, 2, value, e);
    }
    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        codeExp(compiler,registerNb); // On met la valeur dans le registre 2

        //CMP
        compiler.addInstruction(new CMP(new ImmediateInteger(!value), Register.getR(registerNb)));
        compiler.addInstruction(new BNE(e));
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
