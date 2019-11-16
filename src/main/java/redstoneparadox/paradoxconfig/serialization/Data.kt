package redstoneparadox.paradoxconfig.serialization

import blue.endless.jankson.JsonArray

/**
 * 
 */
sealed class Data<T> {
    var successful: Boolean = false

    var result: T? = null

    open val indicies: Iterator<Any> = arrayOf(1).iterator()

    abstract fun getData(index: Any): Any?

    abstract fun toResult(index: Any, value: Any)

    open fun supplyCollection(supplier: () -> MutableCollection<Any>, indexer: MutableCollection<Any>.(index: Int) -> Any?) {}

    open fun supplyDictionary(supplier: () -> MutableMap<String, Any>) {}

    inline fun <reified U> read(reader: (U) -> Any) {
        if (successful) return

        for (index in indicies) {
            val value = getData(index)
            if (value is U) {
                toResult(index, reader(value))
                successful = true
            }
        }
    }

    inline fun <reified U> then(consumer: (U) -> Unit) {
        val any = result
        if (successful && any is U) {
            consumer(any)
        }
    }

    class SingleData<T: Any>(private val t: T): Data<Any>() {

        override fun getData(index: Any): Any {
            return t
        }

        override fun toResult(index: Any, value: Any) {
            result = value
        }
    }

    class CollectionData<T: Any>(private val collection: Array<T>): Data<MutableCollection<Any>>() {
        override val indicies: Iterator<Any>
            get() = collection.indices.iterator()

        private var indexer: (MutableCollection<Any>.(index: Int) -> Any?)? = null

        override fun getData(index: Any): Any {
            if (index is Int) {
                return collection[index]
            }
            throw Exception()
        }

        override fun toResult(index: Any, value: Any) {
            if (index is Int && indexer != null) {
                indexer?.let { result?.let { it1 -> it(it1, index) } }
            }
        }

        override fun supplyCollection(supplier: () -> MutableCollection<Any>, indexer: MutableCollection<Any>.(index: Int) -> Any?) {
            result = supplier()
            this.indexer = indexer
        }
    }

    class DictionaryData<T: Any>(private val dictionary: Map<String, T>): Data<MutableMap<String, Any>>() {
        override val indicies: Iterator<Any>
            get() = dictionary.keys.iterator()

        override fun getData(index: Any): Any? {
            if (index is String) {
                return dictionary[index]
            }
            throw Exception()
        }

        override fun toResult(index: Any, value: Any) {
            if (index is String) {
                dictionary[index]
            }
        }

        override fun supplyDictionary(supplier: () -> MutableMap<String, Any>) {
            result = supplier()
        }
    }

}