package com.epam.ld.module2.testing.service;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandleArgumentsServiceImpl implements HandleArgumentService {


    @Override
    public ImmutableList<String> handleArrayArguments(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("IN handleArrayArguments() -  arguments are absent!)");
        }

        List<String> strings = Arrays.stream(args).collect(Collectors.toList());

        ImmutableList<String> immutableStrings = ImmutableList.copyOf(strings);

        return immutableStrings;
    }
}
