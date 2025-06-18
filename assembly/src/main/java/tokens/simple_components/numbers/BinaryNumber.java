package tokens.simple_components.numbers;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class BinaryNumber implements Number {
    private final int decimalValue;
    private final String binaryValue;

    public BinaryNumber(String binaryValue) {
        this.binaryValue = binaryValue;
        String binaryDigits = binaryValue.substring(2);

        if (binaryDigits.length() == 16 && binaryDigits.charAt(0) == '1') {
            this.decimalValue = -((~Integer.parseInt(binaryDigits, 2) + 1) & 0xFFFF);
        } else {
            this.decimalValue = Integer.parseInt(binaryDigits, 2);
        }
    }

    public static BinaryNumber parseBinaryNumber(String number) {
        try {
            Pattern pattern = Pattern.compile("^0?b[01]{1,16}$");
            Matcher matcher = pattern.matcher(number);

            if (!matcher.matches()) {
                throw new NumberFormatException();
            }

            return new BinaryNumber(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "BinaryNumber(" + binaryValue + ")";
    }
}