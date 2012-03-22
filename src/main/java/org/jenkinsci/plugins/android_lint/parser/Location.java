package org.jenkinsci.plugins.android_lint.parser;

import java.io.Serializable;

/** Represents a location of a Lint issue. */
public class Location implements Serializable {

    private static final long serialVersionUID = 1128640353127613495L;

    private String filename;
    private int lineNumber;
    private int column;

    /** @return The file name. */
    public String getFile() {
        return filename;
    }

    /** @param filename The filename to set. */
    public void setFile(final String filename) {
        this.filename = filename;
    }

    /** @return The line number where the issue occurs. */
    public int getLine() {
        return lineNumber;
    }

    /** @param lineNumber Line where the issue occurs. */
    public void setLine(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /** @return The column on the line where the issue occurs. */
    public int getColumn() {
        return column;
    }

    /** @param column Column on the line where the issue occurs. */
    public void setColumn(final int column) {
        this.column = column;
    }

}
