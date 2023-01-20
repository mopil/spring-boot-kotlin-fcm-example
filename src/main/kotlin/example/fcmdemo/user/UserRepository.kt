package example.fcmdemo.user

import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    private val store = mutableListOf<User>()

    init {
        repeat(10) {
            store.add(User(it.toLong(), "token$it"))
        }
    }

    fun findAll(): List<User> {
        return store
    }
}