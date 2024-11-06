package com.bytedice.b_admin

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object config500Kg {
  fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
    dispatcher.register(
      CommandManager.literal("config500Kg")
        .executes { context ->
          context.source.sendFeedback({ Text.literal("WIP") }, false)
          Command.SINGLE_SUCCESS
        }
    )
  }
}