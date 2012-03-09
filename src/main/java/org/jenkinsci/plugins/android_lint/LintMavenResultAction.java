package org.jenkinsci.plugins.android_lint;

import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.Action;
import hudson.plugins.analysis.core.HealthDescriptor;
import hudson.plugins.analysis.core.MavenResultAction;

import java.util.List;
import java.util.Map;

/**
 * A {@link LintResultAction} for native Maven jobs.
 * <p>
 * This action additionally provides result aggregation for sub-modules and for the main project.
 */
public class LintMavenResultAction extends MavenResultAction<LintResult> {

    private static final String PLUGIN_NAME = "android-lint";

    /**
     * Creates a new instance of {@link LintMavenResultAction}.
     * <p>
     * This instance will have no result set in the beginning. The result will be set successively
     * after each of the modules are build.
     *
     * @param owner The associated build of this action.
     * @param healthDescriptor HealthDescriptor to use.
     * @param defaultEncoding The default encoding to be used when reading and files.
     */
    public LintMavenResultAction(final MavenModuleSetBuild owner,
            final HealthDescriptor healthDescriptor, final String defaultEncoding) {
        super(new LintResultAction(owner, healthDescriptor), defaultEncoding, PLUGIN_NAME);
    }

    /**
     * Creates a new instance of {@link LintMavenResultAction}.
     *
     * @param owner The associated build of this action.
     * @param healthDescriptor HealthDescriptor to use.
     * @param defaultEncoding The default encoding to be used when reading and files.
     * @param result The result for this build.
     */
    public LintMavenResultAction(final MavenBuild owner, final HealthDescriptor healthDescriptor,
            final String defaultEncoding, final LintResult result) {
        super(new LintResultAction(owner, healthDescriptor, result), defaultEncoding, PLUGIN_NAME);
    }

    public MavenAggregatedReport createAggregatedAction(final MavenModuleSetBuild build,
            final Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        return new LintMavenResultAction(build, getHealthDescriptor(), getDisplayName());
    }

    public Action getProjectAction(final MavenModuleSet moduleSet) {
        return new LintProjectAction(moduleSet, LintMavenResultAction.class);
    }

    @Override
    public Class<? extends MavenResultAction<LintResult>> getIndividualActionType() {
        return LintMavenResultAction.class;
    }

    @Override
    protected LintResult createResult(final LintResult existingResult,
            final LintResult additionalResult) {
        return new LintReporterResult(getOwner(), additionalResult.getDefaultEncoding(),
                aggregate(existingResult, additionalResult));
    }

}
