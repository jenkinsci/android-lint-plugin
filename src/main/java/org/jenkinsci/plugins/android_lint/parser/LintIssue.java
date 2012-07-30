package org.jenkinsci.plugins.android_lint.parser;

import java.util.ArrayList;
import java.util.List;

import com.android.tools.lint.detector.api.Severity;

/** Represents a single issue in a Lint XML file. */
public class LintIssue {

    /** Lint rule ID. */
    private String id;

    /** Issue severity. */
    private String severity;

    /** Message describing the issue. */
    private String message;

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

    /** @return The issue description. */
    public String getMessage() {
        return message;
    }

    /** @param message The issue description. */
    public void setMessage(final String message) {
        this.message = message;
    }

    /** @return List of locations the issue was found in. */
    public List<Location> getLocations() {
        return locations;
    }

    /** @param location Location to add to this issue. */
    public void addLocation(final Location location) {
        locations.add(location);
    }

    public Severity severity() {
        for (Severity s : Severity.values()) {
           if (s.getDescription().equals(severity)) return s;
        }
        return Severity.WARNING;
    }
}
