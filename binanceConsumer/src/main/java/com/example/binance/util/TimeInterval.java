package com.example.binance.util;

import java.util.HashMap;
import java.util.Map;

public enum TimeInterval {
    // m -> minutes; h -> hours; d -> days; w -> weeks; M -> months
    _1m("1m", 1),
    _3m("3m", 3),
    _5m("5m", 5),
    _15m("15m", 15),
    _30m("30m", 30),
    _1h("1h", 60),
    _2h("2h", 120),
    _4h("4h", 240),
    _6h("6h", 360),
    _8h("8h", 480),
    _12h("12h", 720),
    _1d("1d", 1440),
    _3d("3d", 4320),
    _1w("1w", 10080),
    _1M("1M", 43800);
    private final String interval;
    private final Integer minutes;

    private static final Map<String, TimeInterval> BY_INTERVAL = new HashMap<>();
    private static final Map<Integer, TimeInterval> BY_MINUTES = new HashMap<>();

    static {
        for (TimeInterval t : values()) {
            BY_INTERVAL.put(t.interval, t);
            BY_MINUTES.put(t.minutes, t);
        }
    }

    TimeInterval(String interval, Integer minutes) {
        this.interval = interval;
        this.minutes = minutes;
    }

    public String getInterval() {
        return interval;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public static TimeInterval valueOfInterval(String interval) {
        return BY_INTERVAL.get(interval);
    }

    public static TimeInterval valueOfMinutes(int minutes) {
        return BY_INTERVAL.get(minutes);
    }
}
