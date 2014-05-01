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
public class Servidor {
    private int    _ID;
    private String _dominio;
    private String _uri_SMTP;
    private int    _porta_SMTP;
    private String _uri_IMap;
    private int    _porta_IMap;

    public Servidor(int ID, String dominio, String uri_SMTP, int porta_SMTP, String uri_IMap, int porta_IMap) {
        this._ID = ID;
        this._dominio = dominio;
        this._uri_SMTP = uri_SMTP;
        this._porta_SMTP = porta_SMTP;
        this._uri_IMap = uri_IMap;
        this._porta_IMap = porta_IMap;
    }

    public int getID() {
        return _ID;
    }

    public void setID(int ID) {
        this._ID = _ID;
    }

    public String getDominio() {
        return _dominio;
    }

    public void setDominio(String dominio) {
        this._dominio = _dominio;
    }

    public String getUri_SMTP() {
        return _uri_SMTP;
    }

    public void setUri_SMTP(String uri_SMTP) {
        this._uri_SMTP = _uri_SMTP;
    }

    public int getPorta_SMTP() {
        return _porta_SMTP;
    }

    public void setPorta_SMTP(int porta_SMTP) {
        this._porta_SMTP = _porta_SMTP;
    }

    public String getUri_IMap() {
        return _uri_IMap;
    }

    public void setUri_IMap(String uri_IMap) {
        this._uri_IMap = _uri_IMap;
    }

    public int getPorta_IMap() {
        return _porta_IMap;
    }

    public void setPorta_IMap(int porta_IMap) {
        this._porta_IMap = _porta_IMap;
    }
    
    
}
