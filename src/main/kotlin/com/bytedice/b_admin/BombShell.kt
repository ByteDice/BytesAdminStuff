package com.bytedice.b_admin

import net.minecraft.entity.decoration.DisplayEntity.BlockDisplayEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtFloat
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtType
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f


class BombShell {
  private val parts: Array<DisplayEntityProperties> = arrayOf(
    DisplayEntityProperties(
        Vector3f(-0.438f, -3.75f, -0.438f),
        Vector4f(0.0f, 0.0f, 0.0f, 1.0f),
        Vector3f(0.875f, 7.5f, 0.875f),
        "minecraft:gray_concrete"
    ),
    DisplayEntityProperties(
        Vector3f(-0.313f, -4.6f, -0.313f),
        Vector4f(0.0f,  0.0f, 0.0f, 1.0f),
        Vector3f(0.625f, 0.875f, 0.625f),
        "minecraft:gray_concrete"
    ),
    DisplayEntityProperties(
        Vector3f(0.188f, 2.091f, -0.45f),
        Vector4f(-0.153f,  -0.69f, 0.153f, 0.69f),
        Vector3f(0.875f, 2.0f, 0.375f),
        "minecraft:gray_concrete"
    ),
    DisplayEntityProperties(
        Vector3f(-0.45f, 2.091f, -0.188f),
        Vector4f(0.0f, 0.0f, 0.216f, 0.976f),
        Vector3f(0.875f, 2.0f, 0.375f),
        "minecraft:gray_concrete"
    ),
    DisplayEntityProperties(
        Vector3f(-0.188f, 2.091f, 0.45f),
        Vector4f(0.153f,  0.69f, 0.153f, 0.69f),
        Vector3f(0.875f, 2.0f, 0.375f),
        "minecraft:gray_concrete"
    ),
    DisplayEntityProperties(
        Vector3f(0.45f, 2.091f, 0.188f),
        Vector4f(0.216f, 0.976f, 0.0f, 0.0f),
        Vector3f(0.875f, 2.0f, 0.375f),
        "minecraft:gray_concrete"
    ),
  )

  private var bombShellDisplayEntities: Array<BlockDisplayEntity> = emptyArray()
  private var ticksUntilImpact: Int = 100
  private var speed: Double = 0.1


  fun spawn(server: ServerWorld, pos: Vec3d, rot: Vector2f) {
    for (part in parts) {
      val upVec = rotToUpVec(Vector2d(rot.x.toDouble(), rot.y.toDouble()))

      val newOffset = Vec3d(
        part.offset.x + (upVec.x * (speed * ticksUntilImpact)),
        part.offset.y + (upVec.y * (speed * ticksUntilImpact)),
        part.offset.z + (upVec.z * (speed * ticksUntilImpact))
      )

      // part.offset = Vector3f(newOffset.x.toFloat(), newOffset.y.toFloat(), newOffset.z.toFloat())

      bombShellDisplayEntities += spawnBlockDisplay(server, pos, rot, part)
    }
  }

  fun tick() {
    if (bombShellDisplayEntities.isEmpty()) {
      return
    }

    // TODO: make this only repeat ticksUntilImpact times

    for (displayEntity in bombShellDisplayEntities) {
      val upVecNorm = rotToUpVec(
        Vector2d(
          displayEntity.rotationClient.x.toDouble(),
          displayEntity.rotationClient.y.toDouble()
        )
      )

      val newPos = Vec3d(
        upVecNorm.x * speed,
        upVecNorm.y * speed,
        upVecNorm.z * speed
      )

      val nbt = NbtCompound().apply {
        displayEntity.writeNbt(this)
      }

      val transformationNbt = nbt.getCompound("transformation")
      val translationList = transformationNbt.getList("translation", NbtElement.FLOAT_TYPE.toInt())

      val currentPos = Vector3f(
        translationList.getFloat(0),
        translationList.getFloat(1),
        translationList.getFloat(2)
      )

      val offsetList = NbtList().apply {
        add(NbtFloat.of(currentPos.x + newPos.x.toFloat()))
        add(NbtFloat.of(currentPos.y + newPos.y.toFloat()))
        add(NbtFloat.of(currentPos.z + newPos.z.toFloat()))
      }

      transformationNbt.put("translation", offsetList)
      nbt.put("transformation", transformationNbt)
      displayEntity.readNbt(nbt)

    }
  }
}