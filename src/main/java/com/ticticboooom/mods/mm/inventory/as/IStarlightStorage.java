package com.ticticboooom.mods.mm.inventory.as;

public interface IStarlightStorage {
    /**
     * Adds starlight to the storage. Returns quantity of starlight that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of starlight to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of starlight that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveStarlight(int maxReceive, boolean simulate);

    /**
     * Removes starlight from the storage. Returns quantity of starlight that was removed.
     *
     * @param maxExtract
     *            Maximum amount of starlight to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of starlight that was (or would have been, if simulated) extracted from the storage.
     */
    int extractStarlight(int maxExtract, boolean simulate);

    /**
     * Returns the amount of starlight currently stored.
     */
    int getStarlightStored();

    /**
     * Returns the maximum amount of starlight that can be stored.
     */
    int getMaxStarlightStored();

    /**
     * Returns if this storage can have starlight extracted.
     * If this is false, then any calls to extractStarlight will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive starlight.
     * If this is false, then any calls to receivestarlight will return 0.
     */
    boolean canReceive();
}
