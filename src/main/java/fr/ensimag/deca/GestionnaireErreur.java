package fr.ensimag.deca;

import java.util.HashMap;
import java.util.Map;

public class GestionnaireErreur {

    private Map<String, Erreur> map = new HashMap<String, Erreur>();

    public GestionnaireErreur(){
        /* Init les Erreurs ICI */
        
        this.addErreur("io_error","Input/Output error");
        this.addErreur("stack_overflow_error","Stack Overflow");
        this.addErreur("overflow_error","Overflow during arithmetic operation");
        this.addErreur("division_by_zero_error","You can't divide a number by 0");
        
        this.addErreur("dereferencement_null","Null pointer exception");
        this.addErreur("no_return", "Method didn't return");

        this.addErreur("tas_plein", "Le tas est plein");
        this.addErreur("pile_pleine", "La pile est pleine");
        this.addErreur("cast_illegal", "Cast de classes non hereditaires");

    }

    public Map<String, Erreur> getErreurs(){
        return map;
    }
    public Erreur getErreur(String nom){
        return map.get(nom);
    }
    
    public void addErreur(String nom, String msgErreur) {
        if(!map.containsKey(nom)){
            map.put(nom, new Erreur(nom,msgErreur));
        }
    }

}