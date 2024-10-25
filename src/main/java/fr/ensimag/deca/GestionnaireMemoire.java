package fr.ensimag.deca;

import java.util.ArrayList;
import java.util.List;

import fr.ensimag.ima.pseudocode.CheckPoint;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.POP;

import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class GestionnaireMemoire {
    // Contexte Relatif (peut être reset lorsque l'on entre dans un nouveau bloc)
    private int nbVarPileUsed = 0; // Commence à 0

    // Empilements (PUSH ET POP) (ADDSP, SUBSP...)
    private int nbMaxEmpilement = 0;
    private int nbCurrentEmpilement = 0;

    private int maxRegisterUsed = 0; // Permet de savoir dans le contexte actuel on utilise des registre au dessus de R1 (R2 ...)

    // Contexte
    private boolean isMain = false;
    CheckPoint topCheckPoint;
    CheckPoint endStack;  // CheckPoint à la fin du ADDSP et TSTO

    List<CheckPoint> lesCheckPointsRTS;

    public GestionnaireMemoire(){}

    public void init(DecacCompiler compiler, boolean isMain){ // Reset tout
        this.isMain = isMain;

        this.nbVarPileUsed = 0; // Commence à 0

        this.nbMaxEmpilement = 0;
        this.nbCurrentEmpilement = 0;

        this.maxRegisterUsed = 0;

        topCheckPoint = compiler.makeCheckPoint();
        endStack = null;

        lesCheckPointsRTS = new ArrayList<>();
    }

    public RegisterOffset getNextRegister(){
        nbVarPileUsed++;

        if (this.isMain){ // On utilise GB
            return new RegisterOffset(nbVarPileUsed,GPRegister.GB);
        }else{ // On utilise LB
            return new RegisterOffset(nbVarPileUsed,GPRegister.LB);
        }
    }

    // Empilements (PUSH ET POP) (ADDSP, SUBSP...)
    public int getNbMaxEmpilement(){ return nbMaxEmpilement; }
    public void decrementEmpilement(int nb){ nbCurrentEmpilement -= nb; }
    public void incrementEmpilement(int nb){
        nbCurrentEmpilement += nb;
        this.nbMaxEmpilement = Math.max(this.nbMaxEmpilement, nbCurrentEmpilement);
    }

    public void updateMaxRegisterUsed(int registreNb){ // Si plus grand, on le prend
        this.maxRegisterUsed = Math.max(this.maxRegisterUsed,registreNb);
    }
    
    // AJOUTE LES ELEMENTS A L'ENVERS ! Car addInstruction en fonction d'un CheckPoint décale les instructions après le checkpoint pour y mettre la nouvelle
    public void buildStack(DecacCompiler compiler){
        if (!this.isMain){
            buildSaveRegister(compiler, topCheckPoint); // Avant de débuter, sauvegarde les registres !
        }
    
        // ADDSP
        if (nbVarPileUsed > 0){ //  Implique qu'on a utilisé des variables qui on été mit dans la piles
            compiler.addInstruction(new ADDSP(nbVarPileUsed) , topCheckPoint,false); // false, car on ne doit pas incrémenter la pile
        }
        
        // TSTO
        int tstoSize = nbMaxEmpilement + nbVarPileUsed;
        if (tstoSize > 0){
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getGestionnaireErreur().getErreur("pile_pleine").getLabel()) , topCheckPoint);
            }
            compiler.addInstruction(new TSTO(new ImmediateInteger(tstoSize)) , topCheckPoint);
        }

        buildRestoreRegister(compiler); // Avant de return, POP le tout !
    }

    public void buildSaveRegister(DecacCompiler compiler, CheckPoint checkPoint){
        for (int i = maxRegisterUsed; i >= 2; i--) {
            compiler.addInstruction(new PUSH(GPRegister.getR(i)) , checkPoint,true);
        }
    }
    public void buildRestoreRegister(DecacCompiler compiler){
        // Avant de return, POP le tout pour tout les RTS !
        for (CheckPoint unCheckPointRTS : lesCheckPointsRTS){
            for (int i = 2; i <= maxRegisterUsed; i++) {
                compiler.addInstruction(new POP(GPRegister.getR(i)) , unCheckPointRTS ,true);
            }
        }
    }

    // C'est apparemment une mauvaise pratique de faire des instanceof, mais je préfère tout faire ici, c'est plus pratique
    public void updateContext(DecacCompiler compiler, Instruction instruction){
        if( instruction instanceof BSR ){
            incrementEmpilement(2);
            decrementEmpilement(2);

        }else if ( instruction instanceof PUSH){
            incrementEmpilement(1);
        }else if (instruction instanceof POP){
            decrementEmpilement(1);
        }else if (instruction instanceof ADDSP){
            ADDSP inst = (ADDSP) instruction;
            int instValue = ((ImmediateInteger) inst.getOperand()).getValue();
            incrementEmpilement(instValue);
        }else if (instruction instanceof SUBSP){
            SUBSP inst = (SUBSP) instruction;
            int instValue = ((ImmediateInteger) inst.getOperand()).getValue();
            decrementEmpilement(instValue);
        }else if (instruction instanceof LOAD){
            LOAD inst = (LOAD) instruction;
            int regPos = ((GPRegister) inst.getOperand2()).getNumber();
            compiler.getGestionnaireMemoire().updateMaxRegisterUsed(regPos);
        }else if (instruction instanceof RTS){
            if (!this.isMain){
                // Ajoute un checkpoint RTS, car on ne connait pas encore le nombre de registre à POP, il peut y en avoir plus ensuite
                lesCheckPointsRTS.add(compiler.makeCheckPoint());

            }
        }
    }



}
