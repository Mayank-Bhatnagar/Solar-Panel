package com.setup.solarpanel;


/**
 * Interface that provides trusted time information, possibly coming from an NTP
 * server. Implementations may cache answers until {@link #forceRefresh()}.
 *
 * @hide
 */
public interface TrustedTime {
    /**
     * Force update with an external trusted time source, returning {@code true}
     * when successful.
     */
    public boolean forceRefresh();
    /**
     * Check if this instance has cached a response from a trusted time source.
     */
    public boolean hasCache();
    /**
     * Return time since last trusted time source contact, or
     * {@link Long#MAX_VALUE} if never contacted.
     */
    public long getCacheAge();
    /**
     * Return certainty of cached trusted time in milliseconds, or
     * {@link Long#MAX_VALUE} if never contacted. Smaller values are more
     * precise.
     */
    public long getCacheCertainty();
    /**
     * Return current time similar to {@link System#currentTimeMillis()},
     * possibly using a cached authoritative time source.
     */
    public long currentTimeMillis();
}