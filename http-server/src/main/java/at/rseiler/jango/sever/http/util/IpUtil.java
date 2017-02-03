package at.rseiler.jango.sever.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public final class IpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);
    private static final String LOCAL_IP = lookupLocalIp();

    public static String getLocalIp() {
        return LOCAL_IP;
    }

    private static String lookupLocalIp() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
                    if (f.getAddress().isSiteLocalAddress()) {
                        return f.getAddress().getHostAddress();
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to lookup local IP", e);
        }

        return null;
    }

    private IpUtil() {
    }
}
