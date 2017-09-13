$(document).ready(function () {

    var $template = $("#template-selection");
    var $entityClass = $("#entity-class");

    $.get("http://localhost:8080/list-templates", {}, function (templateNames) {
        for (var i = 0; i < templateNames.length; i++) {
            $template.append($("<option value='" + templateNames[i] + "'>" + templateNames[i] + "</option>"));
        }
    });

    $.get("http://localhost:8080/list-known-classes", {}, function (classNames) {
        $entityClass.typeahead({source: classNames});
    });

    var $output = $("#output");

    $("#generate-form").submit(function () {

        var finalData = $(this).serializeJSON();

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/generate",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: finalData,
            success: function (response) {

                $output.html("");

                for (var i = 0; i < response.length; i++) {
                    $output.append("<h3>" + response[i].templateName + "</h3>");
                    $output.append("<div class=\"card\">\n" +
                        "<div class=\"card-body\">\n" +
                        "<pre>" +
                        response[i].generatedCode +
                        "</pre>" +
                        "</div>\n" +
                        "</div><br />"
                    )
                }
            }
        });

        return false;
    });
});