/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDeDados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author rodrigo
 */
public class Cadastrados {

    private static String _StringSTMT_getCadastados
            = "select * from Email.Cadastrado where ID_Usuario = ?";
    private static String _StringSTMT_getCadastado
            = "select * from Email.Cadastrado where ID = ?";

    /**
     * Método responsável por devolver o registro associado ao ID passado como 
     * parametro
     * @param ID
     * @return 
     */
    public static Cadastrado getCadastrado(int ID) {
        Cadastrado retorno = null;
        ResultSet result = null;
        try {
            Conector.conectarBD();
            PreparedStatement stmt = Conector.getPreparedStatment(_StringSTMT_getCadastado);
            stmt.setInt(1, ID);
            result = stmt.executeQuery();
            result.next();
            retorno = new Cadastrado(result.getInt(1),
                    result.getInt(2),
                    result.getInt(3),
                    result.getString(4),
                    result.getString(5));
            Conector.desconectarBD();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    /**
     * Descobre todos os emails cadastrados por um determinado usuario
     *
     * @param ID_Usuario
     * @return
     */
    public static Cadastrado[] getCadastrados_IDUsuario(int ID_Usuario) {
        Cadastrado[] retorno = null;
        ResultSet result = null;
        try {
            Conector.conectarBD();
            PreparedStatement stmt = Conector.getPreparedStatment(_StringSTMT_getCadastados);
            stmt.setInt(1, ID_Usuario);
            result = stmt.executeQuery();

            //Descobrindo quantos Registros
            result.last();
            int totalRows = result.getRow();
            result.beforeFirst();

            retorno = new Cadastrado[totalRows];
            //Poem os resultados no vetor
            for (int i = 0; i < totalRows; ++i) {
                result.next();
                Cadastrado cadastradoAtual = new Cadastrado(result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getString(4),
                        result.getString(5));

                retorno[i] = cadastradoAtual;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return retorno;

    }
}
