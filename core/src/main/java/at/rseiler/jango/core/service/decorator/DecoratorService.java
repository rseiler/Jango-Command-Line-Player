package at.rseiler.jango.core.service.decorator;

import java.util.ArrayList;
import java.util.List;

public class DecoratorService<T, R extends Op<T>, S extends OpDec<T>> {
    private final Op<T> init;
    private List<S> decs = new ArrayList<>();

    public DecoratorService(R init) {
        this.init = init;
    }

    public void addDecorator(S dec) {
        decs.add(dec);
    }

    public T exec() {
        T result = init.exec();
        for (OpDec<T> op : decs) {
            result = op.exec(result);
        }
        return result;
    }

}