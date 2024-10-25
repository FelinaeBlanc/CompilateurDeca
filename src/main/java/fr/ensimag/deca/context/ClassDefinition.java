package fr.ensimag.deca.context;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.DeclField;
import fr.ensimag.deca.tree.DeclMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl07
 * @date 21/04/2023
 */
public class ClassDefinition extends TypeDefinition {

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    List<AbstractDeclMethod> vTable;
    List<DeclField> fieldTable;
    Label initLabel;

    private DAddr addrVtable = new RegisterOffset(1, GPRegister.GB); 

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        this.superClass = superClass;
        
        if (superClass != null) {
            // Prend la Vtable et la Field table du parent
            vTable = new ArrayList<>(superClass.vTable); 
            fieldTable = new ArrayList<>(superClass.fieldTable);

            members = new EnvironmentExp(superClass.getMembers());
        } else { // Classe Object

            members = new EnvironmentExp(null);
            vTable = new ArrayList<>();
            fieldTable = new ArrayList<>();
        }
    }

    public int getNumberOfMethods() {
        return vTable.size();
    }
    public int getNumberOfFields() {
        return fieldTable.size();
    }
    public Label getInitLabel(){
        return initLabel;
    }
    public EnvironmentExp getMembers() {
        return members;
    }

    public void setInitLabel(Label initLabel){
        this.initLabel = initLabel;
    }
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    public DAddr getAddrVtable() {
        return addrVtable;
    }
    
    public void setAddrVtable(DAddr addrVtable) {
        this.addrVtable = addrVtable;
    }

    public List<AbstractDeclMethod> getVTable() {
        return vTable;
    }
    public List<DeclField> getFieldTable(){
        return fieldTable;
    }


}
