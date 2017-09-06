package org.jenkinsci.plugins.android_lint;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.maven.MavenReporter;
import hudson.plugins.analysis.core.ReporterDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;

/**
 * Descriptor for the class {@link LintReporter}.<br>
 * Used as a singleton.
 * <p>
 * The class is marked as public so that it can be accessed from views.
 */
@Extension(ordinal = 100)
public class LintReporterDescriptor extends ReporterDescriptor {

    public LintReporterDescriptor() {
        super(LintReporter.class, new LintDescriptor());
    }

    @Override
    @SuppressFBWarnings("NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")
    public MavenReporter newInstance(final StaplerRequest request, @Nonnull final JSONObject formData)
            throws FormException {
        return request.bindJSON(LintReporter.class, formData);
    }

}
