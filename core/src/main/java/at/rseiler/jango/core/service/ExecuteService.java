package at.rseiler.jango.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ExecuteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteService.class);
    private final Object mapper;

    public ExecuteService(Object mapper) {
        this.mapper = mapper;
    }

    public void execute(Object source, Object arg) {
        try {
            Method asExecMethod = mapper.getClass().getDeclaredMethod("asExec", source.getClass());
            Object execObj = asExecMethod.invoke(mapper, source);
            Arrays.stream(execObj.getClass().getDeclaredMethods())
                    .filter(method -> method.getName().equals("execute"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed find execute method"))
                    .invoke(execObj, arg);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Failed to execute command", e);
        }
    }
}
