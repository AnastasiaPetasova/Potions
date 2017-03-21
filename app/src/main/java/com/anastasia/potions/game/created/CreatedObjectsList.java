package com.anastasia.potions.game.created;

import com.anastasia.potions.game.creating.RecipeCreatingConstants;

import java.util.ArrayList;
import java.util.List;

public class CreatedObjectsList implements RecipeCreatingConstants {

    private final List<CreatedObject> createdObjects;

    public CreatedObjectsList() {
        this.createdObjects = new ArrayList<>();
    }

    public List<CreatedObject> toList() {
        return createdObjects;
    }

    public void add(CreatedObject createdObject) {
        createdObjects.add(createdObject);
    }

    public CreatedObject get(int index) {
        return createdObjects.get(index);
    }

    public void remove(CreatedObject createdObject) {
        createdObjects.remove(createdObject);
    }
}
