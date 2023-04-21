package com.epam.ld.module2.testing;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.epam.ld.module2.testing.template.TemplateEngine;


public class Messenger {
    private static final String MESSAGE = "The message to address: ' %s '  ,and content : ' %s ' ,was sent.";

    private MailServer mailServer;
    private TemplateEngine templateEngine;

    public Messenger(MailServer mailServer, TemplateEngine templateEngine) {
        this.mailServer = mailServer;
        this.templateEngine = templateEngine;
    }

    public String prepareMessage(Map<String, String> context) {
        String message = templateEngine.generateMessage(context);
        String address = context.get("address");

        if (StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("IN prepareMessage() - address is Blank!)");
        }

        String readyToSentMessage = String.format(MESSAGE, address, message);

        return readyToSentMessage;
    }

}