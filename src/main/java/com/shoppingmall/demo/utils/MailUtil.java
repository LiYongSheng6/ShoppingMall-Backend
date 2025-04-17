package com.shoppingmall.demo.utils;

import com.shoppingmall.demo.config.EmailConfigProperties;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮箱发送工具类
 * @author RedmiK50Ultra
 * @date 2024/4/30 14:54
 **/
@Slf4j
public class MailUtil {
    /**
     * 发送自定义邮件信息
     * @param emailConfigProperties 发件人信息(发件人邮箱，发件人授权码)及邮件服务器信息(邮件服务器名称，身份验证开关)
     * @param to 收件人邮箱
     * @param title 邮件标题
     * @param content 邮件正文
     * @return
     */
    public static Boolean sendMail(EmailConfigProperties emailConfigProperties, String to, String title, String content) {
        Properties prop = new Properties();
        // 开启debug调试，可在控制台查看
        prop.setProperty("mail.debug", "false");
        // 设置邮件服务器主机名(域名)
        prop.setProperty("mail.host", emailConfigProperties.host);
        // 发送服务器需要身份验证
        prop.setProperty("mail.smtp.auth", emailConfigProperties.auth);
        // 发送邮件协议名称
        prop.setProperty("mail.transport.protocol", "smtp");
        // 开启SSL加密可能会失败
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "false");
            prop.put("mail.smtp.ssl.socketFactory", sf);
            // 创建session
            Session session = Session.getInstance(prop);
            // 通过session得到transport对象
            Transport ts = session.getTransport();
            // 连接邮件服务器：邮箱对应服务器域名，发件人邮箱帐号，发件人邮箱授权码（POP3/SMTP协议授权码 163使用：smtp.163.com，qq使用：smtp.qq.com）
            ts.connect(emailConfigProperties.host, emailConfigProperties.user, emailConfigProperties.code);
            // 创建邮件
            Message message = createSimpleMail(emailConfigProperties.user, session, to, title, content);
            // 发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * @Method: createSimpleMail
     * @Description: 创建一封只包含文本的邮件
     */
    private static MimeMessage createSimpleMail(String user, Session session, String toUserEmail, String title, String content) throws Exception {
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明邮件的发件人
        message.setFrom(new InternetAddress(user));
        // 指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toUserEmail));
        // 邮件的标题
        message.setSubject(title);
        // 邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        // 返回创建好的邮件对象
        return message;
    }
}
