import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Properties;

class ClassTemplateGenerator {

    ClassTemplateGenerator(String filePath) {
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", filePath);
        Velocity.init(p);
    }

    String generate(SourceClass sourceClass, Collection<String> templateNames) {
        VelocityContext context = buildContext(sourceClass);

        StringWriter sw = new StringWriter();
        for (String templateName : templateNames) {
            sw.write("Results for template: '"+ templateName +"'\n\n");
            Template template = Velocity.getTemplate(templateName);
            template.merge(context, sw);
            sw.write("\n\n");
        }

        return sw.toString();
    }

    private VelocityContext buildContext(SourceClass sourceClass) {
        VelocityContext context = new VelocityContext();
        context.put("sourceClass", sourceClass);
        context.put("className", sourceClass.getClassName());
        context.put("fields", sourceClass.getFields());
        return context;
    }
}
