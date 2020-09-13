package io.github.redstoneparadox.paradoxconfig.util

/**
 * A hash map that allows for iterating through entries in the
 * order they were inserted in. Note that it is slower than
 * a default hash map.
 */
internal class OrderedMutableMap<K, V>() : MutableMap<K, V> {

    private val internal: MutableMap<K, V> = hashMapOf()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = internal.entries
    override val keys: MutableSet<K>
        get() = internal.keys
    override val values: MutableCollection<V>
        get() = internal.values
    override val size: Int
        get() = internal.size

    val orderedEntries: MutableList<MutableMap.MutableEntry<K, V>> = mutableListOf()
    val orderedKeys: MutableList<K> = mutableListOf()
    val orderedValues: MutableList<V> = mutableListOf()

    override fun remove(key: K): V? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putAll(from: Map<out K, V>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun put(key: K, value: V): V? {
        return internal.put(key, value)
    }

    override fun clear() {
        internal.clear()
        entries.clear()
        keys.clear()
        values.clear()
        orderedEntries.clear()
        orderedKeys.clear()
        orderedValues.clear()
    }

    override fun isEmpty(): Boolean = size == 0;

    override fun get(key: K): V? = internal[key]

    override fun containsValue(value: V): Boolean = internal.containsValue(value)

    override fun containsKey(key: K): Boolean = internal.containsKey(key)
}