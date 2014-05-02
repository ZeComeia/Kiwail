/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EscreverEmail;

import BancoDeDados.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author rodrigo
 */
@WebServlet(name = "TratadorDeEnvio", urlPatterns =
{
    "/TratadorDeEnvio"
})
public class TratadorDeEnvio extends HttpServlet
{

    private int porta;
    private String filePath = "src/";
    private File file;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;

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
            throws ServletException, IOException
    {
        // Informações do formulário
        String msg = "";
        String assunto = "";
        String to = "";
        int ID_Cadastrado = 0;
       
        //O formulário foi enviado com o cabeçalho de multpart, logo todos os campos do mesmo
        //agora são tratados dessa forma, tornando impossível o uso do simples método
        //request.getParameter
        try
        {
            // Vetor de anexos
            EmailAttachment[] anexos = new EmailAttachment[3];
            int contador = 0;

            //Criando o arquivo temporariamente no sistema
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            try
            {
                // Parse the request to get file items.
                List fileItems = upload.parseRequest(request);

                // Process the uploaded file items
                Iterator i = fileItems.iterator();
                
                //Pegando os valores dos outros campos do formulário
                Scanner sc = null; // obj de leitura do value dos campos
                FileItem fi = null; // Obj que representa o campo
                // 1º: email escolhido
                fi = (FileItem) i.next();
                sc = new Scanner(fi.getInputStream());
                ID_Cadastrado = Integer.parseInt(sc.nextLine()); // retorna o value do campo
                // 2º: To (Destinatarios)
                fi = (FileItem) i.next();
                sc = new Scanner(fi.getInputStream());
                to = sc.nextLine(); // retorna o value do campo
                // 3º: Assunto
                fi = (FileItem) i.next();
                sc = new Scanner(fi.getInputStream());
                assunto = sc.nextLine(); // retorna o value do campo
                // 4º: Mensagem
                fi = (FileItem) i.next();
                sc = new Scanner(fi.getInputStream());
                msg = sc.nextLine(); // retorna o value do campo
                
                // Tratando os possíveis anexos
                while (i.hasNext())
                {
                    fi = (FileItem) i.next();
                    if (!fi.isFormField())
                    {
                        // Get the uploaded file parameters
                        String fieldName = fi.getFieldName();
                        String fileName = fi.getName();
                        
                        if(!fileName.equals("")) // anexo contem um arquivo
                        {
                            // calculando o caminho
                            String caminhos = request.getServletContext().getRealPath("src/"+fileName);
                            file = new File(caminhos.replace("/build", ""));

                            //File escrito
                            fi.write(file);

                            //Criando Objeto de anexo com o arquivo
                            EmailAttachment anexoAtual = new EmailAttachment();
                            anexoAtual.setPath(file.getPath());
                            anexos[contador] = anexoAtual;
                            ++contador;
                        }
                    }
                }
            } catch (Exception ex)
            {
                System.out.println(ex);
            }


            
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
            try
            {
                escreverEmailSimples(servidor.getUri_SMTP(), msg, user, pass, assunto,
                        reparteDestinatarios(to), servidor.getPorta_SMTP(), anexos);
            } catch (EmailException ex)
            {
                //Nao foi possivel enviar
            }
            limpaTemporario(request); // limpa os arquivos temporarios de anexo

        } catch (SQLException ex)
        {
            Logger.getLogger(TratadorDeEnvio.class.getName()).log(Level.SEVERE, null, ex);
            //Nao foi possivel enviar
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(TratadorDeEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ////////////////////////////////////////////////////////////////////////////
/////////////////////ESCRITA DE EMAILS///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////
    private void escreverEmailSimples(String servEmail, String msg, String user,
            String pass, String assunto, String[] to, int porta, EmailAttachment[] anexos) throws EmailException
    {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(servEmail);
        email.setSmtpPort(porta);
        email.setAuthentication(user, pass);
        email.setSSLOnConnect(true);
        email.setSubject(assunto);
        email.setFrom(user);

        //Adicionando os destinatários
        for (int i = 0; i < to.length; ++i)
        {
            email.addTo(to[i]);
        }

        //Adicionando os anexos
        for (int i = 0; i < anexos.length; ++i)
        {
            if(anexos[i] != null)
            {
                email.attach(anexos[i]);
            }
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
    /**
     * Separa a string de destinatarios que tem como separado o caracter ';'
     *
     * @param to
     * @return
     */
    private String[] reparteDestinatarios(String to)
    {
        String[] retorno = new String[1];
        retorno[0] = to;
        return retorno;
    }

    
    /**
     * Método para limpar a pasta src temporário.
     * @param request 
     */
    private void limpaTemporario(HttpServletRequest request)
    {
        String caminhos = request.getServletContext().getRealPath("src/");
        file = new File(caminhos.replace("/build", ""));
        try
        {
            FileUtils.cleanDirectory(file);
        } catch (IOException ex)
        {
            Logger.getLogger(TratadorDeEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            throws ServletException, IOException
    {
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
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
