package com.epam.ld.module2.testing.template;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {

    private static final String PUNCTUATION_PATTERN = "\\p{P}";
    private static final String TEMPLATE_PATTERN = "#\\{[\\w\\d\\s-]+}";
    private String emailTemplate = "Dear msr. #{name}, Best wishes in terms of #{year} New Year !";


    public String getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    protected List<String> getTemplateValues(String emailTemplate) {
        List<String> matchers = new ArrayList<>();

        Pattern pattern = Pattern.compile(TEMPLATE_PATTERN);
        Matcher matcher = pattern.matcher(emailTemplate);
        while (matcher.find()) {
            String group = matcher.group();
            String clearValue = group.replaceAll(PUNCTUATION_PATTERN, "");
            matchers.add(clearValue);
        }
        return matchers;

    }

}
