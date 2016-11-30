package com.th3.appms.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.th3.appms.domain.Imei;
import com.th3.appms.domain.NotificationContent;
import com.th3.appms.repository.ImeiRepository;

@Service
public class NotificationSendMail implements SendNotificationInterface {

	private NotificationContent notificationContent;
	
	private ImeiRepository imeiRep;
	
	private JavaMailSender javaMailService;
	
	@Autowired
	public NotificationSendMail(ImeiRepository imeiR, JavaMailSender javaMai, NotificationContent noti) {
		// TODO Auto-generated constructor stub
		notificationContent = noti;
		imeiRep = imeiR;
		javaMailService = javaMai;
	}
	
	@Override
	public void send() {
		
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		Imei imei = imeiRep.findOneWithImei(notificationContent.getImei());
		mailMessage.setTo(imei.getEmail());
		mailMessage.setSubject("Registration");
		mailMessage.setText("Hello " + notificationContent.getDescription() +"\n Your registration is successfull");
		javaMailService.send(mailMessage);
		
	}
}
