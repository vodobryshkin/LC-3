package tokens.line;

public record Line(Label label, Object instructionOrDirective, Comment comment) {

    public static Line parseLine(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            return new Line(null, null, null);
        }

        Label label = null;
        Object instructionOrDirective = null;
        Comment comment = null;

        int commentIndex = line.indexOf(';');
        String codePart = line;
        if (commentIndex != -1) {
            comment = Comment.parseComment(line.substring(commentIndex));
            codePart = line.substring(0, commentIndex).trim();
        }

        if (codePart.isEmpty()) {
            return new Line(label, instructionOrDirective, comment);
        }

        int labelEnd = codePart.indexOf(':');
        if (labelEnd != -1) {
            String labelStr = codePart.substring(0, labelEnd + 1);
            label = Label.ParseLabel(labelStr);
            if (label == null) {
                return null;
            }
            codePart = codePart.substring(labelEnd + 1).trim();
        }

        if (codePart.isEmpty()) {
            return new Line(label, instructionOrDirective, comment);
        }

        if (codePart.startsWith(".")) {
            instructionOrDirective = Directive.parseDirective(codePart);
        } else {
            int firstSpace = codePart.indexOf(' ');
            if (firstSpace == -1) {
                return null;
            }

            String opcodePart = codePart.substring(0, firstSpace);
            String operandsPart = codePart.substring(firstSpace + 1).trim();

            String fullInstruction = opcodePart + " " + operandsPart;
            instructionOrDirective = Instruction.parseInstruction(fullInstruction);
        }

        return new Line(label, instructionOrDirective, comment);
    }

    private static String joinRemainingParts(String[] parts, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < parts.length; i++) {
            if (i > startIndex) {
                sb.append(" ");
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Line(");
        int len = 0;

        if (label != null) {
            sb.append(label.toString());
            len++;
        }

        if (instructionOrDirective != null) {
            if (len > 0) {
                sb.append(", ");
            }
            sb.append(instructionOrDirective.toString());
            len++;
        }

        if (comment != null) {
            if (len > 0) {
                sb.append(", ");
            }
            sb.append(comment.toString());
        }

        sb.append(")");

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(Line.parseLine(".HALT"));
    }
}