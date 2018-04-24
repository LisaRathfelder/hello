package com.oencue.server;

import com.oencue.client.noteMapper;
import com.oencue.shared.FieldVerifier;
import com.oencue.shared.LoginInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class noteMapperImpl  extends RemoteServiceServlet implements noteMapper {

	private static final long serialVersionUID = 1L;
	private int id,maxid;
	private String name;
		
	@Override
	public String access2Database(String input, int cmd) throws IllegalArgumentException {
		// Verify that the input is valid. 
		
		Connection con = DBConnection.connection();
		 String result ="";
		  try {
		    	//Leeres SQL Statement anlegen
		      Statement stmt = con.createStatement();
		    //Statement ausfuellen und als Query an DB schicken
		      ResultSet rs = stmt.executeQuery("SELECT MAX(idusers) AS idusers FROM users");

					      
		      if (rs.next()) {
		    	//Ergebnis-Tupel in Objekt umwandeln
		    	  maxid=rs.getInt("idusers") + 1;
		    	  stmt = con.createStatement();
		    	

		    	  stmt.executeUpdate("INSERT INTO notes.users(idusers,username) VALUES ("+this.maxid +",\"user"+maxid+"\")");
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		 
		 
		 try{
			 Statement stmt = con.createStatement();
			 ResultSet rs = stmt.executeQuery("select * from users  where idusers is not NULL"); 
			
			 while (rs.next()){
				result +=Integer.toString(rs.getInt("idusers"))+ "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;"+rs.getString("username")+"<br>"; 
				 
			 }
		 }
		 catch (SQLException e){
			 e.printStackTrace();
			 return null;
		 }

			 
			 
		
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Accessing Database<br>Text Parameter = " + input + "<br>Command = " + cmd + 
				"<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent+ "<br><br><br> USERID &emsp;&emsp;&emsp;&emsp;USERNAME<br> " +  result + "<br> MAXID:" +this.maxid;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
