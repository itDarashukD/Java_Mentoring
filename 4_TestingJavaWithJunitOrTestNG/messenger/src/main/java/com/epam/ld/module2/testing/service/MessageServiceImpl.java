package com.epam.ld.module2.testing.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import com.epam.ld.module2.testing.Messenger;
import com.google.common.collect.ImmutableList;


public class MessageServiceImpl implements MessageService {

    private static final Logger log = (Logger) LoggerFactory.getLogger(MessageServiceImpl.class);
    private static final String FILE_MODE_COMMAND_FLAG = "--file";
    private static final Character COMMAND_ARGUMENTS_DELIMITER = '=';
    private static final Character REQUIRED_NUMBER_OF_ARGUMENTS_FOR_FILES = 2;
    private FileService fileService;


    public MessageServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public String handleMessage(ImmutableList<String> args, Messenger messenger) {

        return isFileMode(args) ? handleFileOutput(args, messenger) : handleConsoleOutput(args, messenger);
    }

    private Boolean isFileMode(ImmutableList<String> args) {
        return args.get(0).contains(FILE_MODE_COMMAND_FLAG);
    }

    private String handleFileOutput(ImmutableList<String> args, Messenger messenger) {
        List<String> argsList = Lists.newArrayList(args);
        argsList.remove(0);

        ImmutableList<String> immutableStrings = ImmutableList.copyOf(argsList);

        Map<String, String> fileNames = prepareContext(immutableStrings);
        if (fileNames.size() < REQUIRED_NUMBER_OF_ARGUMENTS_FOR_FILES) {
            throw new IllegalArgumentException("IN handleFileOutput() - not enough arguments!)");
        }

        List<String> rawContext = fileService.readFile(fileNames);
        ImmutableList<String> immutableRawContext = ImmutableList.copyOf(rawContext);

        String sentMessage = createMessage(immutableRawContext, messenger);

        fileService.writeToFile(sentMessage, fileNames);

        return sentMessage;
    }

    private String handleConsoleOutput(ImmutableList<String > args, Messenger messenger) {
        log.info("IN handleConsoleOutput() - start working ...");

        return createMessage(args, messenger);//
    }

    private String createMessage(List<String> rawContext, Messenger messenger) {
        Map<String, String> context = prepareContext(rawContext);
        return messenger.prepareMessage(context);
    }

    private Map<String, String> prepareContext(List<String> args) {
        log.info("IN prepareContext() - start working with " + args);

        Map<String, String> context = new HashMap<>();

        args.forEach(arg -> {
            int index = arg.indexOf(COMMAND_ARGUMENTS_DELIMITER);
            String key = arg.substring(0, index);
            String value = arg.substring(index + 1);

            if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                throw new IllegalArgumentException("IN prepareContext() - some of the arguments in context is blank!)");
            }
            context.put(key, value);
        });
        return context;
    }

}
