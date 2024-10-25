package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.UndefinedVar;
import fr.ensimag.deca.context.UnknowVar;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentVarValue;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import fr.ensimag.ima.pseudocode.DVal;

import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label; 

/**
 * Deca Identifier
 *
 * @author gl07
 * @date 21/04/2023
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    DAddr getDAddr() {
        return getExpDefinition().getOperand();
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    
    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        AbstractExpr actualVal = envVar.getCurrentValue(getName());
        
        // actualVal possède la valeur actuel du champ ! on retourne soit  soit sa valeur si elle peut être donné directement, ou soit l'identifiant,
        if (actualVal != null){
            if (actualVal instanceof FloatLiteral || actualVal instanceof IntLiteral || actualVal instanceof BooleanLiteral){
                return actualVal;
            } else { // Sinon, on utilise sa référence, donc elle est utilisé, on le met dans la map
                envVar.declareUsed(name);
            }
        }

        return this;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError {
        Definition newDef = localEnv.get(name);

        if (newDef == null){ // On accède à un identifiant qui n'est pas définie dans l'environnement !
            throw new ContextualError("Identifiant non déclaré !", getLocation());
        }
            
        this.setDefinition(newDef);
        this.setType(newDef.getType());

        return newDef.getType();
        
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {

            Definition myDef = compiler.environmentType.defOfType(name);
            if (myDef != null) {
                this.setDefinition(myDef);
                this.setType(definition.getType());
                return definition.getType();
            }else{
                throw new ContextualError("Type indefini",getLocation());
            }
            
    }
    
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(getDAddr(),Register.R2), getName() + " -> " + Register.R2.toString());
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister R) {
        if (getDefinition().isField()){
            FieldDefinition def = (FieldDefinition) getDefinition();

            compiler.addInstruction( new LOAD( new RegisterOffset(-2, Register.LB),R)); // charge le this
            compiler.addInstruction(new LOAD( new RegisterOffset(def.getIndex() + 1, R),R));

        } else {
            compiler.addInstruction(new LOAD(getDAddr(),R), getName() + " -> " + R.toString());
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2),GPRegister.getR(1)));

        Type leType = definition.getType();
        if (leType.isInt()){
            compiler.addInstruction(new WINT());
        }else if(leType.isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        

        //compiler.addInstruction(new WINT(new ImmediateInteger(value)));
        //this.getExpDefinition().getOperand()
    }


    @Override
    protected void codeGenPrintHex(DecacCompiler compiler) {
        codeExp(compiler,2);
        compiler.addInstruction(new LOAD(GPRegister.getR(2),GPRegister.getR(1)));
        compiler.addInstruction(new WFLOATX());
    }


    protected DVal dval(){
        return (DVal) getDAddr();
    }

    protected void codeExp( DecacCompiler compiler, int registreNb){
        codeGenInst(compiler,GPRegister.getR(registreNb));
    }

    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        Type leType = definition.getType();

        if (leType.isBoolean()){
            codeExpGenCond(compiler, 2, value, e);
        } else {
            codeExp(compiler,2,value,e );
        }
        
    }

    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e) {
            //Load Addr value in R1
            //compiler.addInstruction(new LOAD(getDAddr(), Register.getR(registerNb)));
            codeGenInst(compiler,GPRegister.getR(registerNb) );
            //CMP Constante 1 avec valeur Indent
            compiler.addInstruction(new CMP(new ImmediateInteger(1), Register.getR(registerNb)));
            if (value){ // SAUTE SI x == x
                compiler.addInstruction(new BEQ(e));
            } else {  // SAUTE SI x > x
                compiler.addInstruction(new BNE(e));
            }
    }
    
}
