package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.context.EnvironmentVarValue;
/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr expr1 = this.getLeftOperand().optimizeExp(compiler, envVar);
        AbstractExpr expr2 = this.getRightOperand().optimizeExp(compiler, envVar);
        
        int numberLiteral = 0;
        boolean makeFloat = false;
        float valA = 0;
        float valB = 0;

        if (expr1 instanceof IntLiteral){
            valA = (float)((IntLiteral)expr1).getValue();
            numberLiteral++;
        }else if (expr1 instanceof FloatLiteral){
            makeFloat = true;
            numberLiteral++;
            valA = ((FloatLiteral)expr1).getValue();
        }
        
        if (expr2 instanceof IntLiteral){
            valB = (float)((IntLiteral)expr2).getValue();
            numberLiteral++;
        }else if (expr2 instanceof FloatLiteral){
            makeFloat = true;
            numberLiteral++;
            valB = ((FloatLiteral)expr2).getValue();
        }

        if (numberLiteral == 2){
            if (makeFloat){
                return createFloatLiteral(preCalculate(valA, valB),compiler);
            }else{
                return createIntLiteral(preCalculate((int)valA, (int)valB),compiler);
            }
        } else { // Pas de literal

        }

        setLeftOperand(expr1);
        setRightOperand(expr2);
        
        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type1  = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2  = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        Type res = null; 

        if (type1.isInt() && type2.isFloat()){
            ConvFloat floattest= new ConvFloat(this.getLeftOperand()); 
            this.setLeftOperand(floattest);
            res = floattest.verifyExpr(compiler, localEnv, currentClass);
        }
        else if (type1.isFloat() && type2.isInt()){
            ConvFloat floattest= new ConvFloat(this.getRightOperand()); 
            this.setRightOperand(floattest);
            res= floattest.verifyExpr(compiler, localEnv, currentClass);
        }
        else if (type1.isInt() && type2.isInt()){
            res =  compiler.environmentType.INT;
        }else if (type1.isFloat() && type2.isFloat()) {
            res =  compiler.environmentType.FLOAT;
        }else {
            throw new ContextualError("Error unsupported types", getLocation());
        }

        this.setType(res); 
        return res; 
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else {
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected void codeGenPrintHex(DecacCompiler compiler){
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2),GPRegister.getR(1)));

        Type leType = getType();
        
        if(leType.isFloat()){
            compiler.addInstruction(new WFLOATX());
        }
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

    private void checkFloatOverflow(DecacCompiler compiler, Type typeA, Type typeB){
        // Si opération sur float, on ajoute 
        if (typeA.isFloat() || typeB.isFloat()){
            compiler.addInstruction(new BOV(compiler.getGestionnaireErreur().getErreur("overflow_error").getLabel()));
        }
    }

    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        // On appel codeGenInst avec les deux registre pour faire l'instruction (dans ce registre)
        DVal dvalE2 = getRightOperand().dval();
        
        if (dvalE2 != null){
            getLeftOperand().codeExp(compiler, registreNb);
            codeGenInst(compiler,dvalE2,Register.getR(registreNb));

        }else {
            if (registreNb < (compiler.getCompilerOptions().getRMax()-1)){ // n < Max
                getLeftOperand().codeExp(compiler, registreNb);
                getRightOperand().codeExp(compiler, registreNb+1);
                codeGenInst(compiler,Register.getR(registreNb+1),Register.getR(registreNb));
            } else { // n >= Max
                getLeftOperand().codeExp(compiler, registreNb);
    
                compiler.addInstruction(new PUSH(Register.getR(registreNb)));
                getRightOperand().codeExp(compiler, registreNb);
    
                compiler.addInstruction(new LOAD(Register.getR(registreNb),Register.getR(1)));
                compiler.addInstruction(new POP(Register.getR(registreNb)));
                codeGenInst(compiler,Register.getR(1),Register.getR(registreNb));
            }
        }
        checkFloatOverflow(compiler, getLeftOperand().getType(),getRightOperand().getType());
    }
}
