package at.rseiler.jango.core.service.decorator;

public interface OpDec<T> {
    T exec(T arg);
}