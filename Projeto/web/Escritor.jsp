<%-- 
    Document   : Escritor
    Created on : Apr 26, 2014, 6:16:47 PM
    Author     : rodrigo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page language="java" import ="BancoDeDados.*" %>  
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Escritor</title>
    </head>
    <body>
        <h1>Por favor, preencha os dados abaixo</h1>
        <br>
        <form method="post" action="TratadorDeEnvio">
            
            <select name="cadastrado">
                <%  
                    Cadastrado[] cadastrados = Cadastrados.getCadastrados_IDUsuario(1);
                    for(int i = 0; i < cadastrados.length; ++i)
                    {
                %>
                <option value=<%cadastrados[i].getID();%>> 
                    <!-- Aqui é preciso somar o URI do cadastrado com seu respectivo dominio !-->
                    <%
                        String dominio = Servidores.getServidor(cadastrados[i].getID_Servidor()).getDominio();
                        out.print(cadastrados[i].getURI()+dominio);
                    %> 
                </option>
                <%  
                        
                    }
                %>
            </select>
            <br>
            <input type="text" placeholder="Destinatario@email.com; Dest2@email.com" name="to">
            <br>
            <input type="text" placeholder="Assunto" name="assunto">
            <br>
            <textarea rows="4" cols="50" placeholder="Mensagem" name="msg">
            </textarea>
            <br>
            <input type="file" placeholder="Anexo" name="anexo1"><br>
            <input type="file" placeholder="Anexo" name="anexo2"><br>
            <input type="file" placeholder="Anexo" name="anexo3"><br>
            <br>
            <input type="submit">
        </form>
    </body>
</html>
