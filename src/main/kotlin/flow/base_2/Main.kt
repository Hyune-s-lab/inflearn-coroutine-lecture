package flow.base_2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import kotlin.time.measureTime

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class UserDTO(
    val id: Long,
    val name: String,
    val email: String,
)

class UserRepository {
    private val users = (1..1000).map {
        User(
            id = it.toLong(),
            name = "user$it",
            email = "user$it@example.com"
        )
    }

    suspend fun getUserByPage(pageSize: Int = 100): Flow<List<User>> = flow {
        val totalPages = (users.size + pageSize - 1) / pageSize

        for (page in 0 until totalPages) {
            delay(100)
            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, users.size)

            // 해당 부분에는 실제 db 조회하여 값을 emit 하면 된다.

            emit(users.subList(startIndex, endIndex)) // 값을 넘겨주는 코드
            println("페이지 $page 로드됨 (${endIndex - startIndex}) 개 레코드")
        }
    }

    suspend fun findById(id: Long): User? {
        delay(50)
        return users.find { it.id == id }
    }
}

class UserService(private val userRepository: UserRepository) {
    suspend fun getAllUsersAsDTO(): Flow<UserDTO> = userRepository.getUserByPage()
        .onEach { println("페이징 처리하며 가져오는 중") }
        .flatMapConcat { it.asFlow() }
        .map { user ->
            delay(5)
            UserDTO(
                id = user.id,
                name = user.name,
                email = user.email
            )
        }.flowOn(Dispatchers.Default)
}

fun main(): Unit = runBlocking {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    println("전체 유저 DTO 변환 시작")

    var count = 0
    val time = measureTime {
        userService.getAllUsersAsDTO().buffer(50).collect { users ->
            count++
            if (count % 100 == 0) {
                println("$count 사용자 처리 완료")
            }
        }
    }

    println("총 유저 처리 시간 소요 시간: $time")


    val userId = 42L
    println("유저 ID: $userId 조회 시작")

    flow {
        val user = userRepository.findById(userId)
        if (user != null) {
            emit(user)
        } else {
            throw NoSuchElementException("User with ID $userId not found")
        }
    }.map { UserDTO(id = it.id, name = it.name, email = it.email) }
        .catch { emit(UserDTO(-1, "unknown", "unknown")) }
        .collect { println("유저 DTO: $it") }
}
