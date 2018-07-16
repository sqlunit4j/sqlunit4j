package org.sqlunit4j.script;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.sqlunit4j.lang.SQLUnit4JLexer;
import org.sqlunit4j.lang.SQLUnit4JParser;
import org.sqlunit4j.lang.SQLUnit4JParser.StatementsContext;

public class Parser {

    public static StatementsContext parse(final String scriptInput) {
        final SQLUnit4JLexer lexer = new SQLUnit4JLexer(new ANTLRInputStream(scriptInput));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final SQLUnit4JParser parser = new SQLUnit4JParser(tokens);
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        try {
            return parser.statements();
        } catch (final Exception e) {// STAGE 2
            tokens.seek(0); // rewind input stream & reset the parser
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            return parser.statements();
        }
    }
}
