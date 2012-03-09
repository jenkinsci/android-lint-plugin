package org.jenkinsci.plugins.android_lint;

import hudson.Extension;
import hudson.maven.MavenReporter;
import hudson.plugins.analysis.core.ReporterDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

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
    public MavenReporter newInstance(final StaplerRequest request, final JSONObject formData)
            throws FormException {
        return request.bindJSON(LintReporter.class, formData);
    }

}
