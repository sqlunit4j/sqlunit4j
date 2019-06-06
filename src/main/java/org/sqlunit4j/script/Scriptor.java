package org.sqlunit4j.script;

import java.sql.CallableStatement;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlunit4j.lang.SQLUnit4JParser.AnnotationParameterContext;
import org.sqlunit4j.lang.SQLUnit4JParser.AnnotationPhraseContext;
import org.sqlunit4j.lang.SQLUnit4JParser.AnnotationWordContext;
import org.sqlunit4j.lang.SQLUnit4JParser.CallStatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.DashCommentContext;
import org.sqlunit4j.lang.SQLUnit4JParser.DefStatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.DeleteStatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.OtherStatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.ParameterContext;
import org.sqlunit4j.lang.SQLUnit4JParser.PrefixPhraseContext;
import org.sqlunit4j.lang.SQLUnit4JParser.PrintPhraseContext;
import org.sqlunit4j.lang.SQLUnit4JParser.SelectStatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.StatementBodyContext;
import org.sqlunit4j.lang.SQLUnit4JParser.StatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.StatementsContext;
import org.sqlunit4j.lang.SQLUnit4JParser.StringLiteralContext;
import org.sqlunit4j.lang.SQLUnit4JParser.UpdateStatementContext;
import org.sqlunit4j.lang.SQLUnit4JParser.WithStatementContext;

import com.ibm.icu.math.BigDecimal;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.AsciiTableException;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a7.A7_Grids;

public class Scriptor {
    final Logger logger = LoggerFactory.getLogger(Scriptor.class);
    final List<StatementHandler> statementHandlers = new ArrayList<>();

    public Scriptor() {

    }

    public String process(final String script, final ScriptContext context) throws Exception {
        final StatementsContext statementsContext = Parser.parse(script);
        final List<TreeNode> root = treeify(statementsContext.statement());
        return process(root, context);
    }

    private List<TreeNode> treeify(final List<StatementContext> statements) {
        final Stack<List<TreeNode>> stack = new Stack<>();
        final List<TreeNode> nodes = new ArrayList<>();
        stack.push(nodes);
        TreeNode priorTreeNode = null;
        for (final StatementContext statement : statements) {
            final int currentNestLevel = statement.indent() == null ? 0 : statement.indent().getText().length();
            final TreeNode currentTreeNode = new TreeNode(statement, currentNestLevel);
            if (priorTreeNode == null) {
                nodes.add(currentTreeNode);
            } else {
                if (currentNestLevel > priorTreeNode.getIndent()) {
                    stack.push(priorTreeNode.getChildren());
                } else if (currentNestLevel < priorTreeNode.getIndent() && stack.size() > 1) {
                    stack.pop();
                }
                stack.peek().add(currentTreeNode);
            }
            priorTreeNode = currentTreeNode;
        }
        return nodes;
    }

