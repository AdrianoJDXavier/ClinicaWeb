package br.com.asfecer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class ConnectionFactory {
    private static final String URL ="jdbc:mysql://localhost:3306/db_asfecer";
    private static final String DRIVER ="com.mysql.jdbc.Driver"; 
    private static final String USER ="root"; 
    private static final String PASSWORD ="root"; 
    private static Connection conexao = null;
    
    public static Connection createConnection() throws Exception{
        if(conexao == null){
            Class.forName(DRIVER);
            conexao = DriverManager.getConnection(URL, USER, PASSWORD);
        }

        return conexao;
    }
    
    public void desconectar() throws Exception{

        conexao.close();

    }

}