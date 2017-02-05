package at.rseiler.jango.core.service.decorator;

import java.util.ArrayList;
import java.util.List;

public class DecoratorService<T, R extends Op<T>, S extends OpDec<T>> {
    private final Op<T> service;
    private final List<S> decorators = new ArrayList<>();

    public DecoratorService(R service) {
        this.service = service;
    }

    protected DecoratorService<T, R, S> add(S dec) {
        decorators.add(dec);
        return this;
    }

    protected DecoratorService<T, R, S> add(List<S> decs) {
        decorators.addAll(decs);
        return this;
    }

    protected T exec() {
        T result = service.exec();

        for (OpDec<T> op : decorators) {
            result = op.exec(result);
        }

        return result;
    }
}