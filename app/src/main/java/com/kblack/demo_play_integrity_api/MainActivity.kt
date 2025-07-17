package com.kblack.demo_play_integrity_api

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kblack.base.BaseActivity
import com.kblack.base.extensions.clickWithTrigger
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.factory.MainActivityViewModelFactory
import com.kblack.demo_play_integrity_api.firebase.FirebaseManager
import com.kblack.demo_play_integrity_api.repository.Repository
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.observeNonNull
import com.kblack.base.extensions.toast
import com.google.gson.Gson
import com.kblack.demo_play_integrity_api.utils.RawJson.formatJsonWithColors

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var repository: Repository


    /**
     * ViewModel for MainActivity, initialized with a factory that provides the necessary dependencies.
     *
     *  @sample by viewModels()
     *  Chỉ hoạt động với ViewModel có constructor rỗng hoặc chỉ nhận Application parameter
     *  Tự động quản lý lifecycle của ViewModel
     *  Đơn giản nhưng không hỗ trợ dependency injection
     * ---------------------------------------------------
     * chỉ hoạt động nếu ViewModel có constructor như này:
     * class MainActivityViewModel : BaseViewModel() // ✅
     * hoặc
     * class MainActivityViewModel(application: Application) : AndroidViewModel(application) // ✅
     *
     * KHÔNG hoạt động với:
     * class MainActivityViewModel(
     *     private val repository: Repository,
     *     private val firebaseManager: FirebaseManager,
     *     private val errorHandler: ErrorHandler
     * ) : BaseViewModel() // ❌ Sẽ gây crash
     * ---------------------------------------------------
     * @sample by lazy
     *  by lazy với custom ViewModelFactory
     *  Sử dụng custom factory để inject dependencies
     *  Hỗ trợ ViewModel với constructor có parameters
     *  Khởi tạo dependencies thủ công
     *  Linh hoạt hơn nhưng phức tạp hơn
     *  ---------------------------------------------------
     * // Hoạt động với ViewModel có dependencies:
     * class MainActivityViewModel(
     *     private val repository: Repository,
     *     private val firebaseManager: FirebaseManager,
     *     private val errorHandler: ErrorHandler
     * ) : BaseViewModel() // ✅
     *  ---------------------------------------------------
     */

    override val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(
            this,
            MainActivityViewModelFactory(repository, applicationContext)
        )[MainActivityViewModel::class.java]
    }

    override val layoutId: Int = R.layout.activity_main
    override val idContainerView: Int = R.id.main

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize dependencies before calling super.onCreate()
        repository = Repository()
        firebaseManager = FirebaseManager(applicationContext)

        super.onCreate(savedInstanceState)

        // Set user properties for better crash reporting
        firebaseManager.setUserId("user_${System.currentTimeMillis()}")
        firebaseManager.setUserProperty("app_version", BuildConfig.VERSION_NAME)
        firebaseManager.setCustomKey("build_type", BuildConfig.BUILD_TYPE)

        viewModel.prepareIntegrityTokenProvider(applicationContext)

        // Log screen view
        firebaseManager.logEvent("screen_view", Bundle().apply {
            putString("screen_name", "MainActivity")
        })
    }

    override fun setupView(activityBinding: ActivityMainBinding) {
        activityBinding.apply {
            btnVerify.clickWithTrigger {
                firebaseManager.logEvent("button_click", Bundle().apply {
                    putString("button_name", "verify_standard")
                })
                viewModel.playIntegrityRequest(applicationContext)
            }

            btnVerifyLocal.clickWithTrigger {
                firebaseManager.logEvent("button_click", Bundle().apply {
                    putString("button_name", "verify_local")
                })
                viewModel.playIntegrityRequestForLocal(applicationContext)
            }

            observeData(this@apply)
        }
    }

    private fun observeData(activityBinding: ActivityMainBinding) {
        viewModel.resultRAW.observeNonNull(this@MainActivity) { dataResult ->
            when (dataResult?.status) {
                DataResult.Status.LOADING -> {
                    activityBinding.txtResult.text = ""
                    activityBinding.ldm3.visibility = View.VISIBLE
                }

                DataResult.Status.SUCCESS -> {
                    // TODO: if use toString() method, it will not format the json string
                    val jsonString = Gson().toJson(dataResult.data)
                    activityBinding.txtResult.text = formatJsonWithColors(jsonString)
                    activityBinding.ldm3.visibility = View.GONE

                    // Log successful result display
                    firebaseManager.logEvent("result_displayed", Bundle().apply {
                        putString("result_type", "success")
                        putInt("result_length", jsonString.length)
                    })
                }

                DataResult.Status.ERROR -> {
                    activityBinding.txtResult.text = dataResult.message ?: "Unknown error"
                    activityBinding.ldm3.visibility = View.GONE

                    // Log error display
                    firebaseManager.logEvent("result_displayed", Bundle().apply {
                        putString("result_type", "error")
                        putString("error_message", dataResult.message ?: "Unknown error")
                    })
                }

                null -> {
                    activityBinding.ldm3.visibility = View.GONE
                    this@MainActivity.toast("DataResult is null")
                    firebaseManager.recordException(IllegalStateException("DataResult is null"))
                }
            }
        }
    }

    override fun showView(isShow: Boolean) {
        TODO("Not yet implemented")
    }
}