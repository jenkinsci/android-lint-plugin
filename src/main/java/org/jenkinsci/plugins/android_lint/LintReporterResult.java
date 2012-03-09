package org.jenkinsci.plugins.android_lint;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

/** Represents the aggregated results of Lint results in an m2 build. */
public class LintReporterResult extends LintResult {

    private static final long serialVersionUID = 1693573220866799558L;

    /**
     * @param build The current build as owner of this action.
     * @param defaultEncoding The default encoding to be used when reading files.
     * @param result The parsed result with all annotations.
     */
    public LintReporterResult(final AbstractBuild<?, ?> build, final String defaultEncoding,
            final ParserResult result) {
        super(build, defaultEncoding, result);
    }

    @Override
    protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
        return LintMavenResultAction.class;
    }

}
