package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;


/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 21/04/2023
 */
public class DeclClass extends AbstractDeclClass {

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass, ListDeclField fields, ListDeclMethod  methods) {
        Validate.notNull(className);
        Validate.notNull(superClass);
        Validate.notNull(fields);
        Validate.notNull(methods);
        this.className = className;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        className.decompile(s);
        if (!(superClass.getName().getName() == "Object") ) {

            s.print(" extends ");
            superClass.decompile(s);
            
        }
        
        s.print(" {");
        s.println();
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.print("}");
    }

    // Passe 4, optimisation
    protected void optimizeDeclClass(DecacCompiler compiler) throws ContextualError {
        // On ne partage pas l'environment entre les fields et methods, ça serait inutile, la gestion des fields néccesite un gros travaille d'analyse et on manque cruellement de temps
        fields.optimizeListDeclField(compiler);
        methods.optimizeListDeclMethod(compiler);
    }

    // Passe 1, (On passe aussi les fields et methods avant pour les déclarer), Passe 2 incluse à la fin
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // Vérifie que le super existe
        TypeDefinition type = compiler.environmentType.defOfType(superClass.getName());
        if (type == null || ! (type instanceof ClassDefinition) || !type.isClass()){
            throw new ContextualError("SuperClass Type not found !", this.getLocation() );
        }

        ClassDefinition parentType = (ClassDefinition)type;
        // Vérifie que la class n'existe pas déjà
        if (compiler.environmentType.defOfType(className.getName()) != null) {
            throw new ContextualError("Class already defined !", this.getLocation() );
        }

        // Ajoute la classe aux types !
        Identifier classIdent = (Identifier) this.className;
        Symbol nomClass = classIdent.getName();

        ClassType laClasse = new ClassType(nomClass,this.getLocation(), parentType);
        ClassDefinition def = laClasse.getDefinition();

        
        def.setInitLabel(new Label("init."+ className.getName().getName(),true));
       
        // Set le type et les définitions des identifiers
        className.setDefinition(def);
        className.setType(laClasse);
        
        superClass.setDefinition(parentType);
        superClass.setType( parentType.getType() );

        compiler.environmentType.addClassDefinition(this.className.getName(),def); // Ajoute la définition avant de verify les membres, pour qu'ils puisse faire référence à la classe

        verifyClassMembers(compiler); // Passe 1 et 2
        
    }
    // Passe 2
    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        // On initialise les membres dans la passe  (methodes et Field)
        
        // On copy la table des méthodes du parent
        // On vérifie les signatures des méthodes sans vérifier le body, à faire avant DecLField pour qu'ils fassent références aux méthodes
        for (AbstractDeclMethod mt: this.methods.getList()) {
            mt.verifyDeclMethod(compiler, this.className.getClassDefinition());
        }

        // On déclare d'abord les fields sans les initialisers !
        for (AbstractDeclField fl: this.fields.getList()) {
            fl.verifyDeclField(compiler, this.className.getClassDefinition());
        }
        // On initialise ensuite les fields !
        for (AbstractDeclField fl: this.fields.getList()) {
            fl.verifyInitField(compiler, this.className.getClassDefinition());
        }

    }

    // Passe 3
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        // Règle 3.5 (selon la syntaxContextuel)
        // Vérifie que la classe existe dans l'environment
        TypeDefinition classeType = compiler.environmentType.defOfType(className.getName());
        if (classeType == null || !classeType.isClass()){
            throw new ContextualError("Classe " + className.getName().getName() + " Type not found !", this.getLocation() );
        }

        for(AbstractDeclMethod mt : methods.getList()){
            mt.verifyMethodBody(compiler, this.className.getClassDefinition(), mt.type.getType());
        }
    }


    @Override
    protected
    void iterChildren(TreeFunction f) {
        className.iter(f);
        superClass.iter(f);
        fields.iter(f);
        methods.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }




    protected void codeGenVTable(DecacCompiler compiler){ // OK

        // Charge la base
        RegisterOffset regBase = compiler.getGestionnaireMemoire().getNextRegister();
        DAddr addrParent = superClass.getClassDefinition().getAddrVtable();

        compiler.addInstruction(new LEA(addrParent,GPRegister.R0)," Génération VTable "+className.getName().getName());
        compiler.addInstruction(new STORE(GPRegister.R0, regBase));

        className.getClassDefinition().setAddrVtable(regBase);
        // Charge les méthodes ensuite !
        for (AbstractDeclMethod method : className.getClassDefinition().getVTable()) {

            MethodDefinition def = method.getName().getMethodDefinition();

            // Prend la prochaine position dans GB
            RegisterOffset regMethod = compiler.getGestionnaireMemoire().getNextRegister();

            compiler.addInstruction(new LOAD( new LabelOperand(def.getLabel()),GPRegister.R0)); // Method equals
            compiler.addInstruction(new STORE(GPRegister.R0,regMethod));
        }
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler) { // En cours

        Label initLbl = className.getClassDefinition().getInitLabel();
        Label parentInitLbl = superClass.getClassDefinition().getInitLabel(); 
        
        compiler.addLabel(initLbl);
        compiler.getGestionnaireMemoire().init(compiler, false); // Reset la pile

        // Précharge les nouveaux champs, car ils peuvent implicitement être modifier par des méthodes redéfinie (page 204 doc! fonctionnalité caché mdr)
        for (AbstractDeclField unField : fields.getList()){
            unField.codeGenDeclFieldDefault(compiler);
        }

        if (parentInitLbl != null) { // Si a un Label init, on l'utilise ! 
            // On a un extends, donc on doit jump vers sont init avant
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),Register.R0));
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new BSR(parentInitLbl));
            compiler.addInstruction(new SUBSP(1));
        } 

        // Ensuite on charge les declField
        for (AbstractDeclField unField : fields.getList()){ // Seulement si elles ont un initialiseur
            unField.codeGenDeclField(compiler);
        }
        
        compiler.addInstruction(new RTS());

        compiler.getGestionnaireMemoire().buildStack(compiler); // Construit TSTO et ADDSP
    }


}
