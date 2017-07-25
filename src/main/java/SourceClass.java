import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.MemberValuePair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SourceClass {

    private String parentClass;
    private String className;
    private String packageName;
    private List<Field> fields;
    private String superClassName;

    public SourceClass(String packageName, String className, List<Field> fields) {
        this.className = className;
        this.packageName = packageName;
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "SourceClass{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", fields=" + fields +
                '}';
    }

    public String getFullyQualifiedClassName() {
        return packageName + "." + className;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getClassName() {
        return className;
    }

    public String getClassNameLower() {
        return className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public static SourceClass fromJavaFile(CompilationUnit compilationUnit) {

        Optional<ClassOrInterfaceDeclaration> classDefinition = compilationUnit.getChildNodesByType(ClassOrInterfaceDeclaration.class)
                .stream()
                .findFirst();

        if (classDefinition.isPresent()) {

            List<Field> fieldNames = compilationUnit.getChildNodesByType(FieldDeclaration.class).stream()
                    .map(f -> {
                                Field field = new Field(
                                        f.getVariables().get(0).getName().toString(),
                                        f.getVariables().get(0).getType().toString());

                                // Try to find a hibernate not null annotation
                                Optional<AnnotationExpr> columnAnnotation = f.getAnnotationByName("Column");
                                if (columnAnnotation.isPresent()) {
                                    for (MemberValuePair attribute : columnAnnotation.get().getChildNodesByType(MemberValuePair.class)) {
                                        if (attribute.getName().getIdentifier().equals("nullable")) {
                                            field.setMandatory(!((BooleanLiteralExpr) attribute.getValue()).getValue());
                                        }
                                    }
                                }

                                return field;
                            }
                    )
                    .collect(Collectors.toList());

            String packageName = compilationUnit.getPackageDeclaration().get().getName().toString();
            String className = classDefinition.get().getName().toString();

            SourceClass sourceClass = new SourceClass(packageName, className, fieldNames);

            if (!classDefinition.get().getExtendedTypes().isEmpty()) {
                String superClassName = classDefinition.get().getExtendedTypes().get(0).getName().getIdentifier();
                sourceClass.setSuperClassName(superClassName);
            }

            return sourceClass;

        } else {
            return null;
        }
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }

    public String getSuperClassName() {
        return superClassName;
    }
}
