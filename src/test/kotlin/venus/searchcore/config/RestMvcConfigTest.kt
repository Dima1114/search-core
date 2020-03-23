package venus.searchcore.config

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.amshove.kluent.`should be instance of`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.ObjectFactory
import org.springframework.context.ApplicationContext
import org.springframework.core.convert.ConversionService
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.hateoas.mediatype.MessageResolver
import org.springframework.test.util.ReflectionTestUtils
import venus.searchcore.search.ApiQuerydslBindingsFactory
import venus.searchcore.search.ApiQuerydslMethodArgumentResolver
import venus.searchcore.search.operator.predicate.IsEmptySearchOperator
import venus.searchcore.search.operator.predicate.SearchOperator

@RunWith(MockitoJUnitRunner::class)
class RestMvcConfigTest {

    @InjectMocks
    lateinit var testSubject: RestMvcConfig

    @Mock
    lateinit var context: ApplicationContext

    @Mock
    lateinit var conversionService: ObjectFactory<ConversionService>

    @Mock
    lateinit var listableFatory: ListableBeanFactory

    @Mock
    lateinit var resolver: MessageResolver

    @Spy
    var list: List<SearchOperator> = listOf(IsEmptySearchOperator())

    @Before
    fun setUp(){
        testSubject.afterPropertiesSet()
        whenever(context.getBeanNamesForType(any(Class::class.java), anyBoolean(), anyBoolean())).thenReturn(arrayOf<String>())
        ReflectionTestUtils.setField(testSubject, "applicationContext", context)
        ReflectionTestUtils.setField(testSubject, "resolver", resolver)
    }

    @Test
    fun `repo request argument resolver is configured`(){

        //given
        val spyTestSubject = Mockito.spy(testSubject)

        //when
        val result = spyTestSubject.repoRequestArgumentResolver()

        //then
        verify(spyTestSubject, times(1)).querydslMethodArgumentResolver()
        result `should be instance of` ApiQuerydslMethodArgumentResolver::class
    }

    @Test
    fun `querydsl bindings factory should be configured`(){

        //when
        val result = testSubject.querydslBindingsFactory()
        val pathResolver = result.entityPathResolver

        //then
        result `should be instance of` ApiQuerydslBindingsFactory::class
        pathResolver `should be instance of` SimpleEntityPathResolver::class
    }

    @Test
    fun `querydsl method argument resolver`(){

        //when
        val result = testSubject.querydslMethodArgumentResolver()

        //then
        result `should be instance of` ApiQuerydslMethodArgumentResolver::class
    }
}
