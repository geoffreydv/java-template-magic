# Java Template Magic

Most Java web applications have domain classes with several fields.

In many projects there are situations where you need to create another class or view based on these classes and their fields.

Some examples:
    - You need a "DTO" for this class
    - You have a couple of admin screens, requiring you to define "crud" html pages for this entity. This might mean you have to define an "Overview" page, a "create form" and an "edit form"
    - You might have a javascript class that represents the json structure of the entity
    
Creating pages or classes like this takes some time. In this case you would have to create a java class, three html pages and a javascript file, containing the same fields but represented in a different way.
When the fields in the entity change, it would require you to make changes in all the other classes as well.

This is manageable for a handful of classes but if your domain grows this can become a hassle to maintain.

To help with that, I created this small tool.

## Features

The tool scans directories recursively for java source files and builds a "knowledge base" of the entities in your project and their fields.
You can then use this knowledge base to generate any plaintext file (using the velocity engine's markup language).

Several helper methods are provided to easily access all the known fields, their type, if they are mandatory or not etc.
