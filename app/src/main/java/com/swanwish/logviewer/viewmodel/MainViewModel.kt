package com.swanwish.logviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/***
 * Created by Stephen on 2022/6/15 21:34
 *
 * @description
 */
class MainViewModel : ViewModel() {
    private var _crashLog = MutableLiveData<String>()
    val crashLog: LiveData<String> = _crashLog

    fun refreshCrashLog() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val process = Runtime.getRuntime().exec("logcat -b crash -d")
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var read: Int
                val buffer = CharArray(4096)
                val output = StringBuffer()
                do {
                    read = reader.read(buffer)
                    if (read < 0) {
                        break
                    }
                    output.append(buffer, 0, read)
                } while (read > 0)
                reader.close()

                process.waitFor()
                withContext(Dispatchers.Main) {
                    _crashLog.value = output.toString()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _crashLog.value = "Load crashlog failed, the error message is ${e.message}"
                }
            }
        }
    }

    fun cleanCrashLog() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val process = Runtime.getRuntime().exec("logcat -b crash -c")
                process.waitFor()

                refreshCrashLog()
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _crashLog.value = "Failed to clean crash log, the error message is ${e.message}"
                }
            }
        }
    }
}