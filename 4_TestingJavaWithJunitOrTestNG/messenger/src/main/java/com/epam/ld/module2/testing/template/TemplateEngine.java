package com.epam.ld.module2.testing.template;


import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.ImmutableList;

public class TemplateEngine {

    private Template template;

    public TemplateEngine(Template template) {
        this.template = template;
    }

    private static final Logger log = Logger.getLogger(TemplateEngine.class.getName());

    public String generateMessage(Map<String, String> context) {
        String emailTemplate = template.getEmailTemplate();

        if (StringUtils.isBlank(emailTemplate)) {
            throw new IllegalArgumentException("IN generateMessage() - emailTemplate is Blank!)");
        }

        List<String> templateValues = template.getTemplateValues(emailTemplate);
        ImmutableList<String> immutableTemplateValues = ImmutableList.copyOf(templateValues);//

        if (isContextNotContainedTemplate(context, immutableTemplateValues)) {
            throw new IllegalArgumentException("IN generateMessage() - some values in context are missing!)");
        }

        String name = context.get("name");
        String year = context.get("year");

        String readyToSend = emailTemplate.replace("#{name}", name).replace("#{year}", year);
        return readyToSend;
    }

    private Boolean isContextNotContainedTemplate(Map<String, String> context, List<String> templateValues) {
        List<Boolean> booleans = templateValues.stream()
                .map(template ->
                {
                    Set<String> keySet = context.keySet();
                    return keySet.contains(template);
                })
                .collect(Collectors.toList());

        return booleans.contains(false);
    }

}
