package net.rawsome1234.fantasycoffee.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

public class Piss {
    public Piss(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("piss").executes((command) -> {
            return pissMessage(command.getSource().source);
        }));
    }

    private int pissMessage(CommandSource source) throws CommandSyntaxException{
//        ServerPlayerEntity player = source.
        source.sendSystemMessage(Component.empty().append(""));
        return 1;
    }

}
