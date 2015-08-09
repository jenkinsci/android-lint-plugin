package org.jenkinsci.plugins.android_lint;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;

/**
 * Represents the aggregated results of Lint results in an m2 build.
 */
public class LintReporterResult extends LintResult {

    private static final long serialVersionUID = 1693573220866799558L;

    /**
     * @param build The current build as owner of this action.
     * @param defaultEncoding The default encoding to be used when reading files.
     * @param result The parsed result with all annotations.
     *
     * @deprecated see {@link #LintReporterResult(Run, String, ParserResult, boolean, boolean)}
     */
    @Deprecated
    public LintReporterResult(final AbstractBuild<?, ?> build, final String defaultEncoding, final ParserResult result) {
        this((Run<?, ?>) build, defaultEncoding, result, false, false);
    }

    /**
     * @param build The current build as owner of this action.
     * @param defaultEncoding The default encoding to be used when reading files.
     * @param result The parsed result with all annotations.
     * @param usePreviousBuildAsReference Determines whether to use the previous build as the reference
     * @param useStableBuildAsReference Determines whether only stable builds should be used as reference builds or not
     */
    public LintReporterResult(final Run<?, ?> build, final String defaultEncoding, final ParserResult result, final boolean usePreviousBuildAsReference, final boolean useStableBuildAsReference) {
        super(build, defaultEncoding, result, usePreviousBuildAsReference, useStableBuildAsReference);
    }

    @Override
    protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
        return LintMavenResultAction.class;
    }

}
