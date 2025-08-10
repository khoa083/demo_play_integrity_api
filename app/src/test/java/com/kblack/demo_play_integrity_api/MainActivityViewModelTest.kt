package com.kblack.demo_play_integrity_api

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManager
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import com.google.android.play.core.integrity.StandardIntegrityManager
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityToken
import com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenProvider
import com.google.firebase.perf.metrics.Trace
import com.kblack.base.BaseRepository
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.firebase.FirebaseManager
import com.kblack.demo_play_integrity_api.repository.Repository
import com.kblack.demo_play_integrity_api.utils.ErrorHandler
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import org.junit.*
import kotlin.onSuccess

class MainActivityViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainActivityViewModel
    private val mockRepository = mockk<Repository>()
    private val mockFirebaseManager = mockk<FirebaseManager>()
    private val mockErrorHandler = mockk<ErrorHandler>()
    private val mockContext = mockk<Context>()
    private val mockTrace = mockk<Trace>()

    @Before
    fun setUp() {
        // Mock static methods
        mockkStatic(IntegrityManagerFactory::class)

        // Setup common mocks
        every { mockFirebaseManager.startTrace(any()) } returns mockTrace
        every { mockTrace.stop() } just Runs
        every { mockFirebaseManager.logEvent(any()) } just Runs
        every { mockFirebaseManager.logEvent(any(), any<Bundle>()) } just Runs
        every { mockFirebaseManager.log(any()) } just Runs
        every { mockErrorHandler.handleGenericError(any(), any()) } just Runs
        every { mockErrorHandler.handleGenericError(any(), any(), any()) } just Runs
        every { mockErrorHandler.handlePlayIntegrityError(any(), any(), any()) } just Runs

        viewModel = MainActivityViewModel(mockRepository, mockFirebaseManager, mockErrorHandler)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `prepareIntegrityTokenProvider success should prepare token provider`() {
        // Arrange
        val mockStandardIntegrityManager = mockk<StandardIntegrityManager>()
        val mockTokenProvider = mockk<StandardIntegrityTokenProvider>()
        val mockTask = mockk<Task<StandardIntegrityTokenProvider>>()

        every { IntegrityManagerFactory.createStandard(mockContext) } returns mockStandardIntegrityManager
        every { mockStandardIntegrityManager.prepareIntegrityToken(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<StandardIntegrityTokenProvider>>()
            listener.onSuccess(mockTokenProvider)
            mockTask
        }
        every { mockTask.addOnFailureListener(any()) } returns mockTask

        // Act
        viewModel.prepareIntegrityTokenProvider(mockContext)

        // Assert
        verifySequence {
            mockFirebaseManager.startTrace("prepare_integrity_token")
            IntegrityManagerFactory.createStandard(mockContext)
            mockStandardIntegrityManager.prepareIntegrityToken(any())
            mockTrace.stop()
            mockFirebaseManager.logEvent("integrity_token_provider_prepared")
            mockFirebaseManager.log("Integrity token provider prepared successfully")
        }

        // Verify no error handling was called
        verify(exactly = 0) { mockErrorHandler.handleGenericError(any(), any()) }
    }

    @Test
    fun `prepareIntegrityTokenProvider failure should handle error`() {
        // Arrange
        val mockStandardIntegrityManager = mockk<StandardIntegrityManager>()
        val mockTask = mockk<Task<StandardIntegrityTokenProvider>>()
        val exception = RuntimeException("Test exception")

        every { IntegrityManagerFactory.createStandard(mockContext) } returns mockStandardIntegrityManager
        every { mockStandardIntegrityManager.prepareIntegrityToken(any()) } returns mockTask
        every { mockTask.addOnSuccessListener(any()) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(exception)
            mockTask
        }

        // Act
        viewModel.prepareIntegrityTokenProvider(mockContext)

        // Assert
        verify { mockTrace.stop() }
        verify { mockErrorHandler.handleGenericError(exception, "prepare_token_provider") }
    }
}