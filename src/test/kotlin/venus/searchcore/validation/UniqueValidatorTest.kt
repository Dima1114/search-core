package venus.searchcore.validation

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW
import org.springframework.transaction.support.DefaultTransactionDefinition
import venus.searchcore.entity.TestEntity
import venus.searchcore.integration.AbstractTestMvcIntegration
import venus.searchcore.repository.TestEntityRepository
import java.time.LocalDate
import javax.validation.ConstraintViolationException
import kotlin.test.assertTrue

@FixMethodOrder(MethodSorters.JVM)
class UniqueValidatorTest : AbstractTestMvcIntegration() {

    @Autowired
    lateinit var testEntityRepository: TestEntityRepository
    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @Before
    fun setUp() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
        testEntityRepository.save(TestEntity(name = "test1", date = LocalDate.now(), float = 100F))
        transactionManager.commit(status)
    }

    @After
    fun cleanUp() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
        testEntityRepository.deleteAll()
        transactionManager.commit(status)
    }

    @Test
    fun `should fail to save entity because of field reject`() {

        try {
            //when
            performPost("/testEntities", """{"name": "test1"}""")
            assertTrue { false }
        } catch (ex: Exception) {
            //then
            val errors = getConstraints(ex.cause as ConstraintViolationException)
            errors.size `should be equal to` 1
            errors[0]["entity"] `should equal` "TestEntity"
            errors[0]["field"] `should equal` "name"
            errors[0]["defaultMessage"] `should equal` "name 'test1' already exists"
        }
    }

    @Test
    fun `should fail to save entity because of fields bunch reject`() {

        try {
            //when
            performPost("/testEntities", """{"name": "test2", "date":"${LocalDate.now()}", "float":100}""")
            assertTrue { false }
        } catch (ex: Exception) {
            //then
            val errors = getConstraints(ex.cause as ConstraintViolationException)
            errors.size `should be equal to` 1
            errors[0]["entity"] `should equal` "TestEntity"
            errors[0]["field"] `should equal` "float"
            errors[0]["defaultMessage"] `should equal` "already exists"
        }

    }

    private fun performPost(query: String, body: String): ResultActions {
        return mvc.perform(MockMvcRequestBuilders.post(query)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
    }

    private fun getConstraints(exception: ConstraintViolationException): List<Map<String, Any>> {
        return exception.constraintViolations
                .map {
                    mapOf("entity" to it.rootBean::class.java.simpleName,
                            "field" to it.propertyPath.toString(),
                            "defaultMessage" to it.messageTemplate)
                }
    }
}