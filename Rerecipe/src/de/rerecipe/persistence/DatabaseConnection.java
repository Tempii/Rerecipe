package de.rerecipe.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection implements AutoCloseable {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String hostname = "ems.informatik.uni-oldenburg.de";
	private static final int PORT = 55000;
	private static final String dbname = "it15g05";
	private static final String url = "jdbc:mysql://" + hostname + ":" + PORT
			+ "/" + dbname;
	private static final String user = "it15g05";
	private static final String password = "hwtvqq6";

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection connection;
	private PreparedStatement preparedStatement;

	public DatabaseConnection(String statement) {
		try {
			connection = DriverManager.getConnection(url, user, password);
			preparedStatement = connection.prepareStatement(statement);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConnectionNotCreatedException();
		}
	}

	public PreparedStatement getStatement() {
		return preparedStatement;
	}

	public boolean disconnect() {
		try {
			if (connection != null)
				connection.close();
			if (preparedStatement != null)
				preparedStatement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() {
		disconnect();
	}

}
