package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.List;
/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class CallMethod extends AbstractExpr{

    protected AbstractExpr classCalled;
    protected AbstractIdentifier name;
    protected ListExpr argsList;

    public CallMethod(AbstractExpr classCalled, AbstractIdentifier name, ListExpr argsList) {
        Validate.notNull(classCalled);
        Validate.notNull(name);
        Validate.notNull(argsList);
        this.classCalled = classCalled;
        this.name = name;
        this.argsList = argsList;
    }
    protected void optimizeInst(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError { // optimises les Arguments !
        List<AbstractExpr> laListe = argsList.getList();

        for (int i = 0; i < laListe.size(); i++) {
            argsList.set(i, laListe.get(i).optimizeExp(compiler, envVar));
        }
    }

    @Override
    public AbstractExpr optimizeExp(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        optimizeInst(compiler,envVar);
        return this;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classCalled.decompile(s);
        s.print(".");
        name.decompile(s);
        s.print("(");
        argsList.decompile(s);
        s.print(")");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (currentClass == null && this.classCalled instanceof This){
            throw new ContextualError("Attempted to call a method of a non-class element", this.getLocation());
        }

        Type accessedT = this.classCalled.verifyExpr(compiler, localEnv, currentClass);
        if(!accessedT.isClass()) { throw new ContextualError("Attempted to call a method of a non-class element", this.getLocation()); }
        ClassType leftClass = accessedT.asClassType("Identifier is not a class", this.getLocation());
        ExpDefinition def = leftClass.getDefinition().getMembers().get(name.getName());

        if(def == null || !def.isMethod()){
            throw new ContextualError("Method not found", this.getLocation());
        }
        Type nameT = name.verifyExpr(compiler, leftClass.getDefinition().getMembers(), currentClass);

        Signature sig = name.getMethodDefinition().getSignature();
        List<AbstractExpr> laListParam = argsList.getList();
        if( sig.size() != laListParam.size()){
            throw new ContextualError(this.name.getName().getName() + ": " + laListParam.size()  + " arguments provided, " + sig.size() + "expected.", this.getLocation());
        }
        // Vérifie les types des params si ils sont compatibles !
        // VerifyRValue
        for (int i = 0; i < laListParam.size(); i++) {
            AbstractExpr param = laListParam.get(i);
            laListParam.set(i,param.verifyRValue(compiler, localEnv, currentClass,sig.paramNumber(i)));
        }

        setType(nameT);

        return nameT;
    }


    @Override
    protected
    void iterChildren(TreeFunction f) {
        name.iter(f);
        argsList.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classCalled.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        argsList.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, boolean value, Label e) {
        codeExpGenCond(compiler, 2, value, e);
    }

    @Override
    protected void codeExpGenCond(DecacCompiler compiler, int registerNb, boolean value, Label e){
        codeExp(compiler,registerNb);
        compiler.addInstruction(new LOAD(Register.getR(registerNb),Register.R0));
        
        //CMP
        compiler.addInstruction(new CMP(new ImmediateInteger(!value), Register.R0));
        compiler.addInstruction(new BNE(e));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeExp(compiler,2);
    }

    protected void codeExp( DecacCompiler compiler, int registreNb){

        //Calcule ADDSP
        int sp_push = argsList.getList().size() + 1;
        RegisterOffset registreMethode = new RegisterOffset(0,Register.SP);

        compiler.addInstruction(new ADDSP(sp_push));

        classCalled.codeExp(compiler, registreNb); // Met la ref de la classe dans le registre 2
        compiler.addInstruction(new STORE(Register.getR(registreNb), registreMethode)); // Empile la ref de la classe dans 0 (SP)


        // Empile les registres
        int pos = -1;
        for (AbstractExpr param : argsList.getList()) {
            param.codeExp(compiler, registreNb); // Calcule le param et ensuite on le stock via sp
            compiler.addInstruction(new STORE(Register.getR(registreNb), new RegisterOffset(pos,Register.SP)));
            pos--;
        }

        // Appel méthode
        compiler.addInstruction(new LOAD(registreMethode,Register.getR(registreNb)));
        
        // check null
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(),Register.getR(registreNb)));
            compiler.addInstruction(new BEQ(compiler.getGestionnaireErreur().getErreur("dereferencement_null").getLabel()));
        }
        

        // appel la méthode ! 
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.getR(registreNb)),Register.getR(registreNb)));

        compiler.addInstruction(new BSR(new RegisterOffset(this.name.getMethodDefinition().getIndex()+1,Register.getR(registreNb))));
        
        // Désempile
        compiler.addInstruction(new SUBSP(sp_push));

        // Bouge le résultat dans le registre demandé
        compiler.addInstruction(new LOAD(Register.R0,Register.getR(registreNb)));

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeExp(compiler,2);

        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else {
            compiler.addInstruction(new WFLOAT());
        }
        
    }

    @Override
    protected void codeGenPrintHex(DecacCompiler compiler) {
        codeExp(compiler,2);

        compiler.addInstruction(new LOAD(Register.getR(2),Register.getR(1)));

        Type leType = getType();
        
        if(leType.isFloat()){
            compiler.addInstruction(new WFLOATX());
        }
        
    }

    protected DVal dval(){
        return null;
    }

}
