package com.greenlaw110.rythm.internal.parser.build_in;

import com.greenlaw110.rythm.exception.ParseException;
import com.greenlaw110.rythm.internal.Keyword;
import com.greenlaw110.rythm.internal.dialect.Rythm;
import com.greenlaw110.rythm.internal.parser.ParserBase;
import com.greenlaw110.rythm.spi.IContext;
import com.greenlaw110.rythm.spi.IParser;
import com.greenlaw110.rythm.spi.Token;
import com.greenlaw110.rythm.utils.TextBuilder;
import com.stevesoft.pat.Regex;

/**
 * Parse @get("name")
 */
public class GetParser extends KeywordParserFactory {

    @Override
    public Keyword keyword() {
        return Keyword.GET;
    }

    @Override
    protected String patternStr() {
        return "^(%s%s((?@())))";
    }

    @Override
    public IParser create(IContext ctx) {
        return new ParserBase(ctx) {
            @Override
            public TextBuilder go() {
                Regex r = reg(dialect());
                if (!r.search(remain())) return null;
                step(r.stringMatched().length()); // remain: @get("name")...
                String s = r.stringMatched(2); // s: ("name")
                s = s.substring(1); // s: "name")
                s = s.substring(0, s.length() - 1); // s: "name"
                r = new Regex("((?@\"\")|(?@'')|[a-zA-Z_][\\w_]+)");
                if (!r.search(s)) throw new ParseException(ctx().currentLine(), "Error parsing @get tag. Correct usage: @get(\"name\")");
                s = r.stringMatched(1); // propName: "name"
                if (s.startsWith("\"") || s.startsWith("'")) {
                    s = s.substring(1);
                    s = s.substring(0, s.length() - 1);
                    // propName: name
                }
                final String propName = s;
                return new Token("", ctx()) {
                    @Override
                    protected void output() {
                        p("\np(_getRenderProperty(\"").p(propName).p("\"));");
                    }
                };
            }
        };
    }

    public static void main(String[] args) {
        String s = "@get(\"var\")";
        GetParser ap = new GetParser();
        Regex r = ap.reg(new Rythm());
        System.out.println(r);
        if (r.search(s)) {
            System.out.println("m: " + r.stringMatched());
            System.out.println("1: " + r.stringMatched(1));
            System.out.println("2: " + r.stringMatched(2));
            System.out.println("3: " + r.stringMatched(3));
            System.out.println("4: " + r.stringMatched(4));
            System.out.println("5: " + r.stringMatched(5));
        }
//        Regex r = new Regex("((?@\"\")|(?@'')|[a-zA-Z_][\\w_]+)\\s*[=:]\\s*('.'|(?@\"\")|[a-zA-Z_][a-zA-Z0-9_\\.]*(?@())*(?@[])*(?@())*(\\.[a-zA-Z][a-zA-Z0-9_\\.]*(?@())*(?@[])*(?@())*)*)");
//        String s = "'xyz': \"Hello\"";
//        if (r.search(s)) {
//            System.out.println("1 " + r.stringMatched(1));
//            System.out.println("2 " + r.stringMatched(2));
//            System.out.println("3 " + r.stringMatched(3));
//        }
    }

}
