package com.bytedice.b_admin

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import org.joml.Vector2f


fun onHandItemUse(player: PlayerEntity, world: World, hand: Hand): TypedActionResult<ItemStack> {
  val handItem: ItemStack = player.getStackInHand(hand)

  if (player is ServerPlayerEntity && world is ServerWorld) {
    player.sendMessage(Text.literal("Item used: ${handItem.item.name.string}"), false)

    if (ItemStack.areItemsEqual(handItem, spawner500Kg())) {
      spawnBomb(player, world)
    }
  }

  return TypedActionResult(ActionResult.PASS, handItem)
}


fun spawnBomb(player: ServerPlayerEntity, world: ServerWorld) {
  val hitResult = raycastFromPlayer(player, 100.0) ?: return

  val bomb = BombShell()

  /*
  val dir = Vector2f(
    Random.create().nextBetween(0, 360).toFloat(),
    30.0f
  )
  */

  val dir = Vector2f(60.0f, 30.0f)

  bomb.spawn(world, hitResult.pos, dir)

  addBombToArray(bomb)
}