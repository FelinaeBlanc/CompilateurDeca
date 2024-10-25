package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * Line of code in an IMA program.
 *
 * @author Ensimag
 * @date 21/04/2023
 */
public class rawAsm extends AbstractLine {

    protected String code;

    public rawAsm(String rawAsm) {
        super();
        this.code = rawAsm;
    }

    @Override
    void display(PrintStream s) {

        if (code != null) {
            s.print(code);
        }
    }

    public void setCode(String rawAsm) {
        this.code = rawAsm;
    }

    public String getCode() {
        return this.code;
    }
    
}
