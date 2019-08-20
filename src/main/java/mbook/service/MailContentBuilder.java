package mbook.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {
    private TemplateEngine templateEngine;
    
    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
 
    public String build(String template, Map<String, String> templateVariables) {
        Context context = new Context();
        for (Map.Entry<String, String> entry : templateVariables.entrySet() ) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(template, context);
    }
}
