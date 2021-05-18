package com.ticticboooom.mods.mm.data.model.structure;

import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockedNamedTag<T> implements ITag.INamedTag<T> {
    private final ResourceLocation name;

    public MockedNamedTag(ResourceLocation name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public boolean contains(T element) {
        return false;
    }

    @Override
    public List<T> getAllElements() {
        return new ArrayList<>();
    }

    @Override
    public T getRandomElement(Random random) {
        return INamedTag.super.getRandomElement(random);
    }
}
