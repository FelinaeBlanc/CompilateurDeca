package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 21/04/2023
 */
public class ImmediateInteger extends DVal {
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
    }

    public ImmediateInteger(boolean value) {
        super();
        this.value = (value) ? 1 : 0;
    }

    public int getValue(){
        return value;
    }
    
    @Override
    public String toString() {
        return "#" + value;
    }
}
