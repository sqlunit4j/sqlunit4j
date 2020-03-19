package org.sqlunit4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;
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

    @IOTest(saveFailedOutput=false)
    public String run() throws Exception {
    	System.out.println("run....");
        try (final Connection connection = getConnection("");) {
            try (final Connection connection2 = getConnection("2");) {
            	final ScriptContext context = new ScriptContext(connection,connection2);
            	return scriptor.process(scriptInput, context);
            }
        }
    }

    public Connection getConnection(String suffix) throws ClassNotFoundException, SQLException {
    	suffix = suffix==null?"":suffix;
        final ResourceBundle bundle = getPropertyBundle();
        final String driver = bundle.getString("driver"+suffix) == null ? System.getProperty("sqlunit4j.driver"+suffix)
                : bundle.getString("driver"+suffix);
        final String url = bundle.getString("url"+suffix) == null ? System.getProperty("sqlunit4j.url"+suffix)
                : bundle.getString("url"+suffix);
        final String user = bundle.getString("user"+suffix) == null ? System.getProperty("sqlunit4j.user"+suffix)
                : bundle.getString("user"+suffix);
        final String password = bundle.getString("password"+suffix) == null ? System.getProperty("sqlunit4j.password"+suffix)
                : bundle.getString("password"+suffix);
    	if(suffix.length()>0 && driver == null) {
    		return null;
    	}
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
        final String packageLocation = getClass().getPackage().getName() + ".sqlunit4j";
        try {
            return ResourceBundle.getBundle(packageLocation);
        } catch (final Exception e) {
            try {
                return ResourceBundle.getBundle("sqlunit4j");
            } catch (final Exception e2) {
                try {
					return new PropertyResourceBundle(new FileInputStream("sqlunit4j.properties"));
				} catch (IOException e1) {
					System.err.println("Unable to find sqlunit4j.properties in the path");
				}
            }
        }
        return null;
    }
}
