/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.utilerias;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author starl
 */
public class Utilerias {

    public void enviarEmail(String correoDestinatario, String asunto, String texto) {

        try {
            Properties p = new Properties();
            p.setProperty("mail.smtp.host", "smtp.gmail.com");
            p.setProperty("mail.smtp.starttls.enable", "true");
            p.setProperty("mail.smtp.starttls.required", "true");
            p.setProperty("mail.smtp.port", "587");
            p.setProperty("mail.smtp.user", "postlets.escom@gmail.com");
            p.setProperty("mail.smtp.auth", "true");
            Session s = Session.getDefaultInstance(p);
            MimeMessage mensaje = new MimeMessage(s);
            mensaje.setFrom(new InternetAddress("virtual@school.com"));
            mensaje.addRecipients(Message.RecipientType.TO, correoDestinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(texto);
            Transport t = s.getTransport("smtp");
            t.connect("postlets.escom@gmail.com", "babyyoda66");
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            t.close();
        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {
        Utilerias u = new Utilerias();
        String cd = "starly_balli@hotmail.com";
        String asunto = "prueba";
        String texto = " Ya ponte a trabajar";
        u.enviarEmail(cd, asunto, texto);

    }

}
