package at.rseiler.jango.core.song;

import at.rseiler.jango.core.service.decorator.DecoratorService;
import at.rseiler.jango.core.service.decorator.OpDec;

import java.util.List;

public class NSSWithDecorators extends DecoratorService<SongData, NSSGrabber, OpDec<SongData>> implements NextSongService {

    public NSSWithDecorators(NSSGrabber nssGrabber, List<OpDec<SongData>> decorators) {
        super(nssGrabber);
        add(decorators);
    }

    @Override
    public SongData getNextSong() {
        return exec();
    }
}
