package top.alazeprt.teleportsystem

import org.bukkit.Bukkit
import org.bukkit.Location
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import top.alazeprt.teleportsystem.settings.HeightTeleport
import top.alazeprt.teleportsystem.settings.SphereTeleport

object TeleportSystem : Plugin() {

    @Config("settings/sphere.yml", migrate = true)
    lateinit var sphereConfig: ConfigFile

    @Config("settings/height.yml", migrate = true)
    lateinit var heightConfig: ConfigFile

    private val heightConfList = mutableListOf<HeightTeleport>()

    private val sphereConfList = mutableListOf<SphereTeleport>()

    override fun onActive() {
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
            val toWorld = Bukkit.getWorld(section.getString("to.world") ?: return@forEach)?: return@forEach
            val toHeight = section.getDouble("to.y")
            val st = SphereTeleport(it, from, radius, toWorld, toHeight)
            sphereConfList.add(st)
        }
        submit(period = 1) {
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