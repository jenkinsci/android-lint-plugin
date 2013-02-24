package org.jenkinsci.plugins.android_lint;

import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuildProxy;
import hudson.maven.MojoInfo;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.plugins.analysis.core.FilesParser;
import hudson.plugins.analysis.core.HealthAwareReporter;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.PluginLogger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.jenkinsci.plugins.android_lint.parser.LintParser;
import org.kohsuke.stapler.DataBoundConstructor;

/** Publishes the results of parsing an Android lint file (maven 2 project type). */
public class LintReporter extends HealthAwareReporter<LintResult> {

    /** Plugin name used in console output. */
    private static final String PLUGIN_NAME = "android-lint";

    /** Default filename pattern. */
    private static final String DEFAULT_PATTERN = "**/lint-results.xml";

    private static final long serialVersionUID = 3712982963880076975L;

    /** Ant fileset pattern of files to work with. */
    private final String pattern;

    /**
     * Constructor.
     *
     * @param healthy Report health as 100% when the number of warnings is less than this value.
     * @param unhealthy Report health as 0% when the number of warnings is greater than this value.
     * @param thresholdLimit Determines which warning priorities should be considered when
     *            evaluating the build stability and health.
     * @param useDeltaValues Determines whether the absolute annotations delta or the actual
     *            annotations set difference should be used to evaluate the build stability.
     * @param unstableTotalAll Annotation threshold.
     * @param unstableTotalHigh Annotation threshold.
     * @param unstableTotalNormal Annotation threshold.
     * @param unstableTotalLow Annotation threshold.
     * @param unstableNewAll Annotation threshold.
     * @param unstableNewHigh Annotation threshold.
     * @param unstableNewNormal Annotation threshold.
     * @param unstableNewLow Annotation threshold.
     * @param failedTotalAll Annotation threshold.
     * @param failedTotalHigh Annotation threshold.
     * @param failedTotalNormal Annotation threshold.
     * @param failedTotalLow Annotation threshold.
     * @param failedNewAll Annotation threshold.
     * @param failedNewHigh Annotation threshold.
     * @param failedNewNormal Annotation threshold.
     * @param failedNewLow Annotation threshold.
     * @param canRunOnFailed Determines whether the plugin can also run for failed builds.
     * @param pattern Ant fileset pattern used to scan for Lint files.
     */
    @DataBoundConstructor
    public LintReporter(final String healthy, final String unhealthy, final String thresholdLimit,
            final boolean useDeltaValues, final String unstableTotalAll,
            final String unstableTotalHigh, final String unstableTotalNormal,
            final String unstableTotalLow, final String unstableNewAll,
            final String unstableNewHigh, final String unstableNewNormal,
            final String unstableNewLow, final String failedTotalAll, final String failedTotalHigh,
            final String failedTotalNormal, final String failedTotalLow, final String failedNewAll,
            final String failedNewHigh, final String failedNewNormal, final String failedNewLow,
            final boolean canRunOnFailed, final String pattern) {
        super(healthy, unhealthy, thresholdLimit, useDeltaValues,
                unstableTotalAll, unstableTotalHigh, unstableTotalNormal, unstableTotalLow,
                unstableNewAll, unstableNewHigh, unstableNewNormal, unstableNewLow,
                failedTotalAll, failedTotalHigh, failedTotalNormal, failedTotalLow,
                failedNewAll, failedNewHigh, failedNewNormal, failedNewLow,
                canRunOnFailed, true, PLUGIN_NAME);
        this.pattern = pattern;
    }

    /**
     * Returns the Ant fileset pattern of files to work with.
     *
     * @return Ant fileset pattern of files to work with.
     */
    public String getPattern() {
        return pattern;
    }

    @Override
    protected boolean acceptGoal(final String goal) {
        return "android-lint".equals(goal) || "site".equals(goal) || "report".equals(goal)
                || "check".equals(goal);
    }

    @Override
    public ParserResult perform(final MavenBuildProxy build, final MavenProject pom,
            final MojoInfo mojo, final PluginLogger logger) throws InterruptedException, IOException {
        FilesParser lintCollector = new FilesParser(PLUGIN_NAME,
                StringUtils.defaultIfEmpty(getPattern(), DEFAULT_PATTERN),
                new LintParser(getDefaultEncoding()), getModuleName(pom));
        return getTargetPath(pom).act(lintCollector);
    }

    @Override
    protected LintResult createResult(final MavenBuild build, final ParserResult project) {
        return new LintReporterResult(build, getDefaultEncoding(), project);
    }

    @Override
    protected MavenAggregatedReport createMavenAggregatedReport(final MavenBuild build,
            final LintResult result) {
        return new LintMavenResultAction(build, this, getDefaultEncoding(), result);
    }

    @Override
    public List<LintProjectAction> getProjectActions(final MavenModule module) {
        return Collections.singletonList(new LintProjectAction(module, getResultActionClass()));
    }

    @Override
    protected Class<LintMavenResultAction> getResultActionClass() {
        return LintMavenResultAction.class;
    }

}
