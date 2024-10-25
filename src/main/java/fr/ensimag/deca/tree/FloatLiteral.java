package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.DVal;
/**
 * Single precision, floating-point literal
 *
 * @author gl07
 * @date 21/04/2023
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
                this.setType(compiler.environmentType.FLOAT);
                return compiler.environmentType.FLOAT;      
    }

    @Override
    protected void codeGenPrintHex(DecacCompiler compiler) {
        codeExp(compiler,1);
        compiler.addInstruction(new WFLOATX());
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeExp(compiler,1);
        compiler.addInstruction(new WFLOAT());
    }

    @Override
    protected DVal dval(){
        return new ImmediateFloat(value);
    }

    @Override
    protected void codeExp( DecacCompiler compiler, int registreNb){
        compiler.addInstruction(new LOAD(new ImmediateFloat(value),Register.getR(registreNb)));
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeExp(compiler,2);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Float.toString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
