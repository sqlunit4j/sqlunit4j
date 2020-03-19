package org.sqlunit4j.script;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptContext {
    final List<Connection> connection= new ArrayList<>();
    final Map<String, Object> variables = new HashMap<>();
    final ScriptContext parentContext;
    final int nestLevel;
    final String indent;
    
    public ScriptContext(final Connection ... conn) {
        this.connection.addAll(Arrays.asList(conn));
        this.parentContext = null;
        this.nestLevel = 0;
        this.indent = "";
    }

    public ScriptContext(List<Connection> connection, final ScriptContext parentContext, final int nestLevel) {
        this.connection.addAll(connection);
        this.parentContext = parentContext;
        this.nestLevel = nestLevel;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nestLevel; i++) {
            sb.append("  ");
        }
        this.indent = sb.toString();
    }

    public Connection getConnection(int index) {
    	if(index>0&&index<=connection.size()) {
    		return connection.get(index-1);
    	}
    	return null;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public ScriptContext getParentContext() {
        return parentContext;
    }

    public int getNestLevel() {
        return nestLevel;
    }

    public ScriptContext subContext() {
        final ScriptContext subContext = new ScriptContext(connection, this, nestLevel + 1);
        subContext.variables.putAll(variables);

        return subContext;
    }

    public String getIndent() {
        return indent;
    }
}
