package com.inza.demonaddon.action.demonview.client;

public class DemonViewClientState {
    public static boolean enabled = false;
    public static boolean retracting = false;
    public static java.util.UUID casterUuid = null;
    public static float maxRadius = 0.0F;
    public static float currentRadius = 0.0F;
    public static long lastUpdateGameTime = 0L;

    private DemonViewClientState() {}
}
