package test.temporary;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.softwaresecurity.group5.mail.EmailService;

public class generateEmailTest {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"/src/main/webapp/resources/Spring-Mail.xml");
		EmailService mm = (EmailService) context.getBean("email");
		mm.sendMail("group05.sbs@gmail.com", "group05.sbs@gmail.com",
				"Love SQL", "try11122L");

	}
}
