package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.UnknowVar;
import fr.ensimag.deca.tree.AbstractExpr;

public class EnvironmentVarValue {
    private Map<Symbol, AbstractExpr> varCurrentValueMap = new HashMap<Symbol, AbstractExpr>();
    private Map<Symbol, Boolean> varUsedMap = new HashMap<Symbol, Boolean>();
    private boolean unknowIfVarUsed = false;

    public EnvironmentVarValue() {}

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public AbstractExpr getCurrentValue(Symbol key) {
        return varCurrentValueMap.get(key);
    }
    public boolean getIsUsed(Symbol key) {
        if (unknowIfVarUsed){
            return true;
        } else {
            return varUsedMap.containsKey(key);
        }
    }


    public void initValue(Symbol name, AbstractExpr def){
        varCurrentValueMap.put(name, def);
    }
    public void invalidateEnv(){ 
        for (Symbol key : varCurrentValueMap.keySet()) {
            varCurrentValueMap.put(key,new UnknowVar());
        }
        // On ne peut pas savoir si une variable a été utilisé dedans, ducoup on bloque !
        unknowIfVarUsed = true;
    }


    public void declareValue(Symbol name, AbstractExpr def)  throws ContextualError { // Vérifie qu'on assign pas une valeur Undefined !
        if (def instanceof UndefinedVar){
            throw new ContextualError("Identifiant non déclaré utilisé, impossible d'optimiser.", def.getLocation());
        }
        varCurrentValueMap.put(name, def);
    }
    public void declareUsed(Symbol name)  throws ContextualError { // Vérifie qu'on assign pas une valeur Undefined !
        varUsedMap.put(name, true);
    }

    public boolean containsCurrentValueKey(Symbol name){
        return varCurrentValueMap.containsKey(name);
    }
    public boolean containsUsedKey(Symbol name){
        return varUsedMap.containsKey(name);
    }



}
