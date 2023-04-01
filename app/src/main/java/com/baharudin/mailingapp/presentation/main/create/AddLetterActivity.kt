package com.baharudin.mailingapp.presentation.main.create

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baharudin.mailingapp.core.RealPathUtil
import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import com.baharudin.mailingapp.data.login.remote.dto.LoginResponse
import com.baharudin.mailingapp.databinding.ActivityAddLetterBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterKindEntity
import com.baharudin.mailingapp.domain.login.entity.LoginEntity
import com.baharudin.mailingapp.presentation.common.extention.gone
import com.baharudin.mailingapp.presentation.common.extention.showGenericAlertDialog
import com.baharudin.mailingapp.presentation.common.extention.showToast
import com.baharudin.mailingapp.presentation.common.extention.visible
import com.baharudin.mailingapp.presentation.login.LoginViewState
import com.baharudin.mailingapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File


@AndroidEntryPoint
class AddLetterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddLetterBinding
    private val viewModel : AddLetterViewModel by viewModels()
    private var dataList = ArrayList<LetterKindEntity>()
    private val PERMISSION_REQ_ID = 22
    var filename = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        observe()
        setupAdapterLetter()
        setupListFilter()
        sendLetter()

        binding.imageLaporan.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }
    }

    private fun setupAdapterLetter(){
        val filterAdapter = LetterKindsAdapter(dataList)
        filterAdapter.setItemClickListener(object : LetterKindsAdapter.OnItemClickListener{
            override fun onClick(filterModel: LetterKindEntity) {
                binding.tvLetterKinds.text = filterModel.name
            }
        })
        binding.rvLetter.apply {
            layoutManager = LinearLayoutManager(this@AddLetterActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    private fun observe(){
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleStateChange(state)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: AddLetterViewState){
        when(state){
            is AddLetterViewState.Init -> Unit
            is AddLetterViewState.ErrorUpload -> handleErrorUpload(state.rawResponse)
            is AddLetterViewState.SuccessCreate -> handleSuccessLogin()
            is AddLetterViewState.ShowToast -> showToast(state.message)
            is AddLetterViewState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleErrorUpload(response: WrappedResponse<LetterDto>){
        showGenericAlertDialog(response.message)
    }
    private fun handleSuccessLogin(){
        movePage()
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingProgressBar.visible()
        }else{
            binding.loadingProgressBar.gone()
        }
    }
    private fun sendLetter() {
        binding.fabSend.setOnClickListener {
            val letterParams = mutableMapOf<String, RequestBody>().apply {
                put("sender_identity", binding.inputNama.text.toString().toRequestBody())
                put("letter_destination", binding.inputLokasi.text.toString().toRequestBody())
                put("letter_date", binding.inputTanggal.text.toString().toRequestBody())
                put("letter_number", binding.inputNoSurat.text.toString().toRequestBody())
                put("letter_discription", binding.inputLaporan.text.toString().toRequestBody())
                put("letter_kinds", binding.tvLetterKinds.text.toString().toRequestBody())
            }
            val imageFile = File(filename)
            val requestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val partFile1 = MultipartBody.Part.createFormData("letter_images", imageFile.name, requestBody)

            runCatching {
                viewModel.uploadLetter(letterParams, partFile1)
            }.onSuccess {
                showToast("berhasil")
            }.onFailure {
                showToast(it.message ?: "Error occurred")
            }

        }
    }


    fun movePage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            val context = this
            filename = getRealPathFromUri(context, uri)
            val bitmap = try {
                BitmapFactory.decodeFile(filename)
            } catch (ex: Exception) {
                null
            }
            bitmap?.let {
                compressImage(it)
                binding.imageLaporan.setImageBitmap(it)
            }
        }
    }

    private fun getRealPathFromUri(context: Context, uri: Uri): String {
        return RealPathUtil.getRealPath(context, uri).toString()
    }


    private fun compressImage(image: Bitmap): Bitmap {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var quality = 100
        while (outputStream.toByteArray().size / 1024 > 150 && quality > 0) {
            outputStream.reset()
            quality -= 10
            image.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        val result = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().size)
        outputStream.close()
        return result
    }

    private fun setupListFilter(){
        dataList.add(
            LetterKindEntity(
                "letter_out",
                "Surat Keluar"
            )
        )
        dataList.add(
            LetterKindEntity(
                "letter_in",
                "Surat Masuk"
            )
        )
    }
}