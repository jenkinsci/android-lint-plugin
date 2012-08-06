package org.jenkinsci.plugins.android_lint.parser;


import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;
import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LintParserTest extends TestCase {

    public void testParser() throws Exception {
        List<FileAnnotation> annotations = parseResults();
        assertEquals(3, annotations.size());

        FileAnnotation a1 = annotations.get(0);
        assertEquals("Call requires API level 8 (current min is 7): android.view.MotionEvent#getActionIndex", a1.getMessage());
        assertEquals(Priority.HIGH, a1.getPriority());
        assertEquals("bin/classes/InputObject.class", a1.getFileName());
        assertEquals("test", a1.getModuleName());
        assertEquals("NewApi", a1.getType());
        assertEquals("Unknown", a1.getCategory());

        FileAnnotation a2 = annotations.get(1);
        assertEquals("The &lt;activity&gt; MonitoredActivity is not registered in the manifest", a2.getMessage());
        assertEquals(Priority.NORMAL, a2.getPriority());
        assertEquals("bin/classes/MonitoredActivity.class", a2.getFileName());
        assertEquals("test", a2.getModuleName());
        assertEquals("Registered", a2.getType());
        assertEquals("Unknown", a2.getCategory());

        FileAnnotation a3 = annotations.get(2);
        assertEquals(Priority.LOW, a3.getPriority());
        assertEquals("Avoid using &quot;px&quot; as units; use &quot;dp&quot; instead", a3.getMessage());
        assertEquals("res/layout/foo.xml", a3.getFileName());
        assertEquals(19, a3.getPrimaryLineNumber());
    }

    public void testIssueWithSeverityErrorHasHighestPriority() throws  Exception {
        List<FileAnnotation> annotations = parseResults();
        FileAnnotation a1 = annotations.get(0);
        assertEquals(Priority.HIGH, a1.getPriority());
    }

    private List<FileAnnotation> parseResults() throws InvocationTargetException {
        LintParser parser = new LintParser("UTF-8");
        Collection<FileAnnotation> annotations = parser.parse(getClass().getResourceAsStream("lint-results.xml"), "test");
        return new ArrayList<FileAnnotation>(annotations);
    }
}