    public String process(final List<TreeNode> statements, final ScriptContext context) throws Exception {
        final StringBuilder totalResult = new StringBuilder();
        for (final TreeNode statementTree : statements) {
            final StatementContext statement = statementTree.getStatementContext();
            try {
                if (statement.prefixPhrase() != null) {
                    final String output = doPrefix(statement.prefixPhrase(), context);
                    if(!hasAnnotation(statement, "Silent")) {
                    	logger.info(output);
                    }
                    if (hasAnnotation(statement, "Verify") || isLastStatement(statements, statementTree)) {
                        totalResult.append(output);
                    }
                    continue;
                }
                if (statement.defStatement() != null) {
                    final String output = doDef(statement.defStatement(), context);
                    if(!hasAnnotation(statement, "Silent")) {
                        logger.info(output);
                    }
                    if (hasAnnotation(statement, "Verify") || isLastStatement(statements, statementTree)) {
                        totalResult.append(output);
                    }
                    continue;
                }
                if (statement.printPhrase() != null) {
                    final String output = doPrint(statement.printPhrase(), context);
                    if(!hasAnnotation(statement, "Silent")) {
                        totalResult.append(output);
                    }
                    continue;
                }
                final StatementMetaData result = new StatementMetaData();

                for (final DashCommentContext dd : statement.dashComment()) {
                    doStatement(dd, result);
                }
                if (statement.selectStatement() != null) {
                    doStatement(statement.selectStatement(), result);
                }
                if (statement.deleteStatement() != null) {
                    doStatement(statement.deleteStatement(), result);
                }
                if (statement.updateStatement() != null) {
                    doStatement(statement.updateStatement(), result);
                }
                if (statement.withStatement() != null) {
                    doStatement(statement.withStatement(), result);
                }
                if (statement.callStatement() != null) {
                    doStatement(statement.callStatement(), result);
                }
                if (statement.otherStatement() != null) {
                    doStatement(statement.otherStatement(), result);
                }
                totalResult.append(context.getIndent()).append(result.getQuery().toString()).append(";\r\n");
                logger.info(context.getIndent() + result.getQuery());
                final String output = doQuery(result, context, statementTree);
                // if(hasAnnotation(statement,"Verify") ||
                // statements.indexOf(statementTree) == statements.size()-1)
                totalResult.append(output);
            } catch (final Exception e) {
                throw new RuntimeException("On line " + statement.start.getLine() + ":"
                        + statement.start.getCharPositionInLine() + " " + e.getMessage(), e);
            }
        }
        return totalResult.toString();
    }

    private boolean isLastStatement(final List<TreeNode> statements, final TreeNode statementTree) {
        return statements.indexOf(statementTree) == statements.size() - 1 && statementTree.getChildren().size() == 0;
    }

