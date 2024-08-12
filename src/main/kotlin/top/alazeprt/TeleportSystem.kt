package top.alazeprt

import org.bukkit.Bukkit
import org.bukkit.Location
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import top.alazeprt.settings.HeightTeleport
import top.alazeprt.settings.SphereTeleport

object TeleportSystem : Plugin() {

    @Config("settings/sphere.yml")
    lateinit var sphereConfig: ConfigFile

    @Config("settings/height.yml")
    lateinit var heightConfig: ConfigFile

    private val heightConfList = mutableListOf<HeightTeleport>()

    private val sphereConfList = mutableListOf<SphereTeleport>()

    override fun onEnable() {
        heightConfig.getKeys(false).forEach {
            val section = heightConfig.getConfigurationSection(it) ?: return@forEach
            val from = Bukkit.getWorld(section.getString("from") ?: return@forEach)
            val height = section.getDouble("height")
            val to = Location(
                Bukkit.getWorld(section.getString("to.world") ?: return@forEach),
                section.getDouble("to.x"), section.getDouble("to.y"), section.getDouble("to.z")
            )
            val ht = HeightTeleport(it, from ?: return@forEach, height, to)
            heightConfList.add(ht)
        }
        sphereConfig.getKeys(false).forEach {
            val section = sphereConfig.getConfigurationSection(it) ?: return@forEach
            val from = Location(
                Bukkit.getWorld(section.getString("from.world") ?: return@forEach),
                section.getDouble("from.x"), section.getDouble("from.y"), section.getDouble("from.z")
            )
            val radius = section.getLong("radius")
            val to = Location(
                Bukkit.getWorld(section.getString("to.world") ?: return@forEach),
                section.getDouble("to.x"), section.getDouble("to.y"), section.getDouble("to.z")
            )
            val st = SphereTeleport(it, from, radius, to)
            sphereConfList.add(st)
        }
    }

    override fun onActive() {
        submit(period = 5) {
            Bukkit.getOnlinePlayers().forEach { player ->
                heightConfList.forEach {
                    it.handle(player)
                }
                sphereConfList.forEach {
                    it.handle(player)
                }
            }
        }
    }
}