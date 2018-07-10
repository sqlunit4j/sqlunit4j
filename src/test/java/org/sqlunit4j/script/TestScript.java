package org.sqlunit4j.script;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.h2.tools.SimpleResultSet;
import org.junit.runner.RunWith;
import org.sqlunit4j.SQLUnitSuite;

import com.github.iounit.IOUnitTestRunner;

@RunWith(IOUnitTestRunner.class)
public class TestScript extends SQLUnitSuite{
    Connection connection;
    static int tries = 0;

    public void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        try (final Statement stmt = connection.createStatement();) {
            stmt.execute("drop table TABLE if exists");
            stmt.execute("create table TABLE (X char(1), Y integer)");
            stmt.execute("insert into TABLE values ('A',1)");

            stmt.execute("drop table TABLE2 if exists");
            stmt.execute("create table TABLE2 (Col1 char(10))");
            stmt.execute("insert into TABLE2 values ('Aaa')");
            stmt.execute("insert into TABLE2 values ('Bbb')");
            stmt.execute("insert into TABLE2 values ('Ccc')");

            stmt.execute("drop ALIAS simpleProc if exists");
            stmt.execute("CREATE ALIAS simpleProc FOR \"org.sqlunit4j.script.TestScript.simpleProc\"");
            stmt.execute("drop ALIAS simpleProc2 if exists");
            stmt.execute("CREATE ALIAS simpleProc2 FOR \"org.sqlunit4j.script.TestScript.simpleProc2\"");
            tries = 0;
            stmt.execute("drop ALIAS failN if exists");
            stmt.execute("CREATE ALIAS failN FOR \"org.sqlunit4j.script.TestScript.failN\"");

        }
        registerStatementHandler(new StatementHandler(){

            @Override
            public String doStatement(String statement, ScriptContext context) {
                return "TEST::" + statement;
            }

            @Override
            public boolean isHandler(String key) {
                return true;
            }
            
        });
    }
    


    public static ResultSet simpleProc(String input) {
        SimpleResultSet rs = new SimpleResultSet();
        if (input != null) {
            rs.addColumn("ID", Types.INTEGER, 10, 0);
            rs.addColumn("NAME", Types.VARCHAR, 255, 0);
            rs.addRow(0, input);
        }
        return rs;
    }
    
    public static SimpleResultSet failN(String input) {
        SimpleResultSet rs = new SimpleResultSet();
        if(tries++>=3){
            rs.addColumn("ID", Types.INTEGER, 10, 0);
            rs.addColumn("NAME", Types.VARCHAR, 255, 0);
            rs.addRow(0, input);
        }
        return rs;
    }
    
    public static String simpleProc2(String input) {
        return input.toLowerCase();
    }



    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return connection;
    }
}
