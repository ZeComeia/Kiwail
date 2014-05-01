/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EscreverEmail;

import BancoDeDados.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author rodrigo
 */
@WebServlet(name = "TratadorDeEnvio", urlPatterns = {"/TratadorDeEnvio"})
public class TratadorDeEnvio extends HttpServlet {

    private int porta;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Reavendo os valores passados no formulário
            
            //Informações de Email para envio
            String msg = request.getParameter("msg");
            String assunto = request.getParameter("assunto");
            String to = request.getParameter("to");;
            
            //Informações de Email escolhido
            int ID_Cadastrado = Integer.parseInt(request.getParameter("servidor")); // retorna o ID do email escolhido
            Conector.conectarBD();
            Cadastrado cadastrado = Cadastrados.getCadastrado(ID_Cadastrado);
            Conector.desconectarBD();
            String user = cadastrado.getURI(); // Apena a URI sem o dominio
            String pass = cadastrado.getSenha();
            
            // Descobrindo as informações do servidor usado
            Conector.conectarBD();
            Servidor servidor = Servidores.getServidor(cadastrado.getID_Servidor());
            Conector.desconectarBD();
            user += servidor.getDominio(); // adicionado o dominio a URI
            //Efetivamente eviando o email
            try {
                escreverEmailSimples(servidor.getUri_SMTP(), msg, user, pass, assunto,
                        reparteDestinatarios(to), 465);
            } catch (EmailException ex) {
                //Nao foi possivel enviar
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TratadorDeEnvio.class.getName()).log(Level.SEVERE, null, ex);
            //Nao foi possivel enviar
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratadorDeEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    /////////////////////ESCRITA E EMAILS///////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    private void escreverEmailSimples(String servEmail, String msg, String user,
            String pass, String assunto, String[] to, int porta) throws EmailException {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(servEmail);
        email.setSmtpPort(porta);
        email.setAuthentication(user, pass);
        email.setSSLOnConnect(true);
        email.setSubject(assunto);
        email.setFrom(user);

        for (int i = 0; i < to.length; ++i) {
            email.addTo(to[i]);
        }

        // Compoe a mensagem HTML
        email.setHtmlMsg(msg);
        // Especifica um texto a ser mostrada no caso
        // do leitor de email nao suportar mensagens em HTML
        email.setTextMsg(msg);
        // Envia o email
        email.send();

    }

    ///////////////////////////////////////////////////////////////////////////
    /////////////////////MÉTODOS AUXILIARES///////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private String[] reparteDestinatarios(String to) {
        String[] retorno = new String[1];
        retorno[0] = to;
        return retorno;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
