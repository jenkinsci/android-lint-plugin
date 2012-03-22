package org.jenkinsci.plugins.android_lint;

import hudson.Extension;
import hudson.Functions;
import hudson.plugins.analysis.core.PluginDescriptor;

/**
 * Descriptor for the class {@link LintPublisher}.<br>
 * Used as a singleton.
 * <p>
 * The class is marked as public so that it can be accessed from views.
 */
@Extension(ordinal = 100)
public final class LintDescriptor extends PluginDescriptor {

    /** Used in visible URLs. */
    public static final String PLUGIN_NAME = "androidLint";

    /** Used to specify location of resources. */
    public static final String PLUGIN_ROOT = Functions.getResourcePath() + "/plugin/android-lint/";

    /** Icon to use for the result and project action. */
    private static final String ACTION_ICON = PLUGIN_ROOT + "icons/android-24x24.png";

    public LintDescriptor() {
        super(LintPublisher.class);
    }

    @Override
    public String getDisplayName() {
        return Messages.AndroidLint_Publisher_Name();
    }

    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getPluginRoot() {
        return PLUGIN_ROOT;
    }

    @Override
    public String getIconUrl() {
        return ACTION_ICON;
    }

}
