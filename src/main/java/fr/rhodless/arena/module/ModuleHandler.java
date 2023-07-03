package fr.rhodless.arena.module;

import fr.rhodless.arena.Arena;
import lombok.Getter;

@Getter
public class ModuleHandler {
    private final Arena arena;

    private Module module;

    public ModuleHandler(Arena arena) {
        this.arena = arena;
    }

    public void registerModule(Module module) {
        if (this.module != null) {
            throw new UnsupportedOperationException("You can't register more than one module");
        }

        this.module = module;
    }
}
