package com.th3.appms.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.th3.appms.domain.NotificationContent;
import com.th3.appms.notification.NotificationSendMail;
import com.th3.appms.notification.SendNotificationInterface;
import com.th3.appms.repository.ImeiRepository;

@Service
public class NotificationSend {
	@Autowired
	private ImeiRepository ImeiRep;
	@Autowired
	private JavaMailSender JavaM;

	public void send(NotificationContent notifiationContent) {
		// TODO Auto-generated method stub
		SendNotificationInterface noti = null;
		switch (notifiationContent.getTarget()) {
		case "email":
			noti = new NotificationSendMail(ImeiRep, JavaM, notifiationContent);
		default :
			break;
		}
		noti.send();
	}
}
