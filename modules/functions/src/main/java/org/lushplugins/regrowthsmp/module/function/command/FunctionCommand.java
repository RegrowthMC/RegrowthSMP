package org.lushplugins.regrowthsmp.module.function.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.command.Command;
import org.lushplugins.lushlib.libraries.chatcolor.ChatColorHandler;
import org.lushplugins.regrowthsmp.module.function.Functions;

import java.util.List;

public class FunctionCommand extends Command {

    public FunctionCommand() {
        super("function");
        addRequiredPermission("regrowthsmp.functions.run");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        if (args.length == 0) {
            sender.sendMessage("Incorrect usage");
            return true;
        }

        String functionName = args[0];
        Functions.getInstance().getFunctionManager().getFunction(functionName).ifPresentOrElse(
            (function) -> {
                function.run();
                ChatColorHandler.sendMessage(sender, String.format("&#b7faa2Successfully ran function &#66b04f%s", functionName));
            },
            () -> ChatColorHandler.sendMessage(sender, String.format("&#ff6969Function &#d13636%s &#ff6969does not exist", functionName))
        );
        return true;
    }

    @Override
    public @Nullable List<String> tabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args, @NotNull String[] fullArgs) {
        return args.length == 1 ? Functions.getInstance().getFunctionManager().getFunctionNames().stream().toList() : null;
    }
}
