import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        SubjectKnowledgeBase knowledgeBase = buildKnowledgeBase();
        generateTemplatesForClass(knowledgeBase, "OffloadedCalculationResult");
    }

    private static void generateTemplatesForClass(SubjectKnowledgeBase knowledgeBase, String className) {
        ClassTemplateGenerator generator = new ClassTemplateGenerator();

        List<String> subjectNames = new ArrayList<>();
        subjectNames.add(className);

        subjectNames.forEach(sn -> {
            SourceClass result = knowledgeBase.findClass(sn);
            if (result != null) {
                String output = generator.generate(result, "C:\\generator\\templates", "migration_classes.vm");
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
