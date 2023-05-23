//package com.huynhchihieu.smtp;
//
//import java.io.IOException;
//import java.util.Properties;
//import java.util.logging.LogManager;
//import java.util.logging.Logger;
//
//import javax.mail.MessagingException;
//
////https://stackoverflow.com/questions/69194560/java-spring-mail-receiver
//
//public class ReadEmailIMAPImpl extends ReadEmailBaseImpl {
//
//    private static Logger logger = LogManager.getLogger(ReadEmailIMAPImpl.class);
//
//    private static final String PROTOCOL = "imap";
//    private static final String HOST = "imap.gmail.com";
//
//    private static Properties getProperties() {
//        Properties props = new Properties();
//        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.imap.socketFactory.fallback", "false");
//        props.put("mail.imap.socketFactory.port", "993");
//        props.put("mail.imap.port", "993");
//        props.put("mail.imap.host", HOST);
//        props.put("mail.imap.user", USERNAME);
//        props.put("mail.imap.protocol", PROTOCOL);
//        return props;
//    }
//
//    public static void main(String[] args) throws Exception {
//        ReadEmailIMAPImpl readEmailIMAP = new ReadEmailIMAPImpl();
//        readEmailIMAP.process();
//    }
//
//    @Override
//    public void process() {
//        try {
//            process(PROTOCOL, HOST, getProperties());
//        } catch (MessagingException e) {
//            logger.error("Error processing emails. "+e.getMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            logger.error("Error processing emails. "+e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}