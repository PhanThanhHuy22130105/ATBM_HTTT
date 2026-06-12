package utool;

import java.util.Date;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class JavaMailUtil {
	// Email: doducduong2004@gmail.com
	// Password: klkp pfwj hskp jhyj
	
	
	static final String from = "doducduong2004@gmail.com";
	static final String password = "klkp pfwj hskp jhyj";

	public static int sendEmail(String to, int authNum) {
		// Properties : khai báo các thuộc tính
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
		props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		// create Authenticator
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(from, password);
			}
		};

		// Phiên làm việc
		Session session = Session.getInstance(props, auth);

		// Tạo một tin nhắn
		MimeMessage msg = new MimeMessage(session);

		try {
			// Kiểu nội dung
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

			// Người gửi
			msg.setFrom(from);

			// Người nhận
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

			// Tiêu đề email
			msg.setSubject("Xác Minh tài khoản của bạn");

			// Quy đinh ngày gửi
			msg.setSentDate(new Date());

			// Nội dung
			msg.setContent("Mã xác minh của bạn là: <b>" + authNum + " <b>.", "text/HTML; charset=UTF-8");

			// Gửi email
			Transport.send(msg);
			System.out.println("Gửi email thành công");
			return authNum;
		} catch (Exception e) {
			System.out.println("Gặp lỗi trong quá trình gửi email");
			e.printStackTrace();
			return -1;
		}
	}
	
	
	
	

	public static void main(String[] args) {
			int num = JavaMailUtil.sendEmail("doducduong2004@gmail.com",1102);
			System.out.println( num );
	}

}