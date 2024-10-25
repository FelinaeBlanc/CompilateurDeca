package fr.ensimag.ima.pseudocode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract representation of an IMA program, i.e. set of Lines.
 *
 * @author Ensimag
 * @date 21/04/2023
 */
public class IMAProgram {
    private final LinkedList<AbstractLine> lines = new LinkedList<AbstractLine>();

    public void add(AbstractLine line) {
        lines.add(line);
    }

    public void addComment(String s) {
        lines.add(new Line(s));
    }

    public void addLabel(Label l) {
        lines.add(new Line(l));
    }

    public void addInstruction(Instruction i) {
        lines.add(new Line(i));
    }

    public void addInstruction(Instruction i, String s) {
        lines.add(new Line(null, i, s));
    }

    // CheckPoint systeme
    public void addInstruction(Instruction i, CheckPoint checkPoint){
        int checkPointPos = lines.indexOf(checkPoint.getLine());
        if (checkPointPos == -1){checkPointPos = 0;} // Si le check a en ref null, ça implique qu'il a été créer quand il n'y avait pas d'instructions, donc index 0.
        
        lines.add(checkPointPos+1, new Line(i)); // Ajoute après 
    }
    public CheckPoint makeCheckPoint(){
        return new CheckPoint(lines.getLast());
    }

    /**
     * Append the content of program p to the current program. The new program
     * and p may or may not share content with this program, so p should not be
     * used anymore after calling this function.
     */
    public void append(IMAProgram p) {
        lines.addAll(p.lines);
    }
    
    /**
     * Add a line at the front of the program.
     */
    public void addFirst(Line l) {
        lines.addFirst(l);
    }

    /**
     * Display the program in a textual form readable by IMA to stream s.
     */
    public void display(PrintStream s) {
        for (AbstractLine l: lines) {
            l.display(s);
        }
    }

    /**
     * Return the program in a textual form readable by IMA as a String.
     */
    public String display() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        display(s);
        return out.toString();
    }

    public void addFirst(Instruction i) {
        addFirst(new Line(i));
    }
    
    public void addFirst(Instruction i, String comment) {
        addFirst(new Line(null, i, comment));
    }
}
