import java.lang.IllegalArgumentException

class LruCache<K, V>(private val capacity: Int) {
    init {
        if (capacity < 1) {
            throw IllegalArgumentException("cache capacity must be greater than 0")
        }
    }

    private inner class Node(val key: K, val value: V) {
        var next: Node? = null
        var prev: Node? = null
    }

    private var head: Node? = null
    private var tail: Node? = null

    private val hashMap: HashMap<K, Node> = HashMap()

    private fun insertNode(node: Node) {
        node.prev = head
        node.next = null
        head?.next = node

        head = node
        if (tail == null) {
            tail = node
        }
    }

    private fun deleteNode(node: Node) {
        node.prev?.next = node.next
        node.next?.prev = node.prev

        if (node == head) {
            head = node.prev
        }
        if (node == tail) {
            tail = node.next
        }
    }

    fun put(key: K, value: V) {
        assert(hashMap.size <= capacity)

        val initialSize = hashMap.size

        val newNode = Node(key, value)

        if (!hashMap.containsKey(key)) {
            if (hashMap.size == capacity) {
                hashMap.remove(tail!!.key)
                deleteNode(tail!!)
            }
            insertNode(newNode)
            hashMap[key] = newNode
        } else {
            deleteNode(hashMap[key]!!)
            hashMap[key] = newNode
            insertNode(newNode)
        }

        assert(hashMap.size <= capacity)
        assert(head != null)
        assert(tail != null)
        assert(hashMap.size >= initialSize)
        assert(head!!.key == key)
        assert(head!!.value == value)
    }

    fun get(key: K) : V? {
        assert(hashMap.size <= capacity)

        val node = hashMap[key] ?: return null

        deleteNode(node)
        insertNode(node)

        assert(hashMap.size <= capacity)
        assert(head != null)
        assert(tail != null)
        assert(head!!.key == key)

        return node.value
    }
}