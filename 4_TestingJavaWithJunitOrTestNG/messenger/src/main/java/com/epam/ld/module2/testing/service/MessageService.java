package com.epam.ld.module2.testing.service;

import com.epam.ld.module2.testing.Messenger;
import com.google.common.collect.ImmutableList;

public interface MessageService {

    String handleMessage(ImmutableList<String> fileNames, Messenger messenger);
}
