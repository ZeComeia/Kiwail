/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BancoDeDados;

/**
 *
 * @author rodrigo
 */
public class Cadastrado {
    private int _ID;
    private int _ID_Servidor;
    private int _ID_Usuario;
    private String _URI;
    private String _senha;

    public Cadastrado(int ID, int ID_Servidor, int ID_Usuario, String URI, String senha) {
        this._ID = ID;
        this._ID_Servidor = ID_Servidor;
        this._ID_Usuario = ID_Usuario;
        this._URI = URI;
        this._senha = senha;
    }

    public int getID() {
        return _ID;
    }

    public void setID(int ID) {
        this._ID = ID;
    }

    public int getID_Servidor() {
        return _ID_Servidor;
    }

    public void setID_Servidor(int ID_Servidor) {
        this._ID_Servidor = ID_Servidor;
    }

    public String getURI() {
        return _URI;
    }

    public void setURI(String URI) {
        this._URI = URI;
    }

    public String getSenha() {
        return _senha;
    }

    public void setSenha(String _senha) {
        this._senha = _senha;
    }
    
    
}
