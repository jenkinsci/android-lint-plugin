package org.jenkinsci.plugins.android_lint;

import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.FilesParser;
import hudson.plugins.analysis.core.HealthAwarePublisher;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.PluginLogger;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.android_lint.parser.LintParser;
import org.kohsuke.stapler.DataBoundConstructor;

/** Publishes the results of parsing an Android lint file. */
public class LintPublisher extends HealthAwarePublisher {

    /** Plugin name used in console output. */
    private static final String PLUGIN_NAME = "android-lint";

    /** Default filename pattern. */
    private static final String DEFAULT_PATTERN = "**/lint-results.xml";

    private static final long serialVersionUID = 3435696173660003622L;

    /** Ant fileset pattern of files to work with. */
    private final String pattern;

    /**
     * Constructor.
     *
     * @param healthy Report health as 100% when the number of warnings is less than this value.
     * @param unhealthy Report health as 0% when the number of warnings is greater than this value.
     * @param thresholdLimit Determines which warning priorities should be considered when
     *            evaluating the build stability and health.
     * @param defaultEncoding The default encoding to be used when reading files.
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
     * @param shouldDetectModules Determines whether module names should be derived from Maven POM
     *            or Ant build files.
     * @param pattern Ant fileset pattern used to scan for Lint files.
     */
    @DataBoundConstructor
    public LintPublisher(final String healthy, final String unhealthy, final String thresholdLimit,
            final String defaultEncoding, final boolean useDeltaValues,
            final String unstableTotalAll, final String unstableTotalHigh,
            final String unstableTotalNormal, final String unstableTotalLow,
            final String unstableNewAll, final String unstableNewHigh,
            final String unstableNewNormal, final String unstableNewLow,
            final String failedTotalAll, final String failedTotalHigh,
            final String failedTotalNormal, final String failedTotalLow, final String failedNewAll,
            final String failedNewHigh, final String failedNewNormal, final String failedNewLow,
            final boolean canRunOnFailed, final boolean shouldDetectModules, final String pattern) {
        super(healthy, unhealthy, thresholdLimit, defaultEncoding, useDeltaValues,
                unstableTotalAll, unstableTotalHigh, unstableTotalNormal, unstableTotalLow,
                unstableNewAll, unstableNewHigh, unstableNewNormal, unstableNewLow,
                failedTotalAll, failedTotalHigh, failedTotalNormal, failedTotalLow,
                failedNewAll, failedNewHigh, failedNewNormal, failedNewLow,
                canRunOnFailed, shouldDetectModules, true, PLUGIN_NAME);
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
    public Action getProjectAction(final AbstractProject<?, ?> project) {
        return new LintProjectAction(project);
    }

    @Override
    public BuildResult perform(final AbstractBuild<?, ?> build, final PluginLogger logger)
            throws InterruptedException, IOException {
        logger.log(Messages.AndroidLint_Publisher_CollectingFiles());
        FilesParser lintCollector = new FilesParser(PLUGIN_NAME,
                StringUtils.defaultIfEmpty(getPattern(), DEFAULT_PATTERN),
                new LintParser(getDefaultEncoding()), shouldDetectModules(), isMavenBuild(build));
        ParserResult project = build.getWorkspace().act(lintCollector);
        logger.logLines(project.getLogMessages());

        LintResult result = new LintResult(build, getDefaultEncoding(), project);
        build.getActions().add(new LintResultAction(build, this, result));

        return result;
    }

    public MatrixAggregator createAggregator(final MatrixBuild build, final Launcher launcher,
            final BuildListener listener) {
        return new LintAnnotationsAggregator(build, launcher, listener, this, getDefaultEncoding());
    }

    @Override
    public LintDescriptor getDescriptor() {
        return (LintDescriptor) super.getDescriptor();
    }

}
