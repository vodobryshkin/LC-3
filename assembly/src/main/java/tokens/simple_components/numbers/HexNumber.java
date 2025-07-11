package tokens.simple_components.numbers;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class HexNumber implements Number {
    private final int decimalValue;
    private final String hexValue;

    private HexNumber(String hexValue) {
        this.hexValue = hexValue;

        String hexDigits;
        if (hexValue.startsWith("0x") || hexValue.startsWith("0X")) {
            hexDigits = hexValue.substring(2);
        } else if (hexValue.startsWith("x") || hexValue.startsWith("X")) {
            hexDigits = hexValue.substring(1);
        } else if (hexValue.startsWith("#")) {
            hexDigits = hexValue.substring(1);
        } else {
            throw new IllegalArgumentException("Invalid hex prefix");
        }

        int unsignedValue = Integer.parseInt(hexDigits, 16);

        if (hexDigits.length() == 4 && (unsignedValue & 0x8000) != 0) {
            this.decimalValue = -((~unsignedValue + 1) & 0xFFFF);
        } else {
            this.decimalValue = unsignedValue;
        }
    }

    public static HexNumber parseHexNumber(String number) {
        try {
            Pattern pattern = Pattern.compile("^(0[xX]|[xX]|#)[0-9A-Fa-f]{1,4}$");
            Matcher matcher = pattern.matcher(number);

            if (!matcher.matches()) {
                throw new NumberFormatException();
            }

            return new HexNumber(number);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public int getExtendedValue() {
        int p = Math.abs(decimalValue) & 0b1111;

        if (p > 7) {
            return p + 0b10000;
        }

        return p;
    }

    public int getExtendedForOffset() {
        int p = Math.abs(decimalValue) & 0b11111;

        if (p > 15) {
            return p + 0b100000;
        }

        return p;
    }

    @Override
    public String toString() {
        return "HexNumber(" + hexValue + ")";
    }
}