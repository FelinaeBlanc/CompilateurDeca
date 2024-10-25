package fr.ensimag.ima.pseudocode;


public class CheckPoint {
    
    private Line instruction; // Permet de connaître la position dans la liste des instructions via cette référence

    public CheckPoint(AbstractLine uneInstruction){
        instruction = (Line) uneInstruction;
    }

    public Line getLine(){
        return instruction;
    }
}
