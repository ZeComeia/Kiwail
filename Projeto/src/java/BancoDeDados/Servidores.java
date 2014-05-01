/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigo
 */
public class Servidores {

    //Statements
    private static String _StringSTMT_GetServidores
            = "select * from Email.Servidor";
    private static String _StringSTMT_GetServidor
            = "select * from Email.Servidor where ID = ?";

    public static Servidor getServidor(int ID) {
        Servidor retorno = null;
        ResultSet result = null;
        try {
            Conector.conectarBD();
            PreparedStatement stmt = Conector.getPreparedStatment(_StringSTMT_GetServidor);
            stmt.setInt(1, ID);
            result  = stmt.executeQuery();
            result.next();
            retorno = new Servidor(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getString(5),
                        result.getInt(6));
            Conector.desconectarBD();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    public static Servidor[] getServidores() {
        Servidor[] retorno = null;
        ResultSet result = null;
        try {
            Conector.conectarBD();
            PreparedStatement stmt = Conector.getPreparedStatment(_StringSTMT_GetServidores);
            result = stmt.executeQuery();

            //Descobrindo quantos Registros
            result.last();
            int totalRows = result.getRow();
            result.beforeFirst();

            retorno = new Servidor[totalRows];
            //Poem os resultados no vetor
            for (int i = 0; i < totalRows; ++i) {
                result.next();
                Servidor servidorAtual = new Servidor(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getString(5),
                        result.getInt(6));

                retorno[i] = servidorAtual;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

}
