package com.example.felipetorres.mapboxspike;


public final class AnimationValues {
    public static final String ALPHA = "alpha";
    public static final String ROTATION = "rotation";
    public static final String TRANSLATION_X = "translationX";
    public static final String TRANSLATION_Y = "translationY";
    public static final String PEEK_HEIGHT = "peekHeight";
    public static final String HEIGHT = "height";
    public static final String WIDTH = "width";
    public static final boolean FADE_IN = true;
    public static final boolean FADE_OUT = false;
    public static final int ZERO_INT = 0;
    public static final float ZERO_FLOAT = 0.0f;
    public static final float ONE_FLOAT = 1.0f;
    public static final float GRADES_180_FLOAT = 180.0f;
    public static final float MULTIPLIER_QUARTER = 0.25f;
    public static final float TWENTY_FIVE_FLOAT = 25.0f;
    public static final float FOURTY_FLOAT = 40.0f;
    public static final float ANTICIPATE_TENSION = 0.25f;
    public static final int ONE_INT = 1;
    public static final int ANIMATION_QUICK_FADE_OUT_DURATION = 150;
    private static int normalDuration = 0;
    private static int fastDuration = 0;
    private static int slowDuration = 0;
    private static int voyagePickerDuration = 0;
    private static int canonAnimationMaxDuration = 0;
    private static int canonAnimationDelta = 200;

    private AnimationValues() {
        // Nothing
    }

    public static int getNormalDuration() {
        return normalDuration;
    }

    public static int getFastDuration() {
        return fastDuration;
    }

    public static int getSlowDuration() {
        return slowDuration;
    }

    public static int getVoyagePickerDuration() {
        return voyagePickerDuration;
    }

    public static int getCanonAnimationMaxDuration() {
        return canonAnimationMaxDuration;
    }

    public static int getCanonAnimationDelta() {
        return canonAnimationDelta;
    }
}
