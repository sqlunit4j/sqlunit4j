package org.sqlunit4j.script;

public interface StatementHandler {

    String doStatement(String statement, ScriptContext context) throws Exception;

    boolean isHandler(String key);
}
