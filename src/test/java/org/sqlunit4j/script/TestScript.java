package org.sqlunit4j.script;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hsqldb.ColumnBase;
import org.hsqldb.jdbc.JDBCConnection;
import org.hsqldb.jdbc.JDBCResultSet;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorClient;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.Type;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.sqlunit4j.SQLUnitSuite;

import com.github.iounit.IOUnitTestRunner;

@RunWith(IOUnitTestRunner.class)
public class TestScript extends SQLUnitSuite {
	static Connection connection;
	static int tries = 0;

	@Before
	public void setUp() throws SQLException, ClassNotFoundException {
		// Class.forName("org.h2.Driver");
		// connection = DriverManager.getConnection("jdbc:h2:mem:test");
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:myDb", "sa", "sa");
		try (final Statement stmt = connection.createStatement();) {
			stmt.execute("drop table TABLE1 if exists");
			stmt.execute("create table TABLE1 (X char(1), Y integer)");
			stmt.execute("insert into TABLE1 values ('A',1)");

			stmt.execute("drop table TABLE2 if exists");
			stmt.execute("create table TABLE2 (Col1 varchar(10))");
			stmt.execute("insert into TABLE2 values ('Aaa')");
			stmt.execute("insert into TABLE2 values ('Bbb')");
			stmt.execute("insert into TABLE2 values ('Ccc')");

			stmt.execute("drop PROCEDURE simpleProc if exists");
			stmt.execute("CREATE PROCEDURE  simpleProc(in input varchar(100)) " 
					+ "LANGUAGE JAVA modifies SQL DATA "
					+ "DYNAMIC RESULT SETS 1 "
					+ "EXTERNAL NAME 'CLASSPATH:org.sqlunit4j.script.TestScript.simpleProc'");

			stmt.execute("drop PROCEDURE simpleProcOut if exists");
			stmt.execute("CREATE PROCEDURE  simpleProcOut(in input varchar(200),out output varchar(200)) " 
					+ "LANGUAGE JAVA modifies SQL DATA "
					+ "DYNAMIC RESULT SETS 1 "
					+ "EXTERNAL NAME 'CLASSPATH:org.sqlunit4j.script.TestScript.simpleProcOut'");

			tries = 0;

			stmt.execute("drop procedure failN if exists");
			stmt.execute("CREATE PROCEDURE  failN(in input varchar(100)) " 
					+ "LANGUAGE JAVA modifies SQL DATA "
					+ "DYNAMIC RESULT SETS 1 "
					+ "EXTERNAL NAME 'CLASSPATH:org.sqlunit4j.script.TestScript.failN'");

		}
		registerStatementHandler(new StatementHandler() {

			@Override
			public String doStatement(final String statement, final ScriptContext context) {
				return "TEST::" + statement;
			}

			@Override
			public boolean isHandler(final String key) {
				return true;
			}

		});
	}

	public static void simpleProc(final String input,ResultSet[] result) throws SQLException {
		if (input != null) {
			ResultMetaData meta = ResultMetaData.newSimpleResultMetaData(new Type[]{Type.SQL_INTEGER,Type.SQL_VARCHAR});
			meta.columns= new ColumnBase[] {new ColumnBase("", "", "", "ID"),new ColumnBase("", "", "", "NAME")};
			Result r = Result.newDataResult(meta);
			r.navigator.add(new Object[] {0,input});
			r.navigator.reset();
			result[0]=JDBCResultSet.newJDBCResultSet(r,meta);
		}
	}

	public static void failN(final String input,ResultSet[] result) throws SQLException {
		if (tries++ < 3) {
			ResultMetaData meta = ResultMetaData.newSimpleResultMetaData(new Type[]{Type.SQL_INTEGER,Type.SQL_VARCHAR});
			meta.columns= new ColumnBase[] {new ColumnBase("", "", "", "ID"),new ColumnBase("", "", "", "NAME")};
			Result r = Result.newDataResult(meta);
			r.navigator.add(new Object[] {0,input});
			r.navigator.reset();
			result[0]=JDBCResultSet.newJDBCResultSet(r,meta);
		}
	}

	public static void simpleProcOut(final String input,final String output[]) {
		output[0]=input!=null?input.toLowerCase():null;
	}

	@Override
	public Connection getConnection(String suffx) throws ClassNotFoundException, SQLException {
		return connection;
	}

	private static Result newTwoColumnResult() {
		Type[] types = new Type[2];
		types[0] = Type.SQL_INTEGER;
		types[1] = Type.SQL_VARCHAR;
		ResultMetaData meta = ResultMetaData.newSimpleResultMetaData(types);
		RowSetNavigator navigator = new RowSetNavigatorClient();
		Result result = Result.newDataResult(meta);
		result.setNavigator(navigator);
		return result;
	}

	public static ResultSet getCustomResult(String input) throws SQLException {
		Result result = newTwoColumnResult();
		Object[] row = new Object[2];
		row[0] = 0;
		row[1] = input;
		result.navigator.add(row);

		result.navigator.reset();
		return new JDBCResultSet((JDBCConnection) connection, null, result, result.metaData);
	}
}
