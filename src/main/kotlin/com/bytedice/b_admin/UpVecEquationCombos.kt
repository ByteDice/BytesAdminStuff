package com.bytedice.b_admin

import net.minecraft.util.math.Vec3d
import org.joml.Vector2d
import kotlin.math.cos
import kotlin.math.sin

fun getUpVecByEquationIndex(i: Int, rot: Vector2d): Vec3d {

  val radians = Vector2d(-rot.x, rot.y)

  when (i) {
    1 -> return Vec3d(sin(radians.y), cos(radians.x), cos(radians.y) * sin(radians.x))
    2 -> return Vec3d(-sin(radians.y), cos(radians.x), cos(radians.y) * sin(radians.x))
    3 -> return Vec3d(sin(radians.y), -cos(radians.x), cos(radians.y) * sin(radians.x))
    4 -> return Vec3d(-sin(radians.y), -cos(radians.x), cos(radians.y) * sin(radians.x))
    5 -> return Vec3d(sin(radians.y), cos(radians.x), -cos(radians.y) * sin(radians.x))
    6 -> return Vec3d(-sin(radians.y), cos(radians.x), -cos(radians.y) * sin(radians.x))
    7 -> return Vec3d(sin(radians.y), -cos(radians.x), -cos(radians.y) * sin(radians.x))
    8 -> return Vec3d(-sin(radians.y), -cos(radians.x), -cos(radians.y) * sin(radians.x))
    9 -> return Vec3d(sin(radians.y), cos(radians.x), cos(radians.y) * -sin(radians.x))
    10 -> return Vec3d(-sin(radians.y), cos(radians.x), cos(radians.y) * -sin(radians.x))
    11 -> return Vec3d(sin(radians.y), -cos(radians.x), cos(radians.y) * -sin(radians.x))
    12 -> return Vec3d(-sin(radians.y), -cos(radians.x), cos(radians.y) * -sin(radians.x))
    13 -> return Vec3d(sin(radians.y), cos(radians.x), -cos(radians.y) * -sin(radians.x))
    14 -> return Vec3d(-sin(radians.y), cos(radians.x), -cos(radians.y) * -sin(radians.x))
    15 -> return Vec3d(sin(radians.y), -cos(radians.x), -cos(radians.y) * -sin(radians.x))
    16 -> return Vec3d(-sin(radians.y), -cos(radians.x), -cos(radians.y) * -sin(radians.x))
    else -> throw IllegalArgumentException("Index out of bounds")
  }
}