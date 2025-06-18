package tokens.operands;

import tokens.instruction_components.Operand;
import tokens.simple_components.Identifier;
import tokens.simple_components.numbers.Number;

public interface MemoryOperand extends Operand {
    static MemoryOperand parseMemoryOperand(String token) {
        Number number = Number.parseNumber(token);

        if (number != null) {
            return number;
        }

        return Identifier.parseIdentifier(token);
    }
}
