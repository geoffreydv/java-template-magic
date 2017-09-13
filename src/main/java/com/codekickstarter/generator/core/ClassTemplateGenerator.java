package com.codekickstarter.generator.core;

import com.codekickstarter.generator.api.GeneratedCode;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.*;

public class ClassTemplateGenerator {

    public ClassTemplateGenerator(String filePath) {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", filePath);
        Velocity.init(p);
    }

    public List<GeneratedCode> generate(SourceClass sourceClass, Collection<String> templateNames, Map<String, String> additionalProperties) {
        VelocityContext context = buildContext(sourceClass);
        additionalProperties.forEach(context::put);

        List<GeneratedCode> results = new ArrayList<>();

        for (String templateName : templateNames) {
            StringWriter sw = new StringWriter();
            Template template = Velocity.getTemplate(templateName);
            template.merge(context, sw);
            results.add(new GeneratedCode(templateName, sw.toString()));
        }

        return results;
    }

    private VelocityContext buildContext(SourceClass sourceClass) {
        VelocityContext context = new VelocityContext();
        context.put("sourceClass", sourceClass);
        context.put("className", sourceClass.getClassName());
        context.put("fields", sourceClass.getFields());
        return context;
    }
}
