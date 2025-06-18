package tokens.simple_components;

import tokens.directive_components.DirectiveOperand;
import tokens.operands.MemoryOperand;
import tokens.operands.SecondRegisterOperandComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Identifier(String identifier) implements DirectiveOperand, MemoryOperand, SecondRegisterOperandComponent {
    public static Identifier parseIdentifier(String identifier) {
        try {
            Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
            Matcher matcher = pattern.matcher(identifier);

            if (!matcher.matches()) {
                throw new IllegalArgumentException();
            }

            return new Identifier(identifier);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Identifier(\"" + identifier + "\")";
    }
}
