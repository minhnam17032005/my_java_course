package vn.hoidanit.jobhunter.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.SubscriberService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService,
                    SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService=subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    // @Scheduled(cron = "*/30 * * * * *") //every 30 seconds
    // @Transactional
    public String sendSimpleEmail() {
        //this.emailService.sendSimpleEmail();
        //this.emailService.sendEmailSync("namcoder2005@gmail.com",
        //        "test send email","<h1> <b1> hello </b> </h1>",false,true);
        //this.emailService.sendEmailFromTemplateSync("namcoder2005@gmail.com",
        //        "test send email","job");

        this.subscriberService.sendSubscribersEmailJobs();     
        return "ok";
    }
}

