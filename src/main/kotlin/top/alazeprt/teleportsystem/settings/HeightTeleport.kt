package top.alazeprt.teleportsystem.settings

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

class HeightTeleport(val name: String, private val from: World, private val height: Double, private val to: Location) {

    fun handle(player: Player) {
        if(player.world == from && player.location.y >= height) {
            player.teleport(to)
        }
    }
}