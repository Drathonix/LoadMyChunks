package com.vicious.loadmychunks.common.util;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
//? if >1.18.2 {
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
//?}
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
//? if <1.18.3
/*import net.minecraft.network.chat.TranslatableComponent;*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BoolArgument implements ArgumentType<Boolean> {
    //? if <1.18.3 {
    /*private static final Dynamic2CommandExceptionType INVALID_BOOL = new Dynamic2CommandExceptionType(
            (found, constants) -> new TranslatableComponent("commands.loadmychunks.arguments.bool.invalid", constants, found));
    *///?}
    //? if >1.18.2 {
    private static final Dynamic2CommandExceptionType INVALID_BOOL = new Dynamic2CommandExceptionType(
            (found, constants) -> Component.translatable("commands.loadmychunks.arguments.bool.invalid", constants, found));
    //?}

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

    //? if >1.18.2 {
    public static class Info implements ArgumentTypeInfo<BoolArgument, Info.Template>
    {
        @Override
        public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {}

        @SuppressWarnings("unchecked")
        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf buffer)
        {
            return new Template();
        }

        @Override
        public void serializeToJson(Template template, JsonObject json) {}

        @Override
        public Template unpack(BoolArgument argument)
        {
            return new Template();
        }

        public class Template implements ArgumentTypeInfo.Template<BoolArgument>
        {
            Template() {}

            @Override
            public BoolArgument instantiate(CommandBuildContext p_223435_)
            {
                return new BoolArgument();
            }

            @Override
            public ArgumentTypeInfo<BoolArgument, ?> type()
            {
                return Info.this;
            }
        }
    }
    //?}
}