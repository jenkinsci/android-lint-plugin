package org.jenkinsci.plugins.android_lint.parser;

import hudson.plugins.analysis.util.model.AbstractAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

/** An analysis-core Annotation representing a potential bug as determined by Lint. */
public class LintAnnotation extends AbstractAnnotation {

    /** Origin of the annotation. */
    private static final String ORIGIN = "android-lint";

    /** File locations associated with this Lint issue. */
    private final HashSet<Location> locations = new HashSet<Location>();

    private static final long serialVersionUID = 4163257426733883046L;

    /** Issue explanation text. */
    private String explanation;

    /** Lines pinpointing where the error occurred. */
    private final List<String> errorLines;

    /**
     * Constructor.
     *
     * @param priority Priority of this bug.
     * @param message Describes exactly what the bug is.
     * @param category Bug categorisation.
     * @param type Type within the category
     * @param lineNumber Line number the bug starts on.
     */
    public LintAnnotation(final Priority priority, final String message, final String category,
            final String type, final int lineNumber) {
        super(priority, message, lineNumber, lineNumber, category, type);
        errorLines = new ArrayList<String>();
        setOrigin(ORIGIN);
    }

    /** @param explanation An explanation of the Lint rule violated. */
    public void setExplanation(final String explanation) {
        this.explanation = explanation;
    }

    @VisibleForTesting
    String getExplanation() {
        return this.explanation;
    }

    /** Sets the error lines for this issue. */
    public void setErrorLines(String... lines) {
        errorLines.clear();
        for (String line : lines) {
            if (line != null) {
                errorLines.add(line);
            }
        }
    }

    /** @return A list of locations where the issue occurs. */
    public List<String> getErrorLines() {
        return Collections.unmodifiableList(errorLines);
    }

    /**
     * Adds an additional location where this issue occurs.
     *
     * @param location A location of this issue.
     */
    public void addLocation(Location location) {
        locations.add(location);
    }

    /**
     * @return A list of locations (beyond the one specified by {@link #getFileName()}) where the
     *         issue occurs.
     */
    public Collection<Location> getLocations() {
        return Collections.unmodifiableCollection(locations);
    }

    public String getToolTip() {
        // Show the issue
        StringBuilder message = new StringBuilder();
        message.append("<p>");
        message.append(explanation);

        // Add error context information
        if (!errorLines.isEmpty()) {
            message.append("<div style='color:#d00'><b><pre>");
            for (String line : errorLines) {
                message.append(line);
                message.append('\n');
            }
            message.append("</pre></b></div>");
        }

        // Show additional locations, if any
        if (locations.size() != 0) {
            message.append("<p/><b>");
            message.append("Additional locations:");
            message.append("</b><ul>");
            for (Location issue : locations) {
                message.append("<li>");
                // TODO: Ideally, we would link to the source file, if appropriate
                message.append(issue.getFile());
                message.append("</li>");
            }
            message.append("</ul>");

        }

        message.append("</p>");
        return message.toString();
    }

}
