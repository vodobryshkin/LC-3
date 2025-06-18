package tokens.line;

import tokens.simple_components.Identifier;

public record Label(Identifier identifier) {

    public static Label ParseLabel(String label) {
        Identifier identifier = Identifier.parseIdentifier(label.substring(0, label.length() - 1));

        if (identifier == null || label.getBytes()[label.length() - 1] != ':') {
            return null;
        }

        return new Label(identifier);
    }

    @Override
    public String toString() {
        return "Label(" + identifier.toString() + ")";
    }
}
