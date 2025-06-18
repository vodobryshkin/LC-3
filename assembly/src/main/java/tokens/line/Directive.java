package tokens.line;

import lombok.Getter;
import tokens.directive_components.DirectiveName;
import tokens.directive_components.DirectiveOperand;

@Getter
public class Directive {
    private final DirectiveName name;
    private DirectiveOperand operand = null;

    public Directive(DirectiveName name, DirectiveOperand operand) {
        this.name = name;
        this.operand = operand;
    }

    public Directive(DirectiveName name) {
        this.name = name;
    }

    public static Directive parseDirective(String directive) {
        if (directive == null || !directive.startsWith(".")) {
            return null;
        }

        String trimmed = directive.substring(1).trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String[] parts = trimmed.split("\\s+", 2);
        DirectiveName name = DirectiveName.parseDirectiveName(parts[0]);
        if (name == null) {
            return null;
        }

        if (parts.length == 1) {
            return new Directive(name);
        } else {
            DirectiveOperand operand = DirectiveOperand.parseDirectiveOperand(parts[1].trim());
            if (operand != null) {
                return new Directive(name, operand);
            }
            return null;
        }
    }

    @Override
    public String toString() {
        if (operand == null) {
            return "Directive(" + name.toString() + ")";
        }
        return "Directive(" + name.toString() + ", " + operand.toString() + ")";
    }
}