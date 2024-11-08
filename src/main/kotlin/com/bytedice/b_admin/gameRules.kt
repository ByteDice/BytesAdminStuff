package com.bytedice.b_admin

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.IntRule

class gameRules() {
  val upVecEquationIndex: GameRules.Key<IntRule> =
    GameRuleRegistry.register("upVecEquationIndex", GameRules.Category.MISC, GameRuleFactory.createIntRule(1, 1))
}