package tokens.simple_components.numbers;

import tokens.directive_components.DirectiveOperand;
import tokens.operands.ImmediateOperand;
import tokens.operands.MemoryOperand;

public interface Number extends DirectiveOperand, ImmediateOperand, MemoryOperand {
    static Number parseNumber(String number) {
        DecimalNumber decimalNumber = DecimalNumber.parseDecimalNumber(number);
        if (decimalNumber != null) {
            return decimalNumber;
        }

        BinaryNumber binaryNumber = BinaryNumber.parseBinaryNumber(number);
        if (binaryNumber != null) {
            return binaryNumber;
        }

        return HexNumber.parseHexNumber(number);
    }
}
