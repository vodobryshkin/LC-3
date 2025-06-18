package tokens.simple_components.numbers;

public record DecimalNumber(int value) implements Number {

    public static DecimalNumber parseDecimalNumber(String number) {
        try {
            int value = Integer.parseInt(number);

            if (value > 32767 || value < -32768) {
                throw new NumberFormatException();
            }

            return new DecimalNumber(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "DecimalNumber(" + value + ")";
    }
}
