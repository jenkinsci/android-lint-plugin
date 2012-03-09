package org.jenkinsci.plugins.android_lint;

import hudson.Launcher;
import hudson.matrix.MatrixRun;
import hudson.matrix.MatrixBuild;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.plugins.analysis.core.AnnotationsAggregator;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.core.ParserResult;

/** Aggregates {@link LintResultAction}s of {@link MatrixRun}s into {@link MatrixBuild}. */
public class LintAnnotationsAggregator extends AnnotationsAggregator {

    /**
     * Constructor.
     *
     * @param build The matrix build.
     * @param launcher Launcher.
     * @param listener Build listener.
     * @param healthDescriptor Health descriptor.
     * @param defaultEncoding Default encoding to be used when reading files.
     */
    public LintAnnotationsAggregator(final MatrixBuild build, final Launcher launcher,
            final BuildListener listener, final HealthDescriptor healthDescriptor,
            final String defaultEncoding) {
        super(build, launcher, listener, healthDescriptor, defaultEncoding);
    }

    @Override
    protected Action createAction(final HealthDescriptor healthDescriptor,
            final String defaultEncoding, final ParserResult aggregatedResult) {
        return new LintResultAction(build, healthDescriptor,
                new LintResult(build, defaultEncoding, aggregatedResult));
    }

    @Override
    protected boolean hasResult(final MatrixRun run) {
        return getAction(run) != null;
    }

    @Override
    protected LintResult getResult(final MatrixRun run) {
        return getAction(run).getResult();
    }

    private LintResultAction getAction(final MatrixRun run) {
        return run.getAction(LintResultAction.class);
    }

}
