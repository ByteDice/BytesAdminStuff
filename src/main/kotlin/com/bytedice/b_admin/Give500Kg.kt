package com.bytedice.b_admin

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object Give500Kg {
  fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
    dispatcher.register(
      CommandManager.literal("give500Kg")
        .executes { context ->
          context.source.sendFeedback({ Text.literal("Gave you 500Kg bomb. Don\'t get too silly with it :3") }, false)
          context.source.player?.inventory?.insertStack(spawner500Kg())
          Command.SINGLE_SUCCESS
        }
    )
  }
}