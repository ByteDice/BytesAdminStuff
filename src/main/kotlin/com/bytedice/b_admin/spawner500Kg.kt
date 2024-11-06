package com.bytedice.b_admin

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import java.awt.Color

fun spawner500Kg() : ItemStack {
  val spawnerItem: ItemStack = ItemStack(Items.AMETHYST_SHARD)

  val nbt: NbtCompound = NbtCompound()
  nbt.putByte("500kg", 1)
  val component: NbtComponent = NbtComponent.of(nbt)

  val displayName = Text.literal("500kg bomb")
    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(Color(255, 0, 0).rgb)))

  spawnerItem.set(DataComponentTypes.ITEM_NAME, displayName)
  spawnerItem.set(DataComponentTypes.CUSTOM_DATA, component)

  return spawnerItem
}