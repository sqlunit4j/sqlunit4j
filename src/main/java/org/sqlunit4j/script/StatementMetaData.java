package org.sqlunit4j.script;

import java.util.ArrayList;
import java.util.List;

import org.sqlunit4j.lang.SQLUnit4JParser.ParameterContext;

public class StatementMetaData {
    final StringBuilder raw = new StringBuilder();
    final StringBuilder query = new StringBuilder();
    final List<String> parameters = new ArrayList<>();
    final List<String> literals = new ArrayList<>();

    public StatementMetaData append(final String text) {
        raw.append(text);
        query.append(text);
        return this;
    }

    public StatementMetaData append(final ParameterContext parameterContext) {
        raw.append(parameterContext.getText());
        if (parameterContext.literal != null) {
            literals.add(parameterContext.literal.getText());
        } else if (parameterContext.name != null) {
            query.append("?");
            parameters.add(parameterContext.name.getText());
        } else {
            query.append("?");
            parameters.add(null);
        }
        return this;
    }

    public String getRaw() {
        return raw.toString();
    }

    public String getQuery() {
        return query.toString().trim();
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<String> getLiterals() {
        return literals;
    }
}