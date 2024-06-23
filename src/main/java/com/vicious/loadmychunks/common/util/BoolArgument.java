package com.vicious.loadmychunks.common.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Copy of the forge Enum Argument
 */
public class BoolArgument implements ArgumentType<Boolean> {
    private static final Dynamic2CommandExceptionType INVALID_BOOL = new Dynamic2CommandExceptionType(
            (found, constants) -> new TranslatableComponent("commands.loadmychunks.arguments.bool.invalid", constants, found));

    public static BoolArgument boolArgument() {
        return new BoolArgument();
    }

    @Override
    public Boolean parse(final StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        try {
            return Boolean.parseBoolean(name) || name.equalsIgnoreCase("ON");
        } catch (Throwable e) {
            throw INVALID_BOOL.createWithContext(reader, name, getExamples().toString());
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(getExamples(), builder);
    }

    private static final List<String> examples = new ArrayList<>();
    static {
        examples.add("TRUE");
        examples.add("FALSE");
        examples.add("ON");
        examples.add("OFF");
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}