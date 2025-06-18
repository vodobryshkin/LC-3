package tokens.operands;

import tokens.directive_components.DirectiveOperand;
import tokens.instruction_components.Operand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record StringOperand(String stringOperand) implements Operand, DirectiveOperand {
    public static StringOperand parseStringOperand(String stringOperand) {
        try {
            Pattern pattern = Pattern.compile("\".*\"");
            Matcher matcher = pattern.matcher(stringOperand);

            if (!matcher.matches()) {
                throw new IllegalArgumentException();
            }

            return new StringOperand(stringOperand);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "StringOperand(\"" + stringOperand + "\")";
    }
}