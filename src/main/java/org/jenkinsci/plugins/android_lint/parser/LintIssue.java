package org.jenkinsci.plugins.android_lint.parser;

import java.util.ArrayList;
import java.util.List;

/** Represents a single issue in a Lint XML file. */
public class LintIssue {

    /** Lint rule ID. */
    private String id;

    /** Severity of this issue type. */
    private String severity;

    /** Categorisation of this issue type. */
    private String category;

    /** Relative priority of this issue type. */
    private int priority;

    /** Generic, longer explanation of the issue. */
    private String explanation;

    /** Message describing the issue. */
    private String message;

    /** First error line, pinpointing the issue location. */
    private String errorLine1;

    /** Second error line, pinpointing the issue location. */
    private String errorLine2;

    /** File(s) in which the issue was found. */
    private final ArrayList<Location> locations = new ArrayList<Location>();

    /** @return The Lint rule ID. */
    public String getId() {
        return id;
    }
    /** @param id The Lint rule ID. */
    public void setId(final String id) {
        this.id = id;
    }

    /** @return The issue severity. */
    public String getSeverity() {
        return severity;
    }
    /** @param severity The issue severity. */
    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    /** @return The issue category. */
    public String getCategory() {
        return category;
    }

    /** @param category The issue category. */
    public void setCategory(String category) {
        this.category = category;
    }

    /** @return Relative issue priority. */
    public int getPriority() {
        return priority;
    }

    /** @param priority Relative issue priority. */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /** @return Generic explanation for this issue type. */
    public String getExplanation() {
        return explanation;
    }

    /** @param explanation Generic explanation for this issue type. */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /** @return The issue description. */
    public String getMessage() {
        return message;
    }

    /** @param message The issue description. */
    public void setMessage(final String message) {
        this.message = message;
    }

    /** @return First error line, pinpointing the issue location. */
    public String getErrorLine1() {
        return errorLine1;
    }

    /** @param errorLine1 First error line, pinpointing the issue location. */
    public void setErrorLine1(String errorLine1) {
        this.errorLine1 = errorLine1;
    }

    /** @return Second error line, pinpointing the issue location. */
    public String getErrorLine2() {
        return errorLine2;
    }

    /** @param errorLine2 Second error line, pinpointing the issue location. */
    public void setErrorLine2(String errorLine2) {
        this.errorLine2 = errorLine2;
    }

    /** @return List of locations the issue was found in. */
    public List<Location> getLocations() {
        return locations;
    }

    /** @param location Location to add to this issue. */
    public void addLocation(final Location location) {
        locations.add(location);
    }

}
