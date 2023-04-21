package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.service.*;
import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import com.google.common.collect.ImmutableList;


public class Application {

    public static void main(String[] args) {
        FileService fileService = new FileServiceImpl();
        MessageService messageService = new MessageServiceImpl(fileService);
        HandleArgumentService handleArgumentService = new HandleArgumentsServiceImpl();

        Template template = new Template();
        MailServer mailServer = new MailServer();
        TemplateEngine templateEngine = new TemplateEngine(template);
        Messenger messenger = new Messenger(mailServer, templateEngine);

        ImmutableList<String> immutableArguments = handleArgumentService.handleArrayArguments(args);
        String readyToSend = messageService.handleMessage(immutableArguments, messenger);
        mailServer.send(readyToSend);

    }
}
