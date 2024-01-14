package com.branow.editors.edit;

import java.util.Locale;

public enum Extension {

    XML, CSS, HTML, JPG, MP3, JAVA, TXT, JS, JSON;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
