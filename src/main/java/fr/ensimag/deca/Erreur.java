package fr.ensimag.deca;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.ERROR;

public class Erreur {

    private Label label;
    private String nom;
    private String msgErreur;
    private boolean isUsed;

    public Erreur(String nom, String msgErreur){
        this.label = new Label(nom,false);
        this.nom = nom;
        this.msgErreur = msgErreur;
        this.isUsed = false;
    }
    public Label getLabel(){
        setUsed();
        return label;
    }

    public String getNom(){
        return nom;
    }

    public String getMsgErreur(){
        return msgErreur;
    }

    public void setUsed(){
        this.isUsed = true;
    }
    public boolean isUsed(){
        return this.isUsed;
    }

    public void codeGenInst(DecacCompiler compiler){
        compiler.addLabel(getLabel());
        compiler.addInstruction(new WSTR("Error: "+msgErreur));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

}
