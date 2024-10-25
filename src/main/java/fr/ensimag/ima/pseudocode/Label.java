package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Representation of a label in IMA code. The same structure is used for label
 * declaration (e.g. foo: instruction) or use (e.g. BRA foo).
 *
 * @author Ensimag
 * @date 21/04/2023
 */
public class Label extends Operand {

    private static long labelCounter = 0;

    @Override
    public String toString() {
        return name;
    }


    public Label(String name) {
        this(name, true);
    }

    public Label(String name, boolean withNumber) {
        super();

        
        withNumber = true;
        if (name.length() > 1024){
            name = name.substring(0,500)+ Math.abs(name.hashCode());
        }
        // Sanitize la string
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9_.]");
        Matcher matcher = pattern.matcher(name);
        name = matcher.replaceAll("X");


        Validate.isTrue(name.length() <= 1024, "Label name too long, not supported by IMA");
        Validate.isTrue(name.matches("^[a-zA-Z][a-zA-Z0-9_.]*$"), "Invalid label name " + name);

        this.name = name + Long.toString(labelCounter);
        labelCounter ++;

    }
    private String name;
}
