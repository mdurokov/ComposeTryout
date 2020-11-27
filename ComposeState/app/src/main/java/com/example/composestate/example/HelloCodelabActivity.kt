package com.example.composestate.example

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composestate.databinding.ActivityHelloCodelabBinding

class HelloCodelabActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHelloCodelabBinding
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.textInput.doAfterTextChanged { text ->
            name = text.toString()
            updateHello()
        }
    }

    private fun updateHello() {
        binding.helloText.text = "Hello, $name"
    }
}

class HelloCodelabViewModel: ViewModel() {
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    fun onNameChanged(newName: String){
        _name.value = newName
    }
}

class HelloActivityWithViewModel: AppCompatActivity(){
    val helloViewModel by viewModels<HelloCodelabViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHelloCodelabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textInput.doAfterTextChanged { text ->
            helloViewModel.onNameChanged(text.toString())
        }

        helloViewModel.name.observe(this) { name ->
            binding.helloText.text = "Hello, $name"
        }
    }
}