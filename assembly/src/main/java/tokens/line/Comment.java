package tokens.line;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Comment(String comment) {
    public static Comment parseComment(String comment) {
        try {
            Pattern pattern = Pattern.compile(";.+");
            Matcher matcher = pattern.matcher(comment);

            if (!matcher.matches()) {
                throw new IllegalArgumentException();
            }

            return new Comment(comment);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Comment(\"" + comment + "\")";
    }
}
