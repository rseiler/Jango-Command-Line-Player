package at.rseiler.jango.core.util;

import at.rseiler.jango.core.command.StationCommand;
import at.rseiler.jango.core.util.ObjectMapperUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static at.rseiler.jango.core.fortest.CommandUtil.JSON_STATION;
import static at.rseiler.jango.core.fortest.CommandUtil.createStationCommand;
import static org.hamcrest.core.Is.is;

public class ObjectMapperUtilTest {

    @Test
    public void read() throws Exception {
        StationCommand stationCommand = ObjectMapperUtil.read(JSON_STATION, StationCommand.class);

        Assert.assertThat(stationCommand, is(createStationCommand()));
    }

    @Test
    public void write() throws Exception {
        byte[] stationBytes = ObjectMapperUtil.write(createStationCommand());

        Assert.assertThat(new String(stationBytes, StandardCharsets.UTF_8), is(JSON_STATION));
    }

}