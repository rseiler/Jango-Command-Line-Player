package at.rseiler.jango.core.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FileUtilTest {
    @Test
    public void sanitizeName() throws Exception {
        String name = FileUtil.sanitizeName("hello \"\\/*<>|?:world");

        assertThat(name, is("hello world"));
    }

}