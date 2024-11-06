package com.bytedice.b_admin

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.DisplayEntity.BlockDisplayEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtList
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import kotlin.math.cos
import kotlin.math.sin


// TODO: fix bomb movement direction


var allBombs: Array<BombShell> = emptyArray()

class BytesAdminStuff : DedicatedServerModInitializer {
  override fun onInitializeServer() {

    ServerTickEvents.START_SERVER_TICK.register { _ ->
      tick()
    }

    CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
      Give500Kg.register(dispatcher)
    }

    UseItemCallback.EVENT.register(UseItemCallback { player: PlayerEntity, world: World, hand: Hand ->
      onHandItemUse(player, world, hand)
    })

    ServerLifecycleEvents.SERVER_STARTED.register(ServerLifecycleEvents.ServerStarted { server: MinecraftServer ->

      server.playerManager.broadcast(Text.of("BDAT - Loaded"), false)
      print("BDAT - Loaded")
    })
  }
}


data class DisplayEntityProperties(
  var offset:  Vector3f,
  val leftRot: Vector4f,
  val scale:   Vector3f,
  val block:   String
)


fun spawnBlockDisplay(server: ServerWorld, worldPosition: Vec3d, worldRotation: Vector2f, properties: DisplayEntityProperties): BlockDisplayEntity {
  val blockDisplay = BlockDisplayEntity(EntityType.BLOCK_DISPLAY, server)

  val nbt = NbtCompound().apply {
    put("block_state", NbtCompound().apply {
      putString("Name", properties.block)
    })
    put("transformation", NbtCompound().apply {
      val offsetList = NbtList().apply {
        add(NbtFloat.of(properties.offset.x))
        add(NbtFloat.of(properties.offset.y))
        add(NbtFloat.of(properties.offset.z))
      }
      val rotList = NbtList().apply {
        add(NbtFloat.of(properties.leftRot.x))
        add(NbtFloat.of(properties.leftRot.y))
        add(NbtFloat.of(properties.leftRot.z))
        add(NbtFloat.of(properties.leftRot.w))
      }
      val scaleList = NbtList().apply {
        add(NbtFloat.of(properties.scale.x))
        add(NbtFloat.of(properties.scale.y))
        add(NbtFloat.of(properties.scale.z))
      }
      val rightRotList = NbtList().apply {
        add(NbtFloat.of(0.0f))
        add(NbtFloat.of(0.0f))
        add(NbtFloat.of(0.0f))
        add(NbtFloat.of(1.0f))
      }

      put("translation",    offsetList)
      put("left_rotation",  rotList)
      put("scale",          scaleList)
      put("right_rotation", rightRotList)
    })
  }

  blockDisplay.readNbt(nbt)

  blockDisplay.refreshPositionAndAngles(worldPosition.x, worldPosition.y, worldPosition.z, worldRotation.x, worldRotation.y)
  server.spawnEntity(blockDisplay)

  return blockDisplay
}


fun raycastFromPlayer(player: ServerPlayerEntity, maxDistance: Double): HitResult? {
  val world: World = player.world
  val eyePos: Vec3d = player.getCameraPosVec(1.0f)
  val lookVec: Vec3d = player.getRotationVec(1.0f)
  val targetPos: Vec3d = eyePos.add(lookVec.multiply(maxDistance))

  val blockHitResult = world.raycast(
    RaycastContext(
      eyePos,
      targetPos,
      RaycastContext.ShapeType.OUTLINE,
      RaycastContext.FluidHandling.NONE,
      player
    )
  )

  return if (blockHitResult.type == HitResult.Type.BLOCK) {
    blockHitResult
  } else {
    null
  }
}


fun rotToUpVec(rotDegrees: Vector2d): Vec3d {
  val radians = Vector2d(
    Math.toRadians(rotDegrees.x),
    Math.toRadians(rotDegrees.y)
  )

  val upVec = Vec3d(
    -sin(radians.y),
    cos(radians.x),
    cos(radians.y) * sin(radians.x)
  )

  return upVec.normalize()
}


fun addBombToArray(bomb: BombShell) {
  allBombs += bomb
}


fun tick() {
  for (bomb in allBombs) {
    bomb.tick()
  }
}