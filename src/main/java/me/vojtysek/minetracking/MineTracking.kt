import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin

class OreCounter : JavaPlugin(), Listener {
    private val oreCounts = HashMap<String, HashMap<Material, Int>>()

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player: Player = event.player
        val block = event.block

        if (block.type.isOre) {
            val playerName = player.name
            val oreType = block.type

            if (!oreCounts.containsKey(playerName)) {
                oreCounts[playerName] = HashMap()
            }

            val playerOreCounts = oreCounts[playerName]!!

            if (!playerOreCounts.containsKey(oreType)) {
                playerOreCounts[oreType] = 0
            }

            playerOreCounts[oreType] = playerOreCounts[oreType]!! + 1
        }
    }

    fun displayOreCounts(playerName: String) {
        val player = server.getPlayer(playerName)

        if (player == null) {
            logger.warning("Player $playerName not found")
            return
        }

        val playerOreCounts = oreCounts[playerName]

        if (playerOreCounts == null) {
            logger.warning("Player $playerName not found in oreCounts map")
            return
        }

        for (oreType in playerOreCounts.keys) {
            val count = playerOreCounts[oreType]!!
            player.sendMessage("$oreType: $count")
        }
    }
}

private val Material.isOre: Boolean
    get() = when (this) {
        Material.IRON_ORE, Material.GOLD_ORE, Material.ANCIENT_DEBRIS,
        Material.DIAMOND_ORE, Material.EMERALD_ORE -> true
        else -> false
    }
