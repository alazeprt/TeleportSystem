package top.alazeprt.settings

import org.bukkit.Location
import org.bukkit.entity.Player

class SphereTeleport(val name: String, val from: Location, val radius: Long, val to: Location) {

    companion object {
        private fun isLocationInSphere(location: Location, center: Location, radius: Long) =
            location.toVector().subtract(center.toVector()).lengthSquared() <= radius * radius
    }

    fun handle(player: Player) {
        if(isLocationInSphere(player.location, from, radius)) {
            player.teleport(to)
        }
    }
}