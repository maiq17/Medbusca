package upa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDestino {
    public static Connection ConnectDb() {
    	  try {
	           

	            String url = "jdbc:mysql://localhost:3306/banco_destino";
	            String username = "root";
	            String password = "";
	            Connection conn = DriverManager.getConnection(url, username, password);
	            
	            
				return conn;
	            
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
