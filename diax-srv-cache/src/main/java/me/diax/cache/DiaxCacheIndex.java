package me.diax.cache;

/**
 * Static list of indices used by the cache
 */
public enum DiaxCacheIndex {
    PROFILE(1);

    private final int index;

    /**
     * Creates a new cache index page
     *
     * @param index the index
     * @throws IllegalArgumentException if the index is negative
     */
    DiaxCacheIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index cannot be negative: " + index);
        }
        this.index = index;
    }

    /**
     * Gets the index if this page
     *
     * @return the inde
     */
    public int getIndex() {
        return index;
    }
}
