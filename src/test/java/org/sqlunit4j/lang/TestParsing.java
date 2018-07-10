package org.sqlunit4j.lang;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.junit.runner.RunWith;
import org.sqlunit4j.lang.SQLUnit4JParser.StatementsContext;

import com.github.iounit.IOUnitTestRunner;
import com.github.iounit.annotations.IOTest;
import com.github.iounit.annotations.IOUnitInput;

@RunWith(IOUnitTestRunner.class)
public class TestParsing {
    @IOUnitInput(extension = "sql")
    String scriptInput;

    @IOTest
    public String run() {
        final SQLUnit4JLexer lexer = new SQLUnit4JLexer(new ANTLRInputStream(scriptInput));
        final String actualTokens = printTokens(lexer.getAllTokens(), lexer.getVocabulary());

        lexer.reset();

        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final SQLUnit4JParser parser = new SQLUnit4JParser(tokens);
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        StatementsContext parseTree;
        try {
            parseTree = parser.statements();
        } catch (final Exception e) {
            tokens.seek(0); // rewind input stream
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            parseTree = parser.statements(); // STAGE 2
        }
        

        return actualTokens + "\r\n===========\r\n"
                + toPrettyTree(parseTree.toStringTree(Arrays.asList(parser.getRuleNames())));
    }

    public String toPrettyTree(final String tree) {
        return tree.replace("(", "(\r\n").replace(")", "\r\n)");
    }

    public static String printTokens(final List<? extends Token> tokens, final Vocabulary vocabulary) {
        final StringBuilder sb = new StringBuilder();
        for (final Token token : tokens) { // A token from a ParseTree object
            final String displayName = vocabulary.getSymbolicName(token.getType());
            sb.append(displayName);
            if (token.getChannel() == 1) {
                sb.append("(Hidden)");
            }
            if (displayName.length() < 25) {
                sb.append("                         ".substring(displayName.length(), 25));
            }
            sb.append('<');
            sb.append(token.getText().trim());
            sb.append('>');
            sb.append("\r\n");
        }
        return sb.toString().trim();
    }
}
