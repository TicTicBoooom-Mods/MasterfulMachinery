package com.ticticboooom.mods.mm.inventory.botania;

public interface IManaStorage {
    /**
     * Adds Mana to the storage. Returns quantity of Mana that was accepted.
     *
     * @param maxReceive
     *            Maximum amount of Mana to be inserted.
     * @param simulate
     *            If TRUE, the insertion will only be simulated.
     * @return Amount of Mana that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveMana(int maxReceive, boolean simulate);

    /**
     * Removes Mana from the storage. Returns quantity of Mana that was removed.
     *
     * @param maxExtract
     *            Maximum amount of Mana to be extracted.
     * @param simulate
     *            If TRUE, the extraction will only be simulated.
     * @return Amount of Mana that was (or would have been, if simulated) extracted from the storage.
     */
    int extractMana(int maxExtract, boolean simulate);

    /**
     * Returns the amount of Mana currently stored.
     */
    int getManaStored();

    /**
     * Returns the maximum amount of Mana that can be stored.
     */
    int getMaxManaStored();

    /**
     * Returns if this storage can have Mana extracted.
     * If this is false, then any calls to extractMana will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive Mana.
     * If this is false, then any calls to receiveMana will return 0.
     */
    boolean canReceive();
}
