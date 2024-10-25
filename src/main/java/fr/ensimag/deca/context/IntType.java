package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;


/**
 *
 * @author Ensimag
 * @date 21/04/2023
 */
public class IntType extends Type {

    public IntType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isInt();
    }


}
