package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl07
 * @date 21/04/2023
 */
public class DeclField extends AbstractDeclField {



    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier name, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.name = name;
        this.initialization = initialization;
    }

    @Override
    public void decompile(IndentPrintStream s) {

        if (visibility == Visibility.PROTECTED) {
            s.print("protected");
            s.print(" ");
        }

        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        if (initialization instanceof Initialization){
            s.print(" = ");
            initialization.decompile(s);
        }
        s.print(";");
    } 

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    String printNodeLine(PrintStream s, String prefix, boolean last,
                         boolean inlist, String nodeName) {
        s.print(prefix);
        if (inlist) {
            s.print("[]>");
        } else if (last) {
            s.print("`>");
        } else {
            s.print("+>");
        }
        if (getLocation() != null) {
            s.print(" " + getLocation().toString());
        }
        s.print(" ");
        s.print("[visibility="+visibility.toString()+"]");
        s.print(" ");
        s.print(nodeName);
        s.println();
        String newPrefix;
        if (last) {
            if (inlist) {
                newPrefix = prefix + "    ";
            } else {
                newPrefix = prefix + "   ";
            }
        } else {
            if (inlist) {
                newPrefix = prefix + "||  ";
            } else {
                newPrefix = prefix + "|  ";
            }
        }
        prettyPrintType(s, newPrefix);
        return newPrefix;
    }
    @Override
    protected void optimizeDeclField(DecacCompiler compiler, EnvironmentVarValue envVar) throws ContextualError {
        // Si initialisation seulement =>
        if (initialization instanceof Initialization){
            initialization.optimizeInitialization(compiler,envVar);
            
            // Set dans l'env des valeurs des vars la valeur
            envVar.declareValue(name.getName(), ((Initialization) initialization).getExpression());
        }
    }

    // Passe 1 (vérifier expression initialisation)
    protected void verifyInitField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        // On verify d'abord l'initialisation, avant de déclarer le field, car il ne peut pas se faire référence à lui même (sauf sur le parent, via cast).
        initialization.verifyInitialization(compiler, name.getDefinition().getType(), currentClass.getMembers(), currentClass);
    }
        
    // Passe 2 (vérifier expression initialisation)
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {

        Type fieldType = type.verifyType(compiler);

        // Les deux conditions de Declfield
        if (fieldType.isVoid()) { // Type != void
            throw new ContextualError("DeclField Type Invalide !", this.getLocation() );
        }

        ClassDefinition superClass = currentClass.getSuperClass(); // Si parent existe et que name y est définie => il doit aussi être  un Field !
        if (superClass != null){
            EnvironmentExp superExp = superClass.getMembers();
            ExpDefinition parentDef = superExp.get(name.getName());
            if (parentDef != null && !parentDef.isField()){
                throw new ContextualError("DeclField name exist in parent but is not a Field !", this.getLocation() );
            }
        }

        int index = currentClass.getNumberOfFields();
        FieldDefinition def = new FieldDefinition(fieldType, this.getLocation(), visibility, currentClass, index);
        currentClass.getFieldTable().add(this); // Ajoute le field à la list
        
        name.setDefinition(def); // Met la def de l'indentifier

        // Et enfin déclare le field dans l'env !
        try {
            currentClass.getMembers().declare(name.getName(), name.getFieldDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError("DeclField name already declared !", this.getLocation() );
        }

        return;
    }

    @Override
    protected void codeGenDeclFieldDefault(DecacCompiler compiler) {
        compiler.addComment("Pre Initialise "+name.getName().getName());
        if (type.getType().isInt()) {
            compiler.addInstruction(new LOAD(0, GPRegister.R0));

        } else if (type.getType().isFloat()) {
            compiler.addInstruction(new LOAD(0.0f, GPRegister.R0));
            
        } else if (type.getType().isBoolean()) {
           compiler.addInstruction(new LOAD(0, GPRegister.R0));
    
        } else if (type.getType().isClass()){
            compiler.addInstruction(new LOAD(new NullOperand(), GPRegister.R0));
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.R1));
        int place = name.getFieldDefinition().getIndex() +1 ;
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(place, GPRegister.R1)));
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        if (this.initialization instanceof Initialization ){
            AbstractExpr expr = ((Initialization) initialization).getExpression();
            expr.codeExp(compiler,2);
            compiler.addInstruction(new LOAD(Register.R2, Register.R0));
        
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), GPRegister.R1));
            int place = name.getFieldDefinition().getIndex() +1 ;
            compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(place, GPRegister.R1)));
        }
    }

}
