package tokens.directive_components;

import tokens.instruction_components.Operand;
import tokens.operands.StringOperand;
import tokens.simple_components.numbers.Number;

public interface DirectiveOperand extends Operand {
    static DirectiveOperand parseDirectiveOperand(String directiveOperand) {
        Number number = Number.parseNumber(directiveOperand);

        if (number != null) {
            return number;
        }

        return StringOperand.parseStringOperand(directiveOperand);
    }
}
