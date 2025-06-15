package flow.base_3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

/**
 * 데이터 모델 계층: 단순하게 임시로 데이터를 생성
 * 로그 생성 계층: 로그를 예시로 생성하는 역할을 수행
 * 분석 계층: 들어오는 로그를 분석
 * main: 실행 계층
 */

data class LogEntry(
    val timestamp: LocalDateTime,
    val level: LogLevel,
    val service: String,
    val message: String,
    val metadata: Map<String, String> = emptyMap()
)

enum class LogLevel(val v: String) {
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR"),
}

class LogStreamGenerator {
    private val services = listOf("auth-service", "user-service", "payment-service", "notification-service")
    private val debugMessages = listOf("Connection established", "Request processed", "Cache hit", "Data fetched")
    private val infoMessages = listOf("User logged in", "Payment processed", "Email sent", "Record updated")
    private val warnMessages = listOf("Retry attempt", "Slow query detected", "Cache miss", "High memory usage")
    private val errorMessages =
        listOf("Database connection failed", "Payment declined", "Service unavailable", "Validation error")

    fun generateLogStream() = flow {
        repeat(500) {
            delay(10)
            emit(createRandomLogEntry())
        }
    }

    private fun createRandomLogEntry(): LogEntry {
        val level = LogLevel.entries.random()
        val service = services.random()

        val message = when (level) {
            LogLevel.DEBUG -> debugMessages.random()
            LogLevel.INFO -> infoMessages.random()
            LogLevel.WARN -> warnMessages.random()
            LogLevel.ERROR -> errorMessages.random()
        }

        return LogEntry(
            timestamp = LocalDateTime.now(),
            level = level,
            service = service,
            message = message,
        )
    }
}

class LogAnalysisService {
    fun formatLogEntries(logFLow: Flow<LogEntry>) = logFLow.map { log ->
        "${log.timestamp} [${log.level.v}] ${log.service}: ${log.message}"
    }

    fun filterErrorLogs(logFlow: Flow<LogEntry>) = logFlow.filter { it.level == LogLevel.ERROR }

    suspend fun countLogsByService(logFlow: Flow<LogEntry>) = logFlow.fold(mutableMapOf<String, Int>()) { count, log ->
        count[log.service] = (count[log.service] ?: 0) + 1
        count
    }
}

fun main() = runBlocking {
    val logAnalysisService = LogAnalysisService()
    val logStreamGenerator = LogStreamGenerator()

    val logStream = logStreamGenerator.generateLogStream().shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Eagerly,
        replay = 100
    )

    println("Log stream started...")
    logAnalysisService.formatLogEntries(logStream).take(10).collect(::println)

    println("Error logs:")
    logAnalysisService.filterErrorLogs(logStream).take(5).collect(::println)
    
    println("Log count by service:")
    val logList = logStreamGenerator.generateLogStream().toList()
    val serviceCounts = logList.groupingBy { it }.eachCount()

    serviceCounts.forEach { (service, count) ->
        println("$service: $count logs")
    }
}
