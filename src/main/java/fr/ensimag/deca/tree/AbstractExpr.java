package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl07
 * @date 21/04/2023
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    // Methodes pour l'optimisation
    protected boolean preCalculateBool(float val1, float val2){ throw new UnsupportedOperationException("preCalculateBool 1 not yet implemented");}
    protected boolean preCalculateBool(boolean val1, boolean val2){ throw new UnsupportedOperationException("preCalculateBool 2 not yet implemented");}
    protected float preCalculate(float val1,float val2){ throw new UnsupportedOperationException("preCalculate 1 not yet implemented");}
    protected int preCalculate(int val1,int val2){ throw new UnsupportedOperationException("preCalculate 2 not yet implemented");}

    

    protected BooleanLiteral createBooleanLiteral(boolean value, DecacCompiler compiler) {
        BooleanLiteral literal = new BooleanLiteral(value);
        literal.setType(compiler.environmentType.BOOLEAN);
        return literal;
    }

    protected IntLiteral createIntLiteral(int value, DecacCompiler compiler) {
        IntLiteral literal = new IntLiteral(value);
        literal.setType(compiler.environmentType.INT);
        return literal;
    }
    
    protected FloatLiteral createFloatLiteral(float value, DecacCompiler compiler) {
        FloatLiteral literal = new FloatLiteral(value);
        literal.setType(compiler.environmentType.FLOAT);
        return literal;
    }
    
    // Passe 1 optimisation
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        return this;   
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,EnvironmentExp localEnv, ClassDefinition currentClass, Type expectedType) throws ContextualError {

        Type t = this.verifyExpr(compiler, localEnv, currentClass);
        if ( !compiler.environmentType.assign_compatible(expectedType,t) ){
            throw new ContextualError("Type "+ t.getName().getName() + " et " + expectedType.getName().getName()+ " non compatible",this.getLocation());
        }
        
        if (expectedType.isFloat() && t.isInt()) {
            ConvFloat conv = new ConvFloat(this);
            conv.verifyExpr(compiler,localEnv,currentClass);
            return conv;
        } else if (expectedType.isInt() && t.isFloat()){
            ConvInt conv = new ConvInt(this);
            conv.verifyExpr(compiler, localEnv, currentClass);
            return conv;
        }
        

        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
                Type t = verifyExpr(compiler, localEnv, currentClass);
                this.setType(t);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            Type t = this.verifyExpr(compiler, localEnv, currentClass);
            
            setType(t);
            if (!t.isBoolean()){
                throw new ContextualError("condition is not boolean", getLocation());
            }

    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("codeGenPrint not yet implemented");
    }

    @Override
    protected void codeGenPrintHex(DecacCompiler compiler) {
        throw new UnsupportedOperationException("codeGenPrint not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //System.err.println(this);
        throw new UnsupportedOperationException("codeGenInst 1 not yet implemented");
    }
    
    protected void codeGenInst(DecacCompiler compiler, GPRegister R){
        throw new UnsupportedOperationException("codeGenInst 2 not yet implemented");
    }

    protected void codeGenInst(DecacCompiler compiler, int lastFreeReg){
        throw new UnsupportedOperationException("codeGenInst 3 not yet implemented");
    }

    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB){
        throw new UnsupportedOperationException("codeGenInst 5 not yet implemented");
    }

    // codeGenInst des opérateurs booleen et comparateurs
    protected void codeGenInst(DecacCompiler compiler, DVal dval,GPRegister RegB,boolean value, Label e){
        throw new UnsupportedOperationException("codeGenInst 6 not yet implemented");
    }

    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        throw new UnsupportedOperationException("codeGenCond not yet implemented");
    }

    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        System.out.println("Nope "+this);
        throw new UnsupportedOperationException("codeGenCond not yet implemented");
    }
    

    protected DVal dval(){
        throw new UnsupportedOperationException("dval not yet implemented");
    }
    protected void codeExp( DecacCompiler compiler, int registreNb){
        throw new UnsupportedOperationException("codeExp not yet implemented");
    }
    // codeExp des comparaisons et opérateurs booleen
    protected void codeExp( DecacCompiler compiler, int registreNb, boolean value, Label e){
        System.out.println(this);
        throw new UnsupportedOperationException("codeExp2 not yet implemented");
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
