package org.sqlunit4j.script;

import java.util.ArrayList;
import java.util.List;

import org.sqlunit4j.lang.SQLUnit4JParser.StatementContext;

public class TreeNode {

    StatementContext statementContext;
    List<TreeNode> children = new ArrayList<>();
    int indent;
    public TreeNode(StatementContext statementContext,int indent) {
        super();
        this.statementContext = statementContext;
        this.indent=indent;
    }
    public StatementContext getStatementContext() {
        return statementContext;
    }
    public List<TreeNode> getChildren() {
        return children;
    }
    public int getIndent() {
        return indent;
    }
    
}
