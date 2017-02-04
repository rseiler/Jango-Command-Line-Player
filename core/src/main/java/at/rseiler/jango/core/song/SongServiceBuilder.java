package at.rseiler.jango.core.song;


import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class SongServiceBuilder {

    private NextSongService nextSongService;

    public SongServiceBuilder(NextSongService nextSongService) {
        this.nextSongService = nextSongService;
    }

    @SafeVarargs
    public final SongServiceBuilder withDecorators(Class<? extends NSSDecorator>... decorators) {
        return withDecorators(Arrays.asList(decorators));
    }

    public SongServiceBuilder withDecorators(List<Class<? extends NSSDecorator>> decorators) {
        for (Class<? extends NSSDecorator> decoratorClass : decorators) {
            try {
                nextSongService = decoratorClass.getDeclaredConstructor(NextSongService.class).newInstance(nextSongService);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    public NextSongService build() {
        return nextSongService;
    }
}
