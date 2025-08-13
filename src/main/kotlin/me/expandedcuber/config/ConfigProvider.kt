package me.expandedcuber.config

import com.mojang.datafixers.util.Pair
import me.expandedcuber.util.SimpleConfig

class ConfigProvider : SimpleConfig.DefaultConfig {
    private var configContents = ""

    private val configsList: MutableList<Pair<String, *>> = mutableListOf()

    fun getConfigsList(): List<Pair<String, *>> {
        return configsList
    }

    fun addKeyValuePair(keyValuePair: Pair<String, *>) {
        configsList.add(keyValuePair)
        configContents += "${keyValuePair.first}=${keyValuePair.second}"
    }

    override fun get(namespace: String): String {
        return configContents
    }
}
