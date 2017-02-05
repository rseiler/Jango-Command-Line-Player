package at.rseiler.jango.core.service.decorator;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DecoratorServiceTest {

    @Test
    public void testDecoratorService() throws Exception {
        DecoratorService<String, Op<String>, OpDec<String>> decoratorService = new DecoratorService<>(new Init());
        decoratorService.addDecorator(new A());
        decoratorService.addDecorator(new B());

        assertThat(decoratorService.exec(), is("init/A/B"));
    }

    class Init implements Op<String> {
        public String exec() {
            return "init";
        }
    }

    class A implements OpDec<String> {
        @Override
        public String exec(String arg) {
            return arg + "/A";
        }
    }

    class B implements OpDec<String> {
        @Override
        public String exec(String arg) {
            return arg + "/B";
        }
    }
}