package bikeshare;

import java.io.IOException;

public interface RideStore {
    void append(Ride ride) throws IOException;
}
