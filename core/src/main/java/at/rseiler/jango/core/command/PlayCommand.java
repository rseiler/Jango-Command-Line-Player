package at.rseiler.jango.core.command;

import at.rseiler.jango.core.song.SongData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlayCommand extends Command {
    private SongData songData;
    private double time;
}