    private boolean hasAnnotation(final StatementContext statement, final String annoName) {
        if (statement.annotationPhrase() != null) {
            for (final AnnotationPhraseContext phrase : statement.annotationPhrase()) {
                for (final AnnotationWordContext word : phrase.annotationWord()) {
                    if (word != null && annoName.equalsIgnoreCase(word.getText())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Object getAnnotationParm(final StatementContext statement, final String annoName, final String parm,
            final Object defaultVal) {
        if (statement.annotationPhrase() != null) {
            for (final AnnotationPhraseContext phrase : statement.annotationPhrase()) {
                for (final AnnotationWordContext word : phrase.annotationWord()) {
                    if (word != null && annoName.equalsIgnoreCase(word.getText())) {
                        for (final AnnotationParameterContext annoParm : phrase.annotationParameter()) {
                            if (annoParm.name.getText().equals(parm)) {
                                return annoParm.number != null ? Integer.parseInt(annoParm.number.getText())
                                        : annoParm.string.getText();
                            }
                        }
                    }
                }
            }
        }
        return defaultVal;
    }

    private String doPrefix(final PrefixPhraseContext prefixPhrase, final ScriptContext context) throws Exception {
        final String prefixText = prefixPhrase.prefix.getText().trim();
        for (final StatementHandler handler : statementHandlers) {
            if (handler != null && handler.isHandler(prefixText)) {
                final String retval = handler.doStatement(prefixPhrase.prefixBody().getText(), context);
                if (retval.length() > 0) {
                    return retval + "\r\n";
                }
                return "";
            }
        }
        throw new RuntimeException("Handler not registered for: " + prefixText);
    }

    private String doPrint(final PrintPhraseContext printPhrase, final ScriptContext context) {
        final StringBuilder output = new StringBuilder();
        if (printPhrase.printBody() != null) {
            output.append(context.getIndent());
            for (final ParameterContext parameter : printPhrase.printBody().parameter()) {
                if (parameter.name != null) {
                    output.append(context.getVariables().get(parameter.name.getText()));
                }
                if (parameter.literal != null) {
                    output.append(context.getVariables().get(parameter.literal.getText()));
                }
                output.append(" ");
            }
            for (final TerminalNode word : printPhrase.printBody().WORD()) {
                output.append(context.getVariables().get(word.getText()));
                output.append(" ");
            }
            for (final StringLiteralContext literal : printPhrase.printBody().stringLiteral()) {
                String text = literal.value.getText();
                final Matcher m = Pattern.compile(".*?[$][{]([^}]*)[}]").matcher(literal.value.getText());
                while (m.find()) {
                    final String key = m.group(1);
                    text = text.replace("${" + key + "}", context.getVariables().get(key) == null ? null
                            : context.getVariables().get(key).toString());
                }
                output.append(text);
                output.append(" ");
            }
            logger.info(output.toString());
            output.append("\r\n");
        }
        return output.toString();
    }

    private String doDef(final DefStatementContext defStatement, final ScriptContext context) {
        if (defStatement.value.NUMBER() != null) {
            context.getVariables().put(defStatement.variable.getText(),
                    new BigDecimal(defStatement.value.NUMBER().getText()));
        } else {
            context.getVariables().put(defStatement.variable.getText(),
                    defStatement.value.stringLiteral().value.getText());
        }
        return defStatement.variable.getText() + " = " + context.getVariables().get(defStatement.variable.getText());
    }

    private String doQuery(final StatementMetaData statementInfo, final ScriptContext context,
            final TreeNode currentStatement) throws Exception {
        final StringBuilder info = new StringBuilder();
        try (CallableStatement st = context.getConnection().prepareCall(statementInfo.getQuery());) {
            for (int parmNum = 1; parmNum <= st.getParameterMetaData().getParameterCount(); parmNum++) {
                if ((ParameterMetaData.parameterModeIn & st.getParameterMetaData().getParameterMode(parmNum)) > 0
                        || (ParameterMetaData.parameterModeInOut
                                & st.getParameterMetaData().getParameterMode(parmNum)) > 0) {
                    if (statementInfo.getParameters().size() >= parmNum) {
                        final String parmName = statementInfo.getParameters().get(parmNum - 1);
                        if (parmName != null) {
                            st.setObject(parmNum, context.getVariables().get(parmName),
                                    st.getParameterMetaData().getParameterType(parmNum));
                            final String representation = renderValue(
                                    st.getParameterMetaData().getParameterType(parmNum),
                                    context.getVariables().get(parmName));
                            // info.append(context.getIndent()).append("? = " +
                            // representation + "\r\n");
                            logger.info(context.getIndent() + "? = " + representation);
                        } else {
                            st.setObject(parmNum, null, st.getParameterMetaData().getParameterType(parmNum));
                            // info.append(context.getIndent()).append("? =
                            // null" + "\r\n");
                            logger.info(context.getIndent() + "? = null");
                        }
                    } else {
                        st.setObject(parmNum, null, st.getParameterMetaData().getParameterType(parmNum));
                        // info.append(context.getIndent()).append("? = null" +
                        // "\r\n");
                        logger.info(context.getIndent() + "? = null");
                    }

                }
                if ((ParameterMetaData.parameterModeOut & st.getParameterMetaData().getParameterMode(parmNum)) > 0
                        || (ParameterMetaData.parameterModeInOut
                                & st.getParameterMetaData().getParameterMode(parmNum)) > 0) {
                    st.registerOutParameter(parmNum, st.getParameterMetaData().getParameterType(parmNum));
                }
            }
            final long start = System.currentTimeMillis();
            st.execute();
            if (st.getUpdateCount() > 0) {
                info.append(context.getIndent()).append(st.getUpdateCount() + " records updated/deleted\r\n\r\n");
                logger.info(context.getIndent() + st.getUpdateCount() + " records updated/deleted");
            }
            processOutParams(statementInfo, context, st);
            ResultSet rsa = st.getResultSet();
            if (rsa != null) {
                int actualRows = 0;
                boolean hasNext = rsa.next();
                if (hasAnnotation(currentStatement.getStatementContext(), "WaitFor")) {
                    final Integer minRows = (Integer) getAnnotationParm(currentStatement.getStatementContext(),
                            "WaitFor", "rows", null);
                    final Integer waitFor = (Integer) getAnnotationParm(currentStatement.getStatementContext(),
                            "WaitFor", "maxMillis", 8000);
                    if (hasNext && minRows != null) {
                        rsa.last();
                        actualRows = rsa.getRow();
                        rsa.first();
                    }else{
                        actualRows = 0;
                    }
                    while (System.currentTimeMillis() < start + waitFor
                            && (!hasNext || (minRows != null && actualRows < minRows))) {
                        Thread.sleep(300);
                        st.execute();
                        if (st.getUpdateCount() > 0) {
                            info.append(context.getIndent())
                                    .append(st.getUpdateCount() + " records updated/deleted\r\n\r\n");
                            logger.info(context.getIndent() + st.getUpdateCount() + " records updated/deleted");
                        }
                        processOutParams(statementInfo, context, st);
                        rsa = st.getResultSet();
                        hasNext = rsa.next();
                        if (hasNext && minRows != null) {
                            rsa.last();
                            actualRows = rsa.getRow();
                            rsa.first();
                        }else{
                            actualRows = 0;
                        }
                    }
                }
                if (!hasNext) {
                    logger.info(context.getIndent() + "<EMPTY>");
                    return info.toString() + context.getIndent() + "<EMPTY>\r\n\r\n";
                }
                if (currentStatement.getChildren().size() > 0) {
                    return info.toString() + processResult(rsa, context, currentStatement);
                } else {
                    return info.toString() + renderResult(rsa, context, currentStatement);
                }
            }
        }
        return info.toString();
    }

    private void processOutParams(final StatementMetaData statementInfo, final ScriptContext context,
            final CallableStatement st) throws SQLException {
        for (int parmNum = 1; parmNum <= st.getParameterMetaData().getParameterCount(); parmNum++) {
            if ((ParameterMetaData.parameterModeOut & st.getParameterMetaData().getParameterMode(parmNum)) > 0
                    || (ParameterMetaData.parameterModeInOut
                            & st.getParameterMetaData().getParameterMode(parmNum)) > 0) {
                final String representation = renderValue(st.getParameterMetaData().getParameterType(parmNum),
                        st.getObject(parmNum));
                if (statementInfo.getParameters().size() >= parmNum) {
                    final String parmName = statementInfo.getParameters().get(parmNum - 1);
                    if (parmName != null) {
                        context.getVariables().put(parmName, st.getObject(parmNum));
                        // info.append(context.getIndent()).append("?" +
                        // parmName + " -> " + representation + "\r\n");
                        logger.info(context.getIndent() + "?" + parmName + " -> " + representation);
                    } else {
                        // info.append(context.getIndent()).append("? -> " +
                        // representation + "\r\n");
                        logger.info(context.getIndent() + "? -> " + representation);
                    }
                } else {
                    // info.append(context.getIndent()).append("? -> " +
                    // representation + "\r\n");
                    logger.info(context.getIndent() + "? -> " + representation);
                }
            }
        }
    }

    private String renderValue(final int parameterType, final Object object) {
        if (parameterType == Types.CHAR || parameterType == Types.DATE || parameterType == Types.NCHAR
                || parameterType == Types.NCLOB || parameterType == Types.NVARCHAR || parameterType == Types.TIME
                || parameterType == Types.TIMESTAMP || parameterType == Types.VARCHAR) {
            return "'" + object + "'";
        }
        return object.toString();
    }

    private void doStatement(final DashCommentContext dashStatement, final StatementMetaData result) {
        result.append(dashStatement.getText());
    }

    private void doStatement(final SelectStatementContext selectStatement, final StatementMetaData result) {
        result.append(selectStatement.getChild(0).getText());
        doStatementBody(selectStatement.statementBody(), result);
    }

    private void doStatement(final CallStatementContext selectStatement, final StatementMetaData result) {
        result.append(selectStatement.getChild(0).getText());
        doStatementBody(selectStatement.statementBody(), result);
    }

    private void doStatement(final DeleteStatementContext selectStatement, final StatementMetaData result) {
        result.append(selectStatement.getChild(0).getText());
        doStatementBody(selectStatement.statementBody(), result);
    }

    private void doStatement(final UpdateStatementContext selectStatement, final StatementMetaData result) {
        result.append(selectStatement.getChild(0).getText());
        doStatementBody(selectStatement.statementBody(), result);
    }

    private void doStatement(final WithStatementContext selectStatement, final StatementMetaData result) {
        result.append(selectStatement.getChild(0).getText());
        doStatementBody(selectStatement.statementBody(), result);
    }

    private void doStatement(final OtherStatementContext selectStatement, final StatementMetaData result) {
        doStatementBody(selectStatement.statementBody(), result);
    }

    private void doStatementBody(final StatementBodyContext statementBody, final StatementMetaData result) {

        for (final ParseTree child : statementBody.children) {
            if (child instanceof TerminalNodeImpl) {
                final TerminalNodeImpl node = (TerminalNodeImpl) child;
                if (node.getSymbol().getType() == Recognizer.EOF) {
                    continue;
                }
            }
            if (child instanceof ParameterContext) {
                result.append((ParameterContext) child);
            } else {
                result.append(child.getText());
            }
        }
    }

    public String renderResult(final ResultSet rs, final ScriptContext context, final TreeNode currentStatement)
            throws NullPointerException, AsciiTableException, SQLException {
        final AsciiTable asciiTable = new AsciiTable();
        if (rs != null) {
            asciiTable.addRule();
            final List<Object> head = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                head.add(rs.getMetaData().getColumnName(i));
            }
            asciiTable.addRow(head);
            asciiTable.addRule();

            // The first row from result set is loaded into the script context
            // as variables
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                context.getVariables().put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            do {
                final List<Object> cols = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    cols.add(rs.getObject(i) == null ? "" : rs.getObject(i));
                }
                asciiTable.addRow(cols);
            } while (rs.next());
            asciiTable.addRule();
        }
        asciiTable.getContext().setGrid(A7_Grids.minusBarPlusEquals());
        asciiTable.getContext().setFrameLeftMargin(context.getNestLevel() * 2);
        asciiTable.getRenderer().setCWC(new CWC_LongestLine());
        final String tableTxt = asciiTable.render();
        logger.info("\r\n" + tableTxt);
        return tableTxt + "\r\n\r\n";
    }

    private String processResult(final ResultSet rs, final ScriptContext context, final TreeNode currentStatement)
            throws Exception {
        final StringBuilder output = new StringBuilder();
        if (rs != null) {
            do {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    context.getVariables().put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                output.append(process(currentStatement.getChildren(), context.subContext())).append("\r\n");
            } while (rs.next());
        }
        return output.toString();
    }

    /**
     * Gets next sibling of ParseTree node.
     *
     * @param node
     *            ParseTree node
     * @return next sibling of ParseTree node.
     */
    private static ParseTree getNextSibling(final ParseTree node) {
        ParseTree nextSibling = null;

        if (node.getParent() != null) {
            final ParseTree parent = node.getParent();
            int index = 0;
            while (true) {
                final ParseTree currentNode = parent.getChild(index);
                if (currentNode.equals(node)) {
                    nextSibling = parent.getChild(index + 1);
                    break;
                }
                index++;
            }
        }
        return nextSibling;
    }

    public void registerStatementHandler(final StatementHandler h) {
        statementHandlers.add(h);
    }
}
