package org.sqlunit4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlunit4j.script.ScriptContext;
import org.sqlunit4j.script.Scriptor;
import org.sqlunit4j.script.StatementHandler;

import com.github.iounit.IOUnitTestRunner;
import com.github.iounit.annotations.IOTest;
import com.github.iounit.annotations.IOUnitInput;

@RunWith(IOUnitTestRunner.class)
public abstract class SQLUnitSuite {
    @IOUnitInput(extension = "sql")
    protected String scriptInput;
    protected Scriptor scriptor = new Scriptor();

    public void registerStatementHandler(final StatementHandler handler) {
        scriptor.registerStatementHandler(handler);
    }

    @IOTest
    public String run() throws Exception {
        try (final Connection connection = getConnection();) {
            final ScriptContext context = new ScriptContext(connection);
            return scriptor.process(scriptInput, context);
        }
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        final ResourceBundle bundle = getPropertyBundle();
        final String driver = bundle.getString("driver") == null ? System.getProperty("sqlunit4j.driver")
                : bundle.getString("driver");
        final String url = bundle.getString("url") == null ? System.getProperty("sqlunit4j.url")
                : bundle.getString("url");
        final String user = bundle.getString("user") == null ? System.getProperty("sqlunit4j.user")
                : bundle.getString("user");
        final String password = bundle.getString("password") == null ? System.getProperty("sqlunit4j.password")
                : bundle.getString("password");
        if (url == null && bundle == null) {
            final Logger logger = LoggerFactory.getLogger(SQLUnitSuite.class);
            logger.error(
                    "sqlunit4j.properties not found.  Create it, override SQLUnitSuite.getConnection(), or set sqlunit4j.url environment var.");
        } else if (driver == null || driver.trim().length() == 0) {
            throw new RuntimeException("sqlunit4j.properties needs to provide a driver property");
        } else if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("sqlunit4j.properties needs to provide a url property");
        }
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    protected ResourceBundle getPropertyBundle() {
        System.out.println();
        final String packageLocation = getClass().getPackage().getName() + ".sqlunit4j";
        try {
            return ResourceBundle.getBundle(packageLocation);
        } catch (final Exception e) {
            try {
                return ResourceBundle.getBundle("sqlunit4j");
            } catch (final Exception e2) {
            }
        }
        return null;
    }
}
