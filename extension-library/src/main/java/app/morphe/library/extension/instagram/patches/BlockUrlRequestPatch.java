package app.morphe.library.extension.instagram.patches;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public final class BlockUrlRequestPatch {
    private static final List<String> BLOCKED_URLS;

    // Populated at patch time via addBlockedUrl()
    static {
        BLOCKED_URLS = new ArrayList<>();
    }

    public static void addBlockedUrl(String urlSubstring) {
        BLOCKED_URLS.add(urlSubstring);
    }

    // Injection point — called at the start of makeTigonRequest()
    public static void checkAndBlockUrl(String uriString) throws IOException {
        if (uriString == null) return;

        for (String blocked : BLOCKED_URLS) {
            if (uriString.contains(blocked)) {
                Logger.printInfo(() -> "Blocking request to: " + uriString);
                throw new IOException("BlockUrlRequestPatch: blocked request to: " + uriString);
            }
        }
    }
}
