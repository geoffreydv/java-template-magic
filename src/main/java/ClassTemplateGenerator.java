import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Properties;

public class ClassTemplateGenerator {

    public ClassTemplateGenerator() {
    }

    public String generate(SourceClass sourceClass, String filePath, String templateName) {

        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", filePath);

        Velocity.init(p);

        Template template = Velocity.getTemplate(templateName);

        VelocityContext context = new VelocityContext();
        context.put("sourceClass", sourceClass);
        context.put("className", sourceClass.getClassName());
        context.put("fields", sourceClass.getFields());

        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        return sw.toString();
    }

}
