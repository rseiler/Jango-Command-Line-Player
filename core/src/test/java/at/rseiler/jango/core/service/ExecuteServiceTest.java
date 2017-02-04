package at.rseiler.jango.core.service;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExecuteServiceTest {
    @Test
    public void execute() throws Exception {
        Execute execute = new Execute();

        new ExecuteService(new Mapper(execute)).execute("source", "test");

        assertThat(execute.executed, is(1));
        assertThat(execute.argument, is("test"));
    }

    @Test(expected = RuntimeException.class)
    public void invalidMapper() throws Exception {
        new ExecuteService("invalid").execute("source", "test");
    }
    
    @Test(expected = RuntimeException.class)
    public void invalidExecutor() throws Exception {
        new ExecuteService(new Mapper("invalid")).execute("source", "test");
    }

    static class Mapper {
        private final Object execute;

        Mapper(Object execute) {
            this.execute = execute;
        }

        public Object asExec(String arg) {
            return execute;
        }

        public Object asExec(Integer arg) {
            return execute;
        }
    }

    static class Execute {
        private int executed = 0;
        private String argument;

        void execute(String arg) {
            executed++;
            argument = arg;
        }
    }
}
