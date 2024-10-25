package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * Definition of a method parameter.
 *
 * @author gl07
 * @date 21/04/2023
 */
public class ParamDefinition extends ExpDefinition {

    public int getIndex() {
        return index;
    }

    private int index;

    public ParamDefinition(Type type, Location location, int index) {
        super(type, location);
        this.index = index;
        setOperand(new RegisterOffset(index, Register.LB));
    }

    @Override
    public String getNature() {
        return "parameter";
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isParam() {
        return true;
    }

}
