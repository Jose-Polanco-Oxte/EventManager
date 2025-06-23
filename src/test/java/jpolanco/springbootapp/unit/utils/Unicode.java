package jpolanco.springbootapp.unit.utils;

public class Unicode {
    public static String escapeUnicode(String input) {
        if (input == null) return null;

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                case '\"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                default:
                    if (c < 32 || c > 126) {
                        sb.append(String.format("\\u%04X", (int)c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}
