package at.rseiler.jango.core.service.decorator;

public interface Op<T> {
    T exec();
}