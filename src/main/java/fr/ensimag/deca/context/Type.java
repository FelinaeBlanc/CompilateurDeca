package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Deca Type (internal representation of the compiler)
 *
 * @author gl07
 * @date 21/04/2023
 */

public abstract class Type {


    /**
     * True if this and otherType represent the same type (in the case of
     * classes, this means they represent the same class).
     */
    public abstract boolean sameType(Type otherType);

    private final Symbol name;

    public Type(Symbol name) {
        this.name = name;
    }

    public Symbol getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName().toString();
    }

    public boolean isClass() {
        return name.getName().equals("class");
    }

    public boolean isInt() {
        return name.getName().equals("int");
    }

    public boolean isFloat() {
        return name.getName().equals("float");
    }

    public boolean isBoolean() {
        return name.getName().equals("boolean");
    }

    public boolean isVoid() {
        return name.getName().equals("void");
    }

    public boolean isString() {
        return name.getName().equals("string");
    }

    public boolean isNull() {
        return name.getName().equals("null");
    }

    public boolean isClassOrNull() {
        return isClass() || isNull();
    }

    /**
     * Returns the same object, as type ClassType, if possible. Throws
     * ContextualError(errorMessage, l) otherwise.
     *
     * Can be seen as a cast, but throws an explicit contextual error when the
     * cast fails.
     */
    public ClassType asClassType(String errorMessage, Location l)
            throws ContextualError {
                
        throw new ContextualError(errorMessage, l);
    }

}
