package com.arctouch.codechallenge.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.arctouch.codechallenge.data.Repository
import com.arctouch.codechallenge.di.appModule
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeViewModelTest : KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel: HomeViewModel by inject()
    val repository: Repository by inject()

    @Mock
    private lateinit var observer: Observer<List<Movie>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        startKoin { modules(appModule) }

        declareMock<Repository> {
            given(this.getGenres()).willReturn(Single.just(mockedGenreList()))
            given(this.getUpcomingMovies(anyInt())).willReturn(Single.just(Pair(mockedMovieList(), 1)))
        }
    }

    private fun mockedGenreList(): List<Genre> {
        val json =
            "[{\"id\":28,\"name\":\"Ação\"},{\"id\":12,\"name\":\"Aventura\"},{\"id\":16,\"name\":\"Animação\"},{\"id\":35,\"name\":\"Comédia\"},{\"id\":80,\"name\":\"Crime\"}]"
        val listGenres = Types.newParameterizedType(List::class.java, Genre::class.java)
        val adapter: JsonAdapter<List<Genre>> = Moshi.Builder().build().adapter(listGenres)
        return adapter.fromJson(json)
    }

    private fun mockedMovieList(): List<Movie> {
        val json =
            "[{\"popularity\":250.222,\"vote_count\":1036,\"video\":false,\"poster_path\":\"\\/jvg9Rf3mvsVTnhuyxPlN0eEL76C.jpg\",\"id\":512200,\"adult\":false,\"backdrop_path\":\"\\/zTxHf9iIOCqRbxvl8W5QYKrsMLq.jpg\",\"original_language\":\"en\",\"original_title\":\"Jumanji: The Next Level\",\"genre_ids\":[28,12,35,14],\"title\":\"Jumanji: Próxima Fase\",\"vote_average\":6.7,\"overview\":\"Tentado em revisitar o mundo de Jumanji, Spencer (Alex Wolff) decide consertar o jogo de videogame que permite que os jogadores sejam transportados ao local. Logo o quarteto formado por Smolder Bravestone (Dwayne Johnson), Moose Finbar (Kevin Hart), Shelly Oberon (Jack Black) e Ruby Roundhouse (Karen Gillan) ressurge, agora comandado por outras pessoas: os avôs de Spencer e Fridge (Danny DeVito e Danny Glover) assumem as personas de Bravestone e Finbar, enquanto o próprio Fridge (Ser'Darius Blain) agora está sob a pele de Oberon.\",\"release_date\":\"2020-01-16\"},{\"popularity\":71.306,\"vote_count\":313,\"video\":false,\"poster_path\":\"\\/7GsM4mtM0worCtIVeiQt28HieeN.jpg\",\"id\":515001,\"adult\":false,\"backdrop_path\":\"\\/agoBZfL1q5G79SD0npArSlJn8BH.jpg\",\"original_language\":\"en\",\"original_title\":\"Jojo Rabbit\",\"genre_ids\":[35,18,10752],\"title\":\"Jojo Rabbit\",\"vote_average\":7.9,\"overview\":\"Alemanha, durante a Segunda Guerra Mundial. Jojo (Roman Griffin Davis) é um jovem nazista de 10 anos, que trata Adolf Hitler (Taika Waititi) como um amigo próximo, em sua imaginação. Seu maior sonho é participar da Juventude Hitlerista, um grupo pró-nazista composto por outras pessoas que concordam com os seus ideais. Um dia, Jojo descobre que sua mãe (Scarlett Johansson) está escondendo uma judia (Thomasin McKenzie) no sótão de casa. Depois de várias tentativas frustradas para expulsá-la, o jovem rebelde começa a desenvolver empatia pela nova hóspede.\",\"release_date\":\"2020-02-06\"}]"
        val listMovies = Types.newParameterizedType(List::class.java, Movie::class.java)
        val adapter: JsonAdapter<List<Movie>> = Moshi.Builder().build().adapter(listMovies)
        return adapter.fromJson(json)
    }

    @After
    fun cleanup() {
        stopKoin()
    }

    @Test
    fun fetchData() {
        viewModel.upcomingMovies.observeForever(observer)

        assertNotNull(viewModel.upcomingMovies.value)
        assertEquals(2, viewModel.upcomingMovies.value?.size)
    }

    @Test
    fun fetchMoreData() {
        viewModel.fetchMoreData()
        viewModel.upcomingMovies.observeForever(observer)

        assertNotNull(viewModel.upcomingMovies.value)
        assertEquals(2, viewModel.upcomingMovies.value?.size)
    }

    @Test
    fun `movie data parsed correctly`() {
        viewModel.upcomingMovies.observeForever(observer)

        with(viewModel.upcomingMovies.value?.first()) {
            assertNotNull(this)
            assertEquals(this?.title, "Jumanji: Próxima Fase")
            assertEquals(this?.id, 512200)
        }
    }
}