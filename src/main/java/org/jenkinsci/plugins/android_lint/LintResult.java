package org.jenkinsci.plugins.android_lint;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.core.BuildHistory;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

import org.jenkinsci.plugins.android_lint.parser.LintAnnotation;
import org.jenkinsci.plugins.android_lint.parser.Location;

import com.thoughtworks.xstream.XStream;

/**
 * Represents the results of a Lint analysis.
 * <p>
 * One instance of this class is persisted for each build via an XML file.
 */
public class LintResult extends BuildResult {

    private static final long serialVersionUID = 7668215801789127570L;

    /**
     * Creates a new instance of {@link LintResult}.
     *
     * @param build The current build as owner of this action.
     * @param defaultEncoding The default encoding to be used when reading files.
     * @param result The parsed result with all annotations.
     */
    public LintResult(final AbstractBuild<?, ?> build, final String defaultEncoding,
            final ParserResult result) {
        super(build, defaultEncoding, result);
    }

    /**
     * Creates a new instance of {@link LintResult}.
     *
     * @param build The current build as owner of this action.
     * @param defaultEncoding The default encoding to be used when reading files.
     * @param result The parsed result with all annotations.
     * @param history Build result history for this plugin.
     */
    public LintResult(final AbstractBuild<?, ?> build, final String defaultEncoding,
            final ParserResult result, final BuildHistory history) {
        super(build, defaultEncoding, result, history);
    }

    @Override
    protected void configure(final XStream xstream) {
        xstream.alias("issue", LintAnnotation.class);
        xstream.alias("location", Location.class);
    }

    /**
     * Returns a summary message for the summary.jelly file.
     *
     * @return the summary message
     */
    public String getSummary() {
        return SummaryGenerator.createSummary(this);
    }

    @Override
    protected String createDeltaMessage() {
        return SummaryGenerator.createDeltaMessage(this);
    }

    @Override
    protected String getSerializationFileName() {
        return "android-lint-issues.xml";
    }

    public String getDisplayName() {
        return Messages.AndroidLint_DisplayName();
    }

    /**
     * Returns the actual type of the associated result action.
     *
     * @return the actual type of the associated result action
     */
    @Override
    protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
        return LintResultAction.class;
    }

}
