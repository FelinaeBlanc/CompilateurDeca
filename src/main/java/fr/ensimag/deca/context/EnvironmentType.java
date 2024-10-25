package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.DeclParam;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.ListDeclParam;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.MethodBody;
import fr.ensimag.deca.tree.DeclParam;
import fr.ensimag.deca.tree.AbstractDeclParam;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ClassDefinition;


// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl07
 * @date 21/04/2023
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.
        
        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));

        // PredefType Object
        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, Location.BUILTIN, null);
        ClassDefinition objectDef = OBJECT.getDefinition();
   
        // Créer la méthode
        // Créer Objet.equals equals(Object o){ return this == o}; (seulement sa définition, il sera ajouté à la main en instruction lors de GenCode)
        
        // Création Méthode equals
        Symbol paramSymb = compiler.createSymbol("o");
        Symbol methodSymb = compiler.createSymbol("equals");

        Identifier paramType = new Identifier(objectSymb);
        Identifier paramName = new Identifier(paramSymb);

        Identifier methodType = new Identifier(booleanSymb);;
        Identifier methodName = new Identifier(methodSymb);

        paramType.setDefinition(objectDef);
        methodType.setDefinition(envTypes.get(booleanSymb));

        paramName.setDefinition(objectDef);
        
        objectDef.setInitLabel(new Label("init."+"Object",true));

        // Création définition Param et Method
        Signature sig = new Signature();
        sig.add(OBJECT);

        // Param
        ParamDefinition paramEquals = new ParamDefinition(OBJECT, Location.BUILTIN, -3);
        DeclParam objParam = new DeclParam(paramType,paramName);
        objParam.setLocation(Location.BUILTIN);
        //try { objectDef.getMembers().declare(paramSymb, paramEquals); } catch (DoubleDefException e) {}

        // La list des params
        ListDeclParam listParam = new ListDeclParam();
        listParam.add((AbstractDeclParam)objParam);

        // La méthode
        MethodDefinition methodEquals = new MethodDefinition(BOOLEAN, Location.BUILTIN, sig, 0); 
        methodEquals.setLabel(new Label("code.Object.equals", false));
        try { objectDef.getMembers().declare(methodSymb, methodEquals); } catch (DoubleDefException e) { /* Osef, il n'y aura jamais de DoubleDefException*/ }

        
        DeclMethod equalsDeclMethod = new DeclMethod(methodType,methodName,listParam,new MethodBody(new ListDeclVar(), new ListInst()));
        equalsDeclMethod.getName().setDefinition(methodEquals);

        objectDef.getVTable().add(equalsDeclMethod);
        envTypes.put(objectSymb,objectDef);
        
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    // Utilisé pour ajouter les types de classes
    public void addClassDefinition(Symbol className, ClassDefinition classDef){
        envTypes.put(className,classDef);
    }

    // t1 est une sous classe de t2
    public boolean isSubClassOf(ClassType t1, ClassType t2) {
        if (t1.sameType(t2)){ return true;}

        ClassDefinition parent = t1.getDefinition().getSuperClass();
        if ( parent == null){ return false;}

        return isSubClassOf(parent.getType(),t2);
    }

    // t1 sous type de t2
    public boolean subtype(Type t1, Type t2) {
        if (t1.sameType(t2)){ return true;}

        if (t1.isClassOrNull() && t2.isClass()){
            if (t1.isNull()){ return true; }

            return isSubClassOf( (ClassType) t1, (ClassType) t2);
        }
        return false;
    }

    
    // Les fonctions de compatibiltié de types
    public boolean assign_compatible(Type t1, Type t2){
        if (t1.isFloat() && t2.isInt()){ return true; }
        return subtype(t2,t1);
    }

    // T2 = (T2) T1;
    public boolean cast_compatible(Type t1, Type t2){
        if (t1.isVoid()){ return false;}
        return assign_compatible(t1, t2) || assign_compatible(t2, t1);
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final NullType    NULL;
    public final ClassType   OBJECT;
}
