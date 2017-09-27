package com.codekickstarter.generator.core;

import org.atteo.evo.inflector.English;

public class Utils {

    public static String lowerFirstCharacter(String word) {
        if(word == null) {
            return "";
        }
        return word.substring(0, 1).toLowerCase() + word.substring(1, word.length());
    }

    public static String plural(String word) {
        return English.plural(word);
    }
}
