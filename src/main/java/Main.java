import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        SubjectKnowledgeBase knowledgeBase = buildKnowledgeBase();

        List<String> templates = new ArrayList<>();
        templates.add("ext-model-and-store.vm");
        templates.add("DTO.vm");

        generateTemplatesForClass(knowledgeBase,
                "FQA",
                templates);
    }

    private static void generateTemplatesForClass(SubjectKnowledgeBase knowledgeBase,
                                                  String className,
                                                  List<String> templateNames) {

        List<String> subjectNames = new ArrayList<>();
        subjectNames.add(className);

        ClassTemplateGenerator generator = new ClassTemplateGenerator("C:\\generator\\templates\\credo");
        subjectNames.forEach(sn -> {
            SourceClass sourceClass = knowledgeBase.findClass(sn);
            if (sourceClass != null) {
                String output = generator.generate(sourceClass, templateNames);
                System.out.println(output);
            } else {
                System.out.println("Class not found");
            }
        });
    }

    private static SubjectKnowledgeBase buildKnowledgeBase() throws IOException {
        SubjectKnowledgeBase knowledgeBase = new SubjectKnowledgeBase();

        List<String> sourceDirectories = new ArrayList<>();
        sourceDirectories.add("C:\\projects\\roots\\ifrs9\\core\\src\\main\\java\\be\\credo\\ifrs9\\core\\domain");
        knowledgeBase.build(sourceDirectories);
        return knowledgeBase;
    }
}
