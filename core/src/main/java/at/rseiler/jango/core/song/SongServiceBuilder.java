package at.rseiler.jango.core.song;


import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

public class SongServiceBuilder {

    private NextSongService nextSongService;

    public SongServiceBuilder(NextSongService nextSongService) {
        this.nextSongService = nextSongService;
    }

    public SongServiceBuilder withDecorators(List<Class<? extends NextSongServiceDecorator>> decorators) {
        Collections.reverse(decorators);

        for (Class<? extends NextSongServiceDecorator> decoratorClass : decorators) {
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
