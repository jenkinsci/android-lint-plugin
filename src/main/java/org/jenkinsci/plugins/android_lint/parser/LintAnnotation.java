package org.jenkinsci.plugins.android_lint.parser;

import hudson.plugins.analysis.util.model.AbstractAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/** An analysis-core Annotation representing a potential bug as determined by Lint. */
public class LintAnnotation extends AbstractAnnotation {

    /** Origin of the annotation. */
    private static final String ORIGIN = "android-lint";

    /** File locations associated with this Lint issue. */
    private final HashSet<Location> locations = new HashSet<Location>();

    private static final long serialVersionUID = 4163257426733883046L;

    /** Issue explanation text. */
    private String explanation;

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
        setOrigin(ORIGIN);
    }

    /** @param explanation An explanation of the Lint rule violated. */
    public void setExplanation(final String explanation) {
        this.explanation = explanation;
    }

    /**
     * Adds a location where this issue occurs.
     *
     * @param location A location of this issue.
     */
    public void addLocation(Location location) {
        locations.add(location);
    }

    /** @return A list of locations where the issue occurs. */
    public Collection<Location> getLocations() {
        return Collections.unmodifiableCollection(locations);
    }

    public String getToolTip() {
        // Show the issue
        StringBuilder message = new StringBuilder();
        message.append("<p>");
        message.append(explanation);

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
