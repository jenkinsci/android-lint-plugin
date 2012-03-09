package org.jenkinsci.plugins.android_lint;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.views.DetailFactory;
import hudson.plugins.analysis.views.TabDetail;

import java.util.Collection;

/** Factory for creating TabDetails. */
public class LintDetailBuilder extends DetailFactory {

    @Override
    protected TabDetail createTabDetail(final AbstractBuild<?, ?> owner, final Collection<FileAnnotation> annotations,
            final String url, final String defaultEncoding) {
        return new LintTabDetail(owner, this, annotations, url, defaultEncoding);
    }

}
