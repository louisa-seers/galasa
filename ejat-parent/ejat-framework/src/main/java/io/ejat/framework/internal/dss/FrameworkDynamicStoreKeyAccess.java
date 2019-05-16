package io.ejat.framework.internal.dss;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import io.ejat.framework.spi.DynamicStatusStoreException;
import io.ejat.framework.spi.IDynamicStatusStore;
import io.ejat.framework.spi.IDynamicStatusStoreKeyAccess;


/**
 *  Provides the common key access to the DSS
 * 
 * @author Bruce Abbott
 */

public class FrameworkDynamicStoreKeyAccess implements IDynamicStatusStoreKeyAccess {
	private final IDynamicStatusStore 		 dssStore;
	private final String                     prefix;
	
	public FrameworkDynamicStoreKeyAccess(IDynamicStatusStore dssStore, String prefix) {
        Objects.requireNonNull(dssStore);
        Objects.requireNonNull(prefix);

        this.dssStore = dssStore;
        this.prefix = prefix;
    }
	
    protected IDynamicStatusStore getDssStore() {
		return this.dssStore;
	}



	/*
     * (non-Javadoc)
     * 
     * @see io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#put(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void put(@NotNull String key, @NotNull String value) throws DynamicStatusStoreException {
        this.dssStore.put(prefixKey(key), value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#put(java.util.Map)
     */
    @Override
    public void put(@NotNull Map<String, String> keyValues) throws DynamicStatusStoreException {
        Objects.requireNonNull(keyValues);

        // *** Copy all the keys and prefix them
        final HashMap<String, String> newKeyValues = new HashMap<>();
        for (final Entry<String, String> entry : keyValues.entrySet()) {
            final String oKey = entry.getKey();
            final String oValue = entry.getValue();

            Objects.requireNonNull(oKey);
            Objects.requireNonNull(oValue);

            newKeyValues.put(prefixKey(oKey), oValue);
        }

        this.dssStore.put(newKeyValues);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#putSwap(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public boolean putSwap(@NotNull String key, String oldValue, @NotNull String newValue)
            throws DynamicStatusStoreException {
        Objects.requireNonNull(newValue);
        return this.dssStore.putSwap(prefixKey(key), oldValue, newValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#putSwap(java.lang.String,
     * java.lang.String, java.lang.String, java.util.Map)
     */
    @Override
    public boolean putSwap(@NotNull String key, String oldValue, @NotNull String newValue,
            @NotNull Map<String, String> others) throws DynamicStatusStoreException {
        Objects.requireNonNull(newValue);
        Objects.requireNonNull(others);

        // *** Copy all the other keys and prefix them
        final HashMap<String, String> newOthers = new HashMap<>();
        for (final Entry<String, String> entry : others.entrySet()) {
            final String oKey = entry.getKey();
            final String oValue = entry.getValue();

            Objects.requireNonNull(oKey);
            Objects.requireNonNull(oValue);

            newOthers.put(prefixKey(oKey), oValue);
        }

        return this.dssStore.putSwap(prefixKey(key), oldValue, newValue, newOthers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#get(java.lang.String)
     */
    @Override
    public @Null String get(@NotNull String key) throws DynamicStatusStoreException {
        return this.dssStore.get(prefixKey(key));
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#getPrefix(java.lang.
     * String)
     */
    @Override
    public @NotNull Map<String, String> getPrefix(@NotNull String keyPrefix) throws DynamicStatusStoreException {
        final Map<String, String> gotSet = this.dssStore.getPrefix(prefixKey(keyPrefix));
        final HashMap<String, String> returnSet = new HashMap<>();

        for (Entry<String, String> entry : gotSet.entrySet()) {
            String key   = entry.getKey();
            final String value = entry.getValue();

            if (key.startsWith(this.prefix)) {
                key = key.substring(this.prefix.length());
                returnSet.put(key, value);
            } else {
                throw new DynamicStatusStoreException("Somehow we got keys with the wrong prefix");
            }
        }

        return returnSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#delete(java.lang.String)
     */
    @Override
    public void delete(@NotNull String key) throws DynamicStatusStoreException {
        this.dssStore.delete(prefixKey(key));
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#delete(java.util.Set)
     */
    @Override
    public void delete(@NotNull Set<String> keys) throws DynamicStatusStoreException {
        Objects.requireNonNull(keys);

        // *** Copy all the keys and prefix them
        final HashSet<String> newKeys = new HashSet<>();
        for (final String key : keys) {
            Objects.requireNonNull(key);
            newKeys.add(prefixKey(key));
        }

        this.dssStore.delete(newKeys);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.ejat.framework.spi.IDynamicStatusStoreKeyAccess#deletePrefix(java.lang.
     * String)
     */
    @Override
    public void deletePrefix(@NotNull String keyPrefix) throws DynamicStatusStoreException {
        this.dssStore.deletePrefix(prefixKey(keyPrefix));
    }
    
    /**
     * Prefix the supplied key with the namespace
     *
     * @param key
     * @return
     */
    private String prefixKey(String key) {
        Objects.requireNonNull(key);
        return this.prefix + key;
    }
}
