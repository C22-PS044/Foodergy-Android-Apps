package com.bangkitacademy.foodergyapps.ui.scanner

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkitacademy.foodergyapps.data.model.ModelExecutionResult
import com.bangkitacademy.foodergyapps.utils.OCRModelExecutor
import com.bangkitacademy.foodergyapps.utils.OCRModelExecutor.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {
    private val _resultingBitmap = MutableLiveData<ModelExecutionResult?>()

    val resultingBitmap: MutableLiveData<ModelExecutionResult?>
        get() = _resultingBitmap

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob)

    // the execution of the model has to be on the same thread where the interpreter
    // was created
    fun onApplyModel(
        context: Context,
        fileName: String,
        ocrModel: OCRModelExecutor?,
        inferenceThread: ExecutorCoroutineDispatcher
    ) {
        viewModelScope.launch(inferenceThread) {
            val inputStream = context.assets.open(fileName)
            val contentImage = BitmapFactory.decodeStream(inputStream)
            try {
                val result = ocrModel?.execute(contentImage)
                _resultingBitmap.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Fail to execute OCRModelExecutor: ${e.message}")
                _resultingBitmap.postValue(null)
            }
        }
    }
}
