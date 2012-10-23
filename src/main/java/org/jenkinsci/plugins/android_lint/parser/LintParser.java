package org.jenkinsci.plugins.android_lint.parser;

import hudson.plugins.analysis.core.AbstractAnnotationParser;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringEscapeUtils;
import org.jenkinsci.plugins.android_lint.Messages;
import org.xml.sax.SAXException;

/** A parser for Android Lint XML files. */
public class LintParser extends AbstractAnnotationParser {

    /** Magic value used to denote annotations which have on associated location. */
    public static final String FILENAME_UNKNOWN = "(none)";

    /** Severity constant value from {@link com.android.tools.lint.detector.api.Severity}. */
    private static final String SEVERITY_FATAL = "Fatal";

    /** Severity constant value from {@link com.android.tools.lint.detector.api.Severity}. */
    private static final String SEVERITY_INFORMATIONAL = "Informational";

    private static final long serialVersionUID = 7110868408124058985L;

    /**
     * Creates a parser for Android Lint files.
     *
     * @param defaultEncoding The encoding to use when reading files.
     */
    public LintParser(final String defaultEncoding) {
        super(defaultEncoding);
    }

    @Override
    public Collection<FileAnnotation> parse(final InputStream file, final String moduleName)
            throws InvocationTargetException {
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setClassLoader(LintParser.class.getClassLoader());

            List<LintIssue> issues = new ArrayList<LintIssue>();
            digester.push(issues);

            String issueXPath = "issues/issue";
            digester.addObjectCreate(issueXPath, LintIssue.class);
            digester.addSetProperties(issueXPath);
            digester.addSetNext(issueXPath, "add");

            String locationXPath = issueXPath + "/location";
            digester.addObjectCreate(locationXPath, Location.class);
            digester.addSetProperties(locationXPath);
            digester.addSetNext(locationXPath, "addLocation", Location.class.getName());

            digester.parse(file);

            return convert(issues, moduleName);
        } catch (IOException exception) {
            throw new InvocationTargetException(exception);
        } catch (SAXException exception) {
            throw new InvocationTargetException(exception);
        }
    }

    /**
     * Converts the Lint object structure to that of the analysis-core API.
     *
     * @param issues The parsed Lint issues.
     * @param moduleName Name of the maven module, if any.
     * @return A collection of the discovered issues.
     */
    private Collection<FileAnnotation> convert(final List<LintIssue> issues, final String moduleName) {
        ArrayList<FileAnnotation> annotations = new ArrayList<FileAnnotation>();

        for (LintIssue issue : issues) {
            // Get filename of first location, if available
            final String filename;
            final int lineNumber;
            final Location[] locations = issue.getLocations().toArray(new Location[0]);
            final int locationCount = locations.length;
            if (locationCount == 0) {
                filename = FILENAME_UNKNOWN;
                lineNumber = 0;
            } else {
                // TODO: Ideally, we would expand relative paths (like ParserResult does later)
                filename = locations[0].getFile();
                lineNumber = locations[0].getLine();
            }

            final Priority priority = getPriority(issue.getSeverity());
            String category = issue.getCategory();
            String explanation = issue.getExplanation();

            // If category is missing the file is from pre-r21 Lint, so show an explanation
            if (category == null) {
                category = Messages.AndroidLint_Parser_UnknownCategory();
                explanation = Messages.AndroidLint_Parser_UnknownExplanation(issue.getId());
            }

            // Create annotation
            LintAnnotation annotation = new LintAnnotation(priority,
                    StringEscapeUtils.escapeHtml(issue.getMessage()),
                    category, issue.getId(), lineNumber);
            annotation.setExplanation(explanation);
            annotation.setErrorLines(StringEscapeUtils.escapeHtml(issue.getErrorLine1()),
                    StringEscapeUtils.escapeHtml(issue.getErrorLine2()));
            annotation.setModuleName(moduleName);
            annotation.setFileName(filename);

            // Generate a hash to uniquely identify this issue and its context (i.e. source code),
            // so that we can detect in later builds whether this issue still exists, or was fixed
            if (lineNumber == 0) {
                // This issue is for a non-source file, so use the issue type and filename
                int hashcode = String.format("%s:%s", filename, issue.getId()).hashCode();
                annotation.setContextHashCode(hashcode);
            } else {
                // This is a source file (i.e. Java or XML), so use a few lines of context
                // surrounding the line on which the issue first occurs, so that we can detect
                // whether this issue still exists later, even if the line numbers have changed
                try {
                    annotation.setContextHashCode(createContextHashCode(filename, lineNumber));
                } catch (IOException e) {
                    // Filename is probably not relative to the workspace root, so we can't read out
                    // the surrounding context of this issue. Nothing we can do about this
                }
            }

            // Add additional locations for this the issue, if any
            for (int i = 1, n = locationCount; i < n; i++) {
                annotation.addLocation(locations[i]);
            }

            annotations.add(annotation);
        }

        return annotations;
    }

    /**
     * Maps a Lint issue severity to an analysis-core priority value.
     *
     * @param severity Issue severity value read from XML.
     * @return Corresponding priority value.
     */
    private Priority getPriority(String severity) {
        if (SEVERITY_FATAL.equals(severity)) {
            return Priority.HIGH;
        }
        if (SEVERITY_INFORMATIONAL.equals(severity)) {
            return Priority.LOW;
        }
        return Priority.NORMAL;
    }

}
