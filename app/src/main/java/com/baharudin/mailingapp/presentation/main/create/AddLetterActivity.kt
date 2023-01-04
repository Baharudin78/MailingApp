package com.baharudin.mailingapp.presentation.main.create

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.core.RealPathUtil
import com.baharudin.mailingapp.databinding.ActivityAddLetterBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterKindEntity
import com.baharudin.mailingapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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

        binding.lyDate.setOnClickListener {
            val newCalendar: Calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val newDate: Calendar = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                    binding.inputTanggal.setText(dateFormatter.format(newDate.getTime()))
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }


    }

    private fun setupAdapterLetter(){
        val filterAdapter = LetterKindsAdapter(dataList)
        filterAdapter.setItemClickListener(object : LetterKindsAdapter.OnItemClickListener{
            override fun onClick(filterModel: LetterKindEntity) {
                Toast.makeText(this@AddLetterActivity, "${filterModel.name}", Toast.LENGTH_SHORT).show()
                binding.tvLetterKinds.text = filterModel.name
            }
        })
        binding.rvLetter.apply {
            layoutManager = LinearLayoutManager(this@AddLetterActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    private fun sendLetter(){
        binding.fabSend.setOnClickListener{
            if (
                binding.inputLaporan.text.toString() != "" &&
                binding.inputLokasi.text.toString() != "" &&
                binding.inputNama.text.toString() != "" &&
                binding.inputTanggal.text.toString() != "" &&
                binding.inputNoSurat.text.toString() != ""
            ){
                var param : HashMap<String, RequestBody> = HashMap<String, RequestBody>()
                param.apply {
                    param["sender_identity"] = createRequestBody(binding.inputNama.text.toString())
                    param["letter_destination"]= createRequestBody(binding.inputLokasi.text.toString())
                    param["letter_number"] = createRequestBody(binding.inputNoSurat.text.toString())
                    param["letter_date"] = createRequestBody(binding.inputTanggal.text.toString())
                    param["letter_discription"] = createRequestBody(binding.inputLaporan.text.toString())
                    param["letter_kinds"] = createRequestBody(binding.tvLetterKinds.text.toString())
                }
                val imageFile = File(filename)
                val requestBody =
                    RequestBody.create(
                        "multipart/form-file".toMediaTypeOrNull(),
                        imageFile
                    )
                val partFile1 =
                    MultipartBody.Part.createFormData(
                        "letter_images",
                        imageFile.name,
                        requestBody
                    )
                viewModel.uploadLetter(param, partFile1)
                movePage()
            }
        }
    }

    fun movePage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun createRequestBody(s : String) : RequestBody {
        return RequestBody.create(
            MULTIPART_FORM_DATA.toMediaTypeOrNull(), s
        )
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
        if (requestCode === 10 && resultCode === RESULT_OK) {
            val uri: Uri = data!!.data!!
            val context: Context = this
            filename = RealPathUtil.getRealPath(context, uri).toString()
            val bitmap = BitmapFactory.decodeFile(filename)
            binding.imageLaporan.setImageBitmap(bitmap)
        }

    }
    companion object {
        const val MULTIPART_FORM_DATA = "multipart/form-data"
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