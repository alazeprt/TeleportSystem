package top.alazeprt.settings

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

class HeightTeleport(val name: String, val from: World, val height: Double, val to: Location) {

    fun handle(player: Player) {
        if(player.world == from && player.location.y >= height) {
            player.teleport(to)
        }
    }
}