/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BancoDeDados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author rodrigo
 */
public class Conector {
    
    private static Connection _conn = null;
    private static PreparedStatement stmt = null;
    
    private static String _JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String _DB_URL = "jdbc:mysql://localhost";

    private static String _USER = "root";
    private static String _PASS = "informatica2012";
    
    
    /**
     * 
     * @param query (String) Query que será utilizada para a criação de um Pre-
     * paredStatment.
     * Ex: INSERT INTO Tabela VALUES(?, ?)
     * 
     * <Strong>
     * Atenção: Esse comando deve ser precedido do uso d comando ConectarBD e
     * sucedido do comando desconectarBD
     * </Strong>
     * @return Retorna o preparedStatement gerado
     * @throws SQLException 
     */
    public static PreparedStatement getPreparedStatment(String query) throws SQLException
    {
        stmt = _conn.prepareStatement(query);
        return stmt;
    }
    /**
     * Esse metodo conecta-se ao driver do banco de dados com a credenciais pas-
     * sadas
     *
     * @throws SQLException - Exception lancada caso nao consiga acesso ao banco
     * de dados.
     */
    public static void conectarBD() throws SQLException, ClassNotFoundException {
        Class.forName(_JDBC_DRIVER); // registrando o banco
        _conn = DriverManager.getConnection(_DB_URL, _USER, _PASS);
        _conn.setAutoCommit(false); // evita de enviar automaticamente o stmt

    }

    /**
     * Metodo responsavel por fechar anteriomente estabelicida com o banco de
     * dados
     *
     * @throws SQLException - Exception lancada caso nao consiga acesso ao banco
     * de dados
     */
    public static void desconectarBD() {
        try {
            stmt.close();
            _conn.close();
        } catch (Exception e) {
        }
        _conn = null;
        stmt = null;
    }
    
    public static void alterarCredenciais(String jdbcDriver, String dbUrl, String user, String pass)
    {
        _JDBC_DRIVER = jdbcDriver;
        _DB_URL = dbUrl;
        _USER = user;
        _PASS = pass;
    }
}
