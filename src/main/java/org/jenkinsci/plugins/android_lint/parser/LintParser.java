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

import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Severity;

/** A parser for Android Lint XML files. */
public class LintParser extends AbstractAnnotationParser {

    /** Magic value used to denote annotations which have on associated location. */
    public static final String FILENAME_UNKNOWN = "(none)";

    /** Issue priorities equal to or less than this value are mapped to {@link Priority#LOW}. */
    private static final int PRIORITY_LOW_MAXIMUM = 3;

    /** Issue priorities equal to or less than this value are mapped to {@link Priority#NORMAL}. */
    private static final int PRIORITY_NORMAL_MAXIMUM = 7;

    /** Used to access Lint's issue definitions. */
    private static final BuiltinIssueRegistry REGISTRY = new BuiltinIssueRegistry();

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

            // Retrieve metadata about this issue from the Lint API

            final Issue lintIssue = getIssueType(issue);
            final String category;
            final String explanation;
            final Priority priority = getPriority(issue, lintIssue);
            if (lintIssue == null) {
                // If the issue isn't in the registry, then probably the parsed file was
                // created with a newer version of Lint than we bundle with this plugin.
                // The best we can do is to set some defaults and add an explanatory message
                category = Messages.AndroidLint_Parser_UnknownCategory();
                explanation = Messages.AndroidLint_Parser_UnknownExplanation(issue.getId());
            } else {
                category = lintIssue.getCategory().getFullName();
                explanation = lintIssue.getExplanation();
            }

            // Create annotation
            LintAnnotation annotation = new LintAnnotation(priority,
                    StringEscapeUtils.escapeHtml(issue.getMessage()),
                    category, issue.getId(), lineNumber);
            annotation.setExplanation(explanation);
            annotation.setModuleName(moduleName);
            annotation.setFileName(filename);
            if (lineNumber != 0) {
                try {
                    annotation.setContextHashCode(createContextHashCode(filename, lineNumber));
                } catch (IOException e) {
                    // Filename is probably not relative to the workspace root; nothing we can do
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
     * Maps a numeric Lint issue priority to an analysis-core priority value.
     *
     * @param issue The Lint issue.
     * @return Corresponding priority value.
     */
    private Priority getPriority(final LintIssue parsedIssue, final Issue issue) {
        // treat all issues with severity error as high priority
        if (parsedIssue.severity() == Severity.ERROR) {
            return Priority.HIGH;
        }
        // there is no built-in issue for this one, treat as normal
        if (issue == null) {
            return Priority.NORMAL;
        }
        // otherwise base it on predefined priority
        int priority = issue.getPriority();
        if (priority <= PRIORITY_LOW_MAXIMUM) {
            return Priority.LOW;
        } else if (priority <= PRIORITY_NORMAL_MAXIMUM) {
            return Priority.NORMAL;
        }
        return Priority.HIGH;
    }

    /**
     * Retrieves the issue type that was triggered.
     *
     * @param issue The parsed issue.
     * @return The corresponding Lint issue object.
     */
    private Issue getIssueType(final LintIssue issue) {
        return REGISTRY.getIssue(issue.getId());
    }

}